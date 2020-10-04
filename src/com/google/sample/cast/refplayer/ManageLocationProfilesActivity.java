package com.google.sample.cast.refplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.sample.cast.refplayer.database.LocationProfileRepo;

public class ManageLocationProfilesActivity extends AppCompatActivity {
    
    private Button m_btnDone;

    private ImageButton m_imgBtnAdd;

    TextView txtLocationProfileId,
            txtLocationProfileName;

    LocationProfileRepo m_repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_location_profiles);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        //ab.setDisplayHomeAsUpEnabled(true);

        setupView();
        setupListeners();
        loadList();

    }

    private void setupView() {
        m_btnDone = findViewById(R.id.btn_done);

        //TODO: Replace with select from list
        m_imgBtnAdd = findViewById(R.id.img_btn_add_location_profile);
    }

    private void setupListeners() {

        m_btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivityIntent);
            }
        });

        m_imgBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addLocationProfile = new Intent(getApplicationContext(),LocationProfileDetail.class);
                addLocationProfile.putExtra("location_profile_Id",0);
                startActivity(addLocationProfile);
            }
        });

    }

    private void loadList() {

        m_repo = new LocationProfileRepo(getApplicationContext());

        ArrayList<HashMap<String, String>> locationProfileList =  m_repo.getLocationProfileList();
        if(locationProfileList.size()!=0) {
            ListView lv = findViewById(R.id.lst_location_profiles);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    txtLocationProfileId = view.findViewById(R.id.txt_location_profile_id);
                    String locationProfileId = txtLocationProfileId.getText().toString();
                    txtLocationProfileName = view.findViewById(R.id.txt_location_profile_name);
                    String locationProfileName = txtLocationProfileName.getText().toString();
                    Intent objIndent = new Intent(getApplicationContext(),LocationProfileDetail.class);
                    objIndent.putExtra("locationProfile_Id", Integer.parseInt( locationProfileId));
                    objIndent.putExtra("locationProfile_name", locationProfileName);
                    startActivity(objIndent);
                }

            });
            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    txtLocationProfileId = view.findViewById(R.id.txt_location_profile_id);
                    int locationProfileId = Integer.parseInt(txtLocationProfileId.getText().toString());
                    txtLocationProfileName = view.findViewById(R.id.txt_location_profile_name);
                    String locationProfileName = txtLocationProfileName.getText().toString();

                    m_repo.delete(locationProfileId);
                    loadList();
                    return true;
                }
            });
            ListAdapter adapter = new SimpleAdapter( ManageLocationProfilesActivity.this,
                    locationProfileList, R.layout.view_location_profile_entry, new String[] { "id","name"}, new int[] {R.id.txt_location_profile_id, R.id.txt_location_profile_name});
            lv.setAdapter(adapter);
        }else{
            Toast.makeText(getApplicationContext(),"No profiles!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadList();
    }

}