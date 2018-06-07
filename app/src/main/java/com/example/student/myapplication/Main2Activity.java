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

public class Main2Activity extends AppCompatActivity {

    private RelativeLayout rel_frame;
    private Main2Activity main2Activity;

    private View rel_qr;
    private View rel_dwm;
    private View rel_lunch;
    private View rel_logout;

    SocketStuff socketStuff;

    SurfaceView cameraPreview;
    CameraSource cameraSource;

    final int RequestCameraPermissionID = 1001;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("myTag", "This is my message1");
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                        return;
                    }
                    try {
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
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

        rel_lunch = inflater.inflate(R.layout.lunch_menu, null);

        rel_qr = inflater.inflate(R.layout.activity_qr,null);

        rel_logout = inflater.inflate(R.layout.activity_logout, null);

        prepareNavigator();
        prepareLoggout();
        prepareConnection();
    }



    private void setLunchMenu(final LinearLayout layout){
        layout.removeAllViews();

        Button button1 = findViewById(R.id.lunch_button1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Log.d("lunch button 1","onclick");
                layout.setBackgroundColor(Color.LTGRAY);
            }
        });

        Button button2 = findViewById(R.id.lunch_button2);
        button2.setOnClickListener(new View.OnClickListener() {

            int a = 0;

            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Log.d("lunch button 2","onclick");
                new Thread(){
                    public void run() {
                        super.run();
                        try {
                            socketStuff.clientThread.send("random text baegwae");
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                }.start();
            }

        });
        for(int i = 0; i< 5 ; i++){

            TextView tv = new TextView(main2Activity);
            tv.setText("hello lunch menu");
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,10,0,0);
            layout.addView(tv,params);
        }
    }

    private void prepareNavigator(){
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_qrattendance:
                        rel_frame.removeAllViews();
                        rel_frame.addView(rel_qr);
                        prepareCameraView();
                        return true;
                    case R.id.navigation_lunchmenu:
                        rel_frame.removeAllViews();
                        rel_frame.addView(rel_lunch);
                        setLunchMenu((LinearLayout)findViewById(R.id.lunch_menu));
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

    private void prepareCameraView(){

        cameraPreview = (SurfaceView) findViewById(R.id.cameraPreview);

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(main2Activity).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(main2Activity, barcodeDetector).setRequestedPreviewSize(640, 320).build();

        final TextView cameraResult = (TextView) findViewById(R.id.cameraResult);

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(main2Activity, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //Request permission
                    ActivityCompat.requestPermissions(main2Activity,new String[]{android.Manifest.permission.CAMERA},RequestCameraPermissionID);
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if(qrcodes.size() != 0){
                    cameraResult.post(new Runnable() {
                        @Override
                        public void run() {
                            String s = qrcodes.valueAt(0).displayValue;
                            cameraResult.setText(s);
                            Log.d("my value ", "run: "+s);
                        }
                    });
                }
            }
        });



    }

    private void prepareLoggout(){
        Button logout = (Button)rel_logout.findViewById(R.id.logout);
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

    private void prepareConnection(){
        socketStuff = new SocketStuff();
        socketStuff.connect();
    }

}
