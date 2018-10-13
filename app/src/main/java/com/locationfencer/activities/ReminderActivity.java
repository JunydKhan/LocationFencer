package com.locationfencer.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.locationfencer.R;
import com.locationfencer.database.AppDatabase;
import com.locationfencer.database.Location;
import com.locationfencer.utils.AppGlobals;
import com.locationfencer.utils.AppUtils;
import com.locationfencer.utils.BackNavigationActivity;
import com.locationfencer.utils.GeofenceTransitionsJobIntentService;


public class ReminderActivity extends BackNavigationActivity {
    public static AppDatabase appDatabase;
    private int PLACE_PICKER_REQUEST = 112;
    private int radius = 50;
    private GeofencingClient mGeofencingClient;
    private Place place;
    private PendingIntent mGeofencePendingIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtils.setFullScreen(this);
        setContentView(R.layout.activity_reminder);
        getAppDatabase();
        getGeofenceClient();
        bindListeners();
    }

    private void getAppDatabase() {
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, AppGlobals.DATABASE_NAME).allowMainThreadQueries().build();
    }

    private void getGeofenceClient() {
        mGeofencingClient = LocationServices.getGeofencingClient(this);
    }

    private void bindListeners() {
        findViewById(R.id.et_place_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(ReminderActivity.this), PLACE_PICKER_REQUEST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        SeekBar seekBar = findViewById(R.id.seekbar_radius);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radius = progress;
                findTextViewById(R.id.tv_seeked_value).setText(progress + " meters");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        findViewById(R.id.btn_set_reminder).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                setReminder();
            }
        });

        setOnBackClickListener();
    }

    @SuppressLint("NewApi")
    private void setReminder() {
        if (ActivityCompat.checkSelfPermission(ReminderActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(ReminderActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (!areParamsValid())
                return;

            Geofence geofence = getGeofence();
            GeofencingRequest geofencingRequest = getGeofencingRequest(geofence);
            saveReminderToDb(geofence);

            mGeofencingClient.addGeofences(geofencingRequest, getGeofencePendingIntent())
                    .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Reminder", "onSuccess");
                            Toast.makeText(ReminderActivity.this, "Reminder set succefully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Reminder", "onFailure");
                            Toast.makeText(ReminderActivity.this, "Reminder failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 333);
    }

    private boolean areParamsValid() {
        if (place == null) {
            Toast.makeText(this, "Please select location", Toast.LENGTH_SHORT).show();
            return false;
        } else if (findFieldById(R.id.et_reminder_text).getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter reminder text", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @NonNull
    private void saveReminderToDb(Geofence geofence) {
        Location location = new Location(
                geofence.getRequestId(),
                place.getName().toString(),
                findFieldById(R.id.et_reminder_text).getText().toString());

        try {
            appDatabase.appDao().insertLocation(location);
        } catch (Exception e) {
            Toast.makeText(this, "This location has already added", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private GeofencingRequest getGeofencingRequest(Geofence geofence) {
        GeofencingRequest builder = new GeofencingRequest.Builder()
                .addGeofence(geofence)
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .build();
        return builder;
    }

    @NonNull
    private Geofence getGeofence() {
        return new Geofence.Builder()
                .setRequestId(place.getId())
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setCircularRegion(
                        place.getLatLng().latitude,
                        place.getLatLng().longitude,
                        radius
                )
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .build();
    }

    private PendingIntent getGeofencePendingIntent() {
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsJobIntentService.class);
        mGeofencePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace(data, this);
                findFieldById(R.id.et_place_name).setText(place.getName());
            }
        }
    }

    public EditText findFieldById(@IdRes int id) {
        return (EditText) findViewById(id);
    }

    public TextView findTextViewById(@IdRes int id) {
        return (TextView) findViewById(R.id.tv_seeked_value);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 333)
            if (permissions.length > 0)
                setReminder();
    }
}
