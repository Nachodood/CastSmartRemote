package com.google.sample.cast.refplayer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.sample.cast.refplayer.database.PivotDeviceProfileDBItem;
import com.google.sample.cast.refplayer.database.PivotRepo;

import static android.view.WindowManager.LayoutParams;

/*
Vectores and axis:
When device screen faces the user:
y: ^ Up down
x: <-> Left right
z: back and fore

Think of the left-hand rule                                     //TODO: I SHOULD REALLY TIDY THIS UP
 */

//https://developer.android.com/reference/android/net/wifi/WifiManager
//https://developer.android.com/training/connect-devices-wirelessly/wifi-direct Create P2P connections with Wi-Fi Direct

//https://developers.google.com/nearby/connections/overview
//https://developer.android.com/training/connect-devices-wirelessly

public class MainActivity extends AppCompatActivity implements SensorEventListener, NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout m_drawerLayout;
    NavigationView m_navView;
    androidx.appcompat.widget.Toolbar m_toolbar;

    View m_pivotDialogueView;

    ImageView imgMain;

    private SensorManager m_sensorManager;

    private Sensor m_sensorAccelerometer,
                    m_sensorProximity,
                    m_sensorGameRotation,
                    m_sensorCompass;

    PivotRepo m_pivotRepo;

    int m_selectedLocationID;
    PivotDeviceProfileDBItem m_selectedPivotLocationProfile;

    ArrayList<HashMap<String, String>> m_locationProfileList;

    ///////////////////////////// VIBRATION /////////////////////////////
    private Vibrator m_vibrator;
    private long[] m_pattern = {100, 100, 100, 10 };
    private String m_vibratorService,
            m_SensorsNotProvideByDevice;

    private boolean m_isgestureListen = false;

    private int m_compassValue,
            m_intCastValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);

        m_sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        ////////////////////////////////////////////////////////////////////////////// COMPASS TESTING //////////////////////////////////////////////////////////////////////////////
        m_sensorCompass = m_sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        m_sensorManager.registerListener(this, m_sensorCompass, SensorManager.SENSOR_DELAY_NORMAL);
        deviceHasSensors();

        setupVibration();
        //setupWiFi();
        setupView();
        setupListeners();
        //pivotTestAdd();
        //listTest();
        setupDrawer();

        imgMain = findViewById(R.id.img_background);////////////////////////////////////////// Set onFling up for videos

    }

    /////////////////////////////////////////////////////// SENSOR CHECK /////////////////////////////////////////////////////

    //Check if device has all sensors
    //Return if required sensor is unavailable, give notification and limit functions/features
    private void deviceHasSensors() {

        List<Sensor> m_deviceSensorsList = m_sensorManager.getSensorList(Sensor.TYPE_ALL);
        ArrayList<Sensor> m_requiredSensorsList = new ArrayList<>();

        m_sensorAccelerometer = m_sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        /*
        Sensor.TYPE_LINEAR_ACCELERATION
Software Sensor
Measure acceleration force applied to device in three axes
excluding the force of gravity
         */

        //TYPE_ORIENTATION has been deprecated
        Sensor m_sensorGyro = m_sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        m_sensorProximity = m_sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        m_sensorGameRotation = m_sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);

        m_requiredSensorsList.add(m_sensorAccelerometer);
        m_requiredSensorsList.add(m_sensorGyro);
        m_requiredSensorsList.add(m_sensorProximity);
        m_requiredSensorsList.add(m_sensorGameRotation);

        if (!m_deviceSensorsList.contains(m_sensorAccelerometer)) {
            m_SensorsNotProvideByDevice += " Accelerometer";
        }

        if (!m_deviceSensorsList.contains(m_sensorGyro)) {m_SensorsNotProvideByDevice += " Gyroscope";}

        if (!m_deviceSensorsList.contains(m_sensorProximity)) {m_SensorsNotProvideByDevice += " Proximity";}

        if (!m_deviceSensorsList.contains(m_sensorGameRotation)) {m_SensorsNotProvideByDevice += " game_rotation";}

        if(m_SensorsNotProvideByDevice != null){
            openErrorDialogue(m_SensorsNotProvideByDevice);
        }

    }

    /////////////////////////////////////////////////////// VIBRATION ////////////////////////////////////////////////////////
    private void setupVibration() {
        //https://developer.android.com/reference/android/os/Vibrator
        //https://www.android-examples.com/start-stop-android-vibrate-example-tutorial/

        m_vibratorService = Context.VIBRATOR_SERVICE;
        m_vibrator = (Vibrator)getSystemService(m_vibratorService);

    }

    /////////////////////////////////////////////////////// WIFI STUFF ///////////////////////////////////////////////////////


    /////////////////////////////////////////////////////// VIEW STUFF ///////////////////////////////////////////////////////
    private void setupView() {

        //m_imgGesturePerformed = findViewById(R.id.img_gesture_performed);

        //m_imgBtnGestureListen = findViewById(R.id.imgbtn_listen_gesture);
        //m_imgBtnChooseLocationList = findViewById(R.id.img_btn_list_locations);

    }

    ///////////////////////////////////////////////////////    Drawer  ///////////////////////////////////////////////////////
    public void setupDrawer(){

        m_drawerLayout = findViewById(R.id.drawer_layout);
        m_navView = findViewById(R.id.nav_view);

        m_navView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, m_drawerLayout, m_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        m_drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        m_navView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {

        if(m_drawerLayout.isDrawerOpen(GravityCompat.END)){
            m_drawerLayout.closeDrawer(GravityCompat.END);
        } else {

            super.onBackPressed();

        }

    }

    /////////////////////////////////////////////////////// LISTENERS ///////////////////////////////////////////////////////
    private void setupListeners() {

        /////////////////////////////////////////////////////// LISTENERS SENSORS ///////////////////////////////////////////////////////
        //linear acceleration = acceleration - acceleration due to gravity
        m_sensorAccelerometer = m_sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        m_sensorManager.registerListener(this, m_sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        m_sensorManager.registerListener(this, m_sensorProximity, SensorManager.SENSOR_DELAY_NORMAL);
        m_sensorManager.registerListener(this, m_sensorGameRotation, SensorManager.SENSOR_DELAY_GAME);

        m_sensorManager.registerListener(this, m_sensorCompass, SensorManager.SENSOR_DELAY_NORMAL);

    }

    /////////////////////////////////////////////////////// ERROR DIALOGUE /////////////////////////////////////////////////
    private void showErrorDialogue(){
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.error_dialogue, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        final TextView txtViewErrorMessage = promptView.findViewById(R.id.txt_error_message);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    /////////////////////////////////////////////////////// LIST DIALOGUE /////////////////////////////////////////////////
    private void showPivotListDialogue(){
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        m_pivotDialogueView = layoutInflater.inflate(R.layout.pivot_list_dialogue, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(m_pivotDialogueView);

        loadPivotList(m_pivotDialogueView);
        // setup a dialog window
//        alertDialogBuilder.setCancelable(false)
//                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });

        // create an alert dialog

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void loadPivotList(View pivotDialogueView){

        m_pivotRepo = new PivotRepo(getApplicationContext());

        m_locationProfileList =  m_pivotRepo.getPivotList();
        if(m_locationProfileList.size()!=0) {
            ListView lv = pivotDialogueView.findViewById(R.id.lst_location_pivot);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    TextView txtPivotId = view.findViewById(R.id.txt_pivot_id);
                    TextView txtPivotBearing = view.findViewById(R.id.txt_pivot_bearing);
                    int pivotId = Integer.parseInt(txtPivotId.getText().toString());

                    m_selectedLocationID = pivotId;
                    m_selectedPivotLocationProfile = m_pivotRepo.getPivotById(pivotId);

                }

            });

            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView txtPivotId = view.findViewById(R.id.txt_pivot_id);
                    TextView txtPivotBearing = view.findViewById(R.id.txt_pivot_bearing);
                    int pivotId = Integer.parseInt(txtPivotId.getText().toString());
                    m_pivotRepo.delete(pivotId);
                    //Reload list
                    return false;
                }
            });
            ListAdapter adapter = new SimpleAdapter( MainActivity.this,
                    m_locationProfileList, R.layout.view_pivot_entry, new String[] { "id","bearing"}, new int[] {R.id.txt_pivot_id, R.id.txt_pivot_bearing});
            lv.setAdapter(adapter);
        }else{
            Toast.makeText(getApplicationContext(),"No pivots!", Toast.LENGTH_SHORT).show();
        }

    }

    /////////////////////////////////////////////////////// DEVICE CHECKS //////////////////////////////////////////////////
    private void openErrorDialogue(String m_SensorsNotProvideByDevice) {

        ErrorDialogue errorDialogue = new ErrorDialogue(m_SensorsNotProvideByDevice);
        errorDialogue.show(getSupportFragmentManager(), "example dialogue");

    }

    ///////////////////////////////////////////////////////  SENSORS    ///////////////////////////////////////////////////
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

//                       float m_orientx = sensorEvent.values[0];
//                        float m_orienty = sensorEvent.values[1];
//                        float m_orientz = sensorEvent.values[2];
//                        if (m_orientz < 90 && 60 < m_orientz && 80 < m_orientx && m_orientx <100) {
//                            Toast.makeText(getApplicationContext(),"Right horizontal tilt", Toast.LENGTH_SHORT).show();
//                       } else if (m_orientx > 255 && 265 > m_orientx) {
//                           Toast.makeText(getApplicationContext(),"Anticlockwise", Toast.LENGTH_SHORT).show();
//                        }

//        Ø public static float getAltitude(float p0, float p):
//        Computes the Altitude in meters from the atmospheric pressure and the
//        pressure at sea level.
//        Ø public static float[] getOrientation(float[] R,
//        float[] values): Computes the device's orientation based on the
//        rotation matrix.
//        Ø public static float getInclination(float[] I):
//        Computes the geomagnetic inclination angle in radians from the
//        inclination matrix I returned by getRotationMatrix(float[],
//        float[], float[], float[]).
//        Ø public static void getAngleChange(float[]
//        angleChange, float[] R, float[] prevR): Helper function to
//        compute the angle change between two rotation matrices.
//        Ø More public function: getRotationMatrix,
//                getQuaternionFromVector, getRotationMatrixFromVector

        if(sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            m_compassValue = (int) sensorEvent.values[0];
        }

        if (m_isgestureListen == true) {
            synchronized (this) {
                switch (sensorEvent.sensor.getType()) {

                    case Sensor.TYPE_LINEAR_ACCELERATION:

                        float x = sensorEvent.values[0];
                        if (x > 10) {
                            Toast.makeText(getApplicationContext(),"Right ", Toast.LENGTH_SHORT).show();
                        } else if (x < -10) {
                            Toast.makeText(getApplicationContext(),"Left", Toast.LENGTH_SHORT).show();
                        }

                        float z = sensorEvent.values[2];
                        if (z > 10) {
                            Toast.makeText(getApplicationContext(),"Up", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case Sensor.TYPE_GAME_ROTATION_VECTOR:
                        float rotationX = sensorEvent.values[0];
                        //TextView txtX = findViewById(R.id.txt_rotation_x);
                        //txtX.setText("X: " + rotationX);

                        float rotationY = sensorEvent.values[1];
                        //TextView txtY = findViewById(R.id.txt_rotation_y);
                        //txtY.setText("Y: " + rotationY);

                        float rotationZ = sensorEvent.values[2];
                        //TextView txtZ = findViewById(R.id.txt_rotation_z);
                        //txtZ.setText("Z: " + rotationZ);

                        int m_testAlexa = 270;
                        int m_testLight = 320;
                        int m_testFridge = 180;

                        if(rotationX < 0.1 && rotationY > 0.3){
                            if(m_compassValue < m_testAlexa +10 && m_compassValue > m_testAlexa - 10) {
                                Toast.makeText(getApplicationContext(), "Tilt right at Alexa", Toast.LENGTH_SHORT).show();
                                //m_vibrator.vibrate(m_pattern, -1);
                                m_vibrator.vibrate(1);
                            }
                            if(m_compassValue < m_testLight +10 && m_compassValue > m_testLight - 10) {
                                Toast.makeText(getApplicationContext(), "Tilt right at m_testLight", Toast.LENGTH_SHORT).show();
                                //m_vibrator.vibrate(m_pattern, -1);
                                m_vibrator.vibrate(10);
                            }
                            if(m_compassValue < m_testFridge +10 && m_compassValue > m_testFridge - 10) {
                                Toast.makeText(getApplicationContext(), "Tilt right at m_testFridge", Toast.LENGTH_SHORT).show();
                                //m_vibrator.vibrate(m_pattern, -1);
                                m_vibrator.vibrate(20);
                            }
                        }
                        if(rotationX < 0.1 && rotationY < -0.3) {
                            if(m_compassValue < m_testAlexa *1.05 && m_compassValue > m_testAlexa *0.95) {
                                Toast.makeText(getApplicationContext(), "Tilt left at Alexa", Toast.LENGTH_SHORT).show();

                                //m_vibrator.vibrate(m_pattern, -1);

                                m_vibrator.vibrate(100); // Vibrate for 1 second.
                            }
                            if(m_compassValue < m_testLight +10 && m_compassValue > m_testLight - 10) {
                                Toast.makeText(getApplicationContext(), "Tilt left at m_testLight", Toast.LENGTH_SHORT).show();
                                //m_vibrator.vibrate(m_pattern, -1);
                                m_vibrator.vibrate(10);
                            }
                            if(m_compassValue < m_testFridge +10 && m_compassValue > m_testFridge - 10) {
                                Toast.makeText(getApplicationContext(), "Tilt left at m_testFridge", Toast.LENGTH_SHORT).show();
                                //m_vibrator.vibrate(m_pattern, -1);
                                m_vibrator.vibrate(20);
                            }
                        }
                        if(rotationX < -0.3) {
                            if(m_compassValue < m_testAlexa *1.05 && m_compassValue > m_testAlexa *0.95) {
                                Toast.makeText(getApplicationContext(), "Tilt forward at Alexa", Toast.LENGTH_SHORT).show();

                                m_vibrator.vibrate(m_pattern, -1);
                                //m_vibrator.vibrate(100); // Vibrate for 1 second.
                            }
                            if(m_compassValue < m_testLight +10 && m_compassValue > m_testLight - 10) {
                                Toast.makeText(getApplicationContext(), "Tilt forward at m_testLight", Toast.LENGTH_SHORT).show();
                                //m_vibrator.vibrate(m_pattern, -1);
                                m_vibrator.vibrate(10);
                            }
                            if(m_compassValue < m_testFridge +10 && m_compassValue > m_testFridge - 10) {
                                Toast.makeText(getApplicationContext(), "Tilt forward at m_testFridge", Toast.LENGTH_SHORT).show();
                                //m_vibrator.vibrate(m_pattern, -1);
                                m_vibrator.vibrate(20);
                            }
                        }

//                        case Sensor.TYPE_ORIENTATION:
//                            m_compassValue = (int) sensorEvent.values[0];
                }
            }
        }

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Menu menu = m_navView.getMenu();

        switch(item.getItemId()){
            case R.id.nav_set_cast_bearing:

                m_intCastValue = m_compassValue;

                Toast.makeText(getApplicationContext(), "Sensor: " + m_compassValue + " SetValue: " + m_intCastValue, Toast.LENGTH_SHORT)
                        .show();
                //m_selectedLocation = item;
//                //menu.findItem(item.getItemId()).setVisible(false);
//                break;
//
//            case R.id.nav_example_location2:
//                Toast.makeText(getApplicationContext(), "Example 2", Toast.LENGTH_SHORT)
//                        .show();
//                break;
//
//            case R.id.nav_example_location3:
//                Toast.makeText(getApplicationContext(), "Example 3", Toast.LENGTH_SHORT)
//                        .show();
//                break;
//
//            case R.id.nav_locations:
//                 Intent manageLocationIntent = new Intent(this, ManageLocationProfilesActivity.class);
//                    startActivity(manageLocationIntent);
        }

        m_drawerLayout.closeDrawer(GravityCompat.END);

        //To default to an item:
        //m_navView.setCheckedItem(R.id.nav_home);

        return true;
    }

    //TODO: Register and unregister sensors
//    @Override
//    protected void onResume() {
//        super.onResume();
//       m_sensorManager.registerListener(this, m_sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//        m_sensorManager.registerListener(this, m_sensorGyro, SensorManager.SENSOR_DELAY_NORMAL);
//
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//       //m_sensorManager.unregisterListener(this);
//    }

}

/////////////////////////////////////////////// REDUNDANT CODE

//                    case Sensor.TYPE_ORIENTATION:
//                        float m_orientx = sensorEvent.values[0];
//                     float m_orienty = sensorEvent.values[1];
//                        float m_orientz = sensorEvent.values[2];
//                        if (m_orientz < 90 && 60 < m_orientz && 80 < m_orientx && m_orientx <100) {
//                            Toast.makeText(getApplicationContext(),"Right horizontal tilt", Toast.LENGTH_SHORT).show();
//                       } //else if (m_orientx > 255 && 265 > m_orientx) {
//                           //Toast.makeText(getApplicationContext(),"Anticlockwise", Toast.LENGTH_SHORT).show();
//                        //}
//                        break;

//        if(sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY && sensorEvent.values[0] > 7){
////            Window window = this.getWindow(); //to get the window of your activity;
////            window.addFlags(FLAG_TURN_SCREEN_ON);
////            //WindowManager.LayoutParams(FLAG_TURN_SCREEN_ON) //that you desire:            FLAG_TURN_SCREEN_ON
//        }

//        m_imgGesturePerformed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(m_isgestureListen == false){
//                    m_isgestureListen = true;
//                    m_imgGesturePerformed.setImageResource(R.drawable.shitty_go);
//                }
//                else
//                {
//                    m_isgestureListen = false;
//                }
//            }
//        });
/////////////////////////////////////////////////////// LISTENERS IMAGES ///////////////////////////////////////////////////////
//        m_imgLocations.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent manageLocationProfilesIntent = new Intent(getApplicationContext(), ManageLocationProfilesActivity.class);
//                startActivity(manageLocationProfilesIntent);
//            }
//        });
//
//        m_imgLocations.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Intent CompassCalibrateintent = new Intent(getApplicationContext(), CompassCalibrateActivity.class);
//                startActivity(CompassCalibrateintent);
//                return false;
//            }
//        });
//
//        m_imgDevices.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent manageDevicesIntent = new Intent(getApplicationContext(), ManageDevicesActivity.class);
//                startActivity(manageDevicesIntent);
//            }
//        });
//
//        m_imgGestures.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent manageGesturesIntent = new Intent(getApplicationContext(), ManageGesturesActivity.class);
//                startActivity(manageGesturesIntent);
//            }
//        });
/////////////////////////////////////////////////////// LISTENERS BUTTONS ///////////////////////////////////////////////////////
/*        m_imgBtnGestureListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m_isgestureListen == true) {

                    Toast.makeText(getApplicationContext()," m_isgestureListen = false; ", Toast.LENGTH_SHORT).show();
                    m_isgestureListen = false;

                }
                if(m_isgestureListen == false) {

                    Toast.makeText(getApplicationContext()," m_isgestureListen = true; ", Toast.LENGTH_SHORT).show();
                    m_isgestureListen = true;

                }
            }
        });
 */

        /* m_imgBtnChooseLocationList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPivotListDialogue();
            }
        });
        */

//        m_btnConnect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent connectActivityIntent = new Intent(getApplicationContext(), ConnectActivity.class);
//                startActivity(connectActivityIntent);
//            }
//        });

//    public void listTest(){
//
//        TextView txtListTest = findViewById(R.id.txt_list_test);
//
//        m_pivotRepo = new PivotRepo(getApplicationContext());
//        ArrayList<HashMap<String, String>> pivotList =  m_pivotRepo.getPivotList();
//
//        txtListTest.setText(pivotList.toString());
//
//    }

////////////////////////////////////////////////////////////////////// PIVOT STUFF //////////////////////////////////////////////////////////////////////
//m_txtSelectedLocationProfile.setText("Selected location: " + m_locationProfileList.get(m_selectedLocationID));

//                    TextView txtLocationProfileName = view.findViewById(R.id.txt_location_profile_name);
//                    String locationProfileName = txtLocationProfileName.getText().toString();
//
//                    //getPivotById();
//                    txt_pivot_location_profile_id
//                            txt_device_id
//                    txt_pivot_device_name
//                            txt_pivot_location_profile_name
//                    txt_pivot_bearing
//m_pivotRepo.delete(pivotId);
