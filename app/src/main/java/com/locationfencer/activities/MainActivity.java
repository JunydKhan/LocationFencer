package com.locationfencer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.location.places.ui.PlacePicker;
import com.locationfencer.R;
import com.locationfencer.utils.AppUtils;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtils.setFullScreen(this);
        setContentView(R.layout.activity_main);
        registerListeners();
    }

    private void registerListeners() {
        findViewById(R.id.btn_set_reminder).setOnClickListener(this);
        findViewById(R.id.btn_note).setOnClickListener(this);
        findViewById(R.id.btn_bookmark).setOnClickListener(this);
        findViewById(R.id.btn_nearby).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_set_reminder: {
                startActivity(new Intent(MainActivity.this, ShowRemindersActivity.class));
                break;
            }
            case R.id.btn_note: {
                startActivity(new Intent(MainActivity.this, ShowNotesActivity.class));
                break;
            }
            case R.id.btn_bookmark: {
                startActivity(new Intent(MainActivity.this, BookMarksActivity.class));
                break;
            }
            case R.id.btn_nearby: {
                gotoPlacesPicker();
                break;
            }
        }
    }

    private void gotoPlacesPicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(MainActivity.this), 000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
