package com.example.student.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private RelativeLayout relativeLayout;
    private MainActivity this_ref;

    private View rel_dwm;
    private View rel_lunch;

    SurfaceView cameraPreview;
    TextView cameraResult;

    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_qrattendance:
                    relativeLayout.removeAllViews();
                    relativeLayout.addView(rel_dwm);
                    return true;
                case R.id.navigation_lunchmenu:
                    relativeLayout.removeAllViews();
                    relativeLayout.addView(rel_lunch);
                    return true;
                case R.id.navigation_schedule:
                    relativeLayout.removeAllViews();
                    relativeLayout.addView(rel_dwm);
                    return true;
                case R.id.navigation_settings:
                    mTextMessage.setText(R.string.title_settings);
                    return true;
            }
            return false;
        }
    };

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
        setContentView(R.layout.activity_qr);
        this_ref = this;

        relativeLayout = (RelativeLayout) findViewById(R.id.rel1);
        mTextMessage = (TextView) findViewById(R.id.messagerr);


        //Camera
        barcodeDetector = new BarcodeDetector.Builder(this_ref).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(this_ref, barcodeDetector).setRequestedPreviewSize(640, 480).build();

        Log.d("myTag", "This is my message2");


        cameraPreview = (SurfaceView) findViewById(R.id.cameraPreview);
        cameraResult = (TextView) findViewById(R.id.cameraResult);

        LayoutInflater inflater = LayoutInflater.from(this_ref); // 1
        rel_dwm = inflater.inflate(R.layout.activity_dwm, null);

        inflater = LayoutInflater.from(this_ref); // 1
        rel_lunch = inflater.inflate(R.layout.lunch_menu, null);

        Log.d("myTag", "This is my message3");

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(this_ref, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //Request permission
                    ActivityCompat.requestPermissions(this_ref,new String[]{android.Manifest.permission.CAMERA},RequestCameraPermissionID);
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

        Log.d("myTag", "This is my message5");


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

        Log.d("myTag", "This is my message4");


        //BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
