package com.example.student.myapplication;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ManagerQR {

    AppCompatActivity activity;
    RelativeLayout layout;


    SurfaceView cameraPreview;
    CameraSource cameraSource;

    final int RequestCameraPermissionID = 1001;

    private int state;
    public volatile boolean barcodeRead;

    public ManagerQR(RelativeLayout layout, AppCompatActivity activity){
        this.activity = activity;
        this.layout = layout;

        Button button1 = layout.findViewById(R.id.qr_button1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                if(state ==1){

                }else{
                    Log.d("managerQR btn1", " do nothing ");
                }

            }
        });

        Button button2 = layout.findViewById(R.id.qr_button2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button

                if(state ==2){

                }else{
                    Log.d("managerQR btn2", " do nothing ");
                }
            }
        });


    }



    public void setState(int i){
        this.state = i;
        switch (state){
            case(0):
                ((Button)layout.findViewById(R.id.qr_button1)).setTextColor(Color.BLACK);
                ((Button)layout.findViewById(R.id.qr_button2)).setTextColor(Color.GRAY);

                ((Button)layout.findViewById(R.id.qr_button1)).setText("출근");
                ((Button)layout.findViewById(R.id.qr_button2)).setText("퇴근");
                break;
            case(1):
                ((Button)layout.findViewById(R.id.qr_button1)).setTextColor(Color.GRAY);
                ((Button)layout.findViewById(R.id.qr_button1)).setText("출근됨");
                ((Button)layout.findViewById(R.id.qr_button2)).setTextColor(Color.BLACK);
                break;
                case(2):
                    ((Button)layout.findViewById(R.id.qr_button1)).setTextColor(Color.GRAY);
                    ((Button)layout.findViewById(R.id.qr_button2)).setTextColor(Color.GRAY);
                    ((Button)layout.findViewById(R.id.qr_button2)).setText("퇴근됨");
                    break;
        }
    }

    public void cameraPermitted(int requestCode,int[] grantResults){
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
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

    private void prepareCameraView(){

        barcodeRead = false;
        cameraPreview = (SurfaceView)layout.findViewById(R.id.cameraPreview);

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(activity).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(activity, barcodeDetector).setRequestedPreviewSize(640, 320).build();

        final TextView cameraResult = (TextView)layout.findViewById(R.id.cameraResult);

        //cameraPreview.setRotation(180);

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //Request permission
                    ActivityCompat.requestPermissions(activity,new String[]{android.Manifest.permission.CAMERA},RequestCameraPermissionID);
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
                if(!barcodeRead) {
                    final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                    if (qrcodes.size() != 0) {
                        cameraResult.post(new Runnable() {
                            @Override
                            public void run() {
                                String s = qrcodes.valueAt(0).displayValue;
                                cameraResult.setText(s);
                                SocketStuff.send(SocketStuff.format_qr_(s));
                            }
                        });
                    }
                    barcodeRead = true;
                }
            }
        });



    }

    public void selected(ViewGroup viewGroup){
        viewGroup.removeAllViews();
        prepareCameraView();
        viewGroup.addView(this.layout);
    }

    public void setCheckArrive(){
        Log.d("managerQR", "setCheckArrive: ");
        layout.removeView(cameraPreview);
        TextView tv = new TextView(activity);
        tv.setText("waegtttttttttttttttttttttttttttttttttttttttttttttt");
        layout.addView(tv);
    }
    public void setCheckLeave(){
        Log.d("managerQR", "setCheckLeave: ");
        layout.removeView(cameraPreview);
        TextView tv = new TextView(activity);
        tv.setText("waegtttttttttttttttttttttttttttttttttttttttttttttt");
        layout.addView(tv);
    }

}

