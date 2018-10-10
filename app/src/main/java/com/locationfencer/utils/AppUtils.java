package com.locationfencer.utils;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;


/**
 * Created by JUNAID_KHAN on 28/05/2018.
 */

public class AppUtils {
    public static void setFullScreen(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
