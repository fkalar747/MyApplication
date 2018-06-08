package com.example.student.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main2Activity extends AppCompatActivity implements PostMsgable {

    private RelativeLayout rel_frame;
    private Main2Activity main2Activity;

    private View rel_logout;
    private View rel_dwm;

    private ManagerLunch managerLunch;
    private ManagerQR managerQR;

    private boolean is_lunch_loaded;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("myTag", "This is my message1");
        managerQR.cameraPermitted(requestCode,grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main2Activity = this;
        rel_frame = findViewById(R.id.relFrame);

        //Camera

        Log.d("myTag", "This is my message2");

        LayoutInflater inflater = LayoutInflater.from(main2Activity); // 1
        rel_dwm = inflater.inflate(R.layout.activity_dwm, null);
        rel_logout = inflater.inflate(R.layout.activity_logout, null);

        managerLunch = new ManagerLunch((RelativeLayout) inflater.inflate(R.layout.lunch_menu, null),this);
        managerQR = new ManagerQR((RelativeLayout)inflater.inflate(R.layout.activity_qr,null),this);

        prepareNavigator();
        prepareLoggout();
        SocketStuff.prepareConnection(this);
    }





    private void prepareNavigator(){
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_qrattendance:
                        managerQR.selected(rel_frame);
                        return true;
                    case R.id.navigation_lunchmenu:
                        managerLunch.selected(rel_frame);
                        return true;
                    case R.id.navigation_schedule:
                        rel_frame.removeAllViews();
                        rel_frame.addView(rel_dwm);
                        return true;
                    case R.id.navigation_settings:
                        rel_frame.removeAllViews();
                        rel_frame.addView(rel_logout);
                        return true;
                }
                return false;
            }
        });
    }



    private void prepareLoggout() {
        Button logout = (Button) rel_logout.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(intent);
                SharedPreferences prefs = getSharedPreferences("PrefName", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(Main2Activity.this, "로그아웃.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void postMsg(String msg){
        switch (msg.charAt(0)){
            case('1'):
                break;
            case('2'):
                String[] strings = msg.split("/");
                managerLunch.setLunchMenu(strings);
                break;
            case('3'):
                break;
            case('4'):
                break;
            default:
                break;
        }
    }
    @Override
    public void postToast(String s){
        Toast.makeText(Main2Activity.this, s, Toast.LENGTH_SHORT).show();
    }
}
