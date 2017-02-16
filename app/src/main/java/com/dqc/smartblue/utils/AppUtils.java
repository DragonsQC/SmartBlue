package com.dqc.smartblue.utils;

import android.app.Activity;
import android.content.Intent;

import java.util.List;

public class AppUtils {

    private List<String> mEventList = null;

    private static AppUtils _instance = null;

    public static AppUtils getInstance() {
        if (null == _instance) {
            return new AppUtils();
        }
        return _instance;
    }


    public static void exitApp() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public static void exitAppAndRestart(Activity activity) {
        exitApp();
        Intent i = activity.getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(activity.getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(i);
    }



}
