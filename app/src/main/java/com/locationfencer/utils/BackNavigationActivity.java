package com.locationfencer.utils;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.locationfencer.R;

/**
 * Created by JUNAID_KHAN on 13/10/2018.
 */

public class BackNavigationActivity extends Activity {

    protected void setOnBackClickListener() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
