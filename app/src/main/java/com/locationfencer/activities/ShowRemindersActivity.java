package com.locationfencer.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.locationfencer.R;
import com.locationfencer.adapters.RemindersAdapter;
import com.locationfencer.database.AppDatabase;
import com.locationfencer.database.Location;
import com.locationfencer.utils.AppGlobals;
import com.locationfencer.utils.BackNavigationActivity;

import java.util.List;

public class ShowRemindersActivity extends BackNavigationActivity implements RemindersAdapter.OnReminderGeofencingCallback, RemindersAdapter.OnReminderDeleteCallback {

    private List<Location> locationList;
    private AppDatabase appDatabase;
    private RemindersAdapter remindersAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_reminders);
        getAppDatabase();
        bindListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setReminderListing();
    }

    private void bindListeners() {
        findViewById(R.id.iv_add_reminder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowRemindersActivity.this,ReminderActivity.class));
            }
        });

        setOnBackClickListener();
    }

    private void setReminderListing() {
        locationList = appDatabase.appDao().selectLocations();
        if(locationList.size() == 0){
            findViewById(R.id.tv_no_reminders_found).setVisibility(View.VISIBLE);
            return;
        }
        remindersAdapter = new RemindersAdapter(this,locationList);
        RecyclerView recyclerViewReminders = findViewById(R.id.rv_reminders);
        recyclerViewReminders.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerViewReminders.setAdapter(remindersAdapter);
    }

    private void getAppDatabase() {
        appDatabase = Room.databaseBuilder(this, AppDatabase.class, AppGlobals.DATABASE_NAME).allowMainThreadQueries().build();
    }

    @Override
    public void onReminderMarkerClick(int position) {
        Location location = locationList.get(position);
        Intent intent = new Intent(this,GeofencePreviewActivity.class);
        intent.putExtra("location",location);
        startActivity(intent);
    }

    @Override
    public void onReminderDelete(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Reminder");
        builder.setMessage("Are you sure you to delete this reminder?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                appDatabase.appDao().deleteLocation(locationList.get(position));
                locationList.remove(position);
                remindersAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
