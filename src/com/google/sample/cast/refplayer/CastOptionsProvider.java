package com.google.sample.cast.refplayer;

import com.google.android.gms.cast.framework.CastOptions;
import com.google.android.gms.cast.framework.OptionsProvider;
import com.google.android.gms.cast.framework.SessionProvider;

import com.google.android.gms.cast.framework.media.CastMediaOptions;
import com.google.android.gms.cast.framework.media.NotificationOptions;

import android.content.Context;

import java.util.List;

import com.google.sample.cast.refplayer.expandedcontrols.ExpandedControlsActivity;

public class CastOptionsProvider implements OptionsProvider {

    @Override
    public CastOptions getCastOptions(Context context) {
        //NotificationOptions notificationOptions = new NotificationOptions.Builder()
        //        .setTargetActivityClassName(ExpandedControlsActivity.class.getName())
        //        .build();
        //CastMediaOptions mediaOptions = new CastMediaOptions.Builder()
        //        .setNotificationOptions(notificationOptions)
        //        .setExpandedControllerActivityClassName(ExpandedControlsActivity.class.getName())
        //        .build();

        return new CastOptions.Builder()
                .setReceiverApplicationId(context.getString(R.string.app_id))
                //.setCastMediaOptions(mediaOptions)
                .build();
    }

    @Override
    public List<SessionProvider> getAdditionalSessionProviders(Context context) {
        return null;
    }
}