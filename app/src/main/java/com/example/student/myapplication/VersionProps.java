package com.example.student.myapplication;

import android.os.Build;
import android.util.Log;

public class VersionProps {

    public static final void init(){
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        Log.d("version check","Android SDK: " + sdkVersion + " (" + release +")");

        surfaceViewRotate = 0;
        if(sdkVersion <20){
            surfaceViewRotate = 180;
        }

    }

    public static int surfaceViewRotate;

}
