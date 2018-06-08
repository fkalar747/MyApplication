package com.example.student.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ManagerLunch {

    AppCompatActivity activity;
    RelativeLayout layout;

    public ManagerLunch(RelativeLayout layout, AppCompatActivity activity){
        this.activity = activity;
        this.layout = layout;
        Button button1 = layout.findViewById(R.id.lunch_button1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Log.d("lunch button 1","onclick");
            }
        });

        Button button2 = layout.findViewById(R.id.lunch_button2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Log.d("lunch button 2","onclick");

            }
        });
    }




    public void setLunchMenu(String[] strings){

        for(int i = 0; i< 5 ; i++){

            TextView tv = new TextView(activity);
            tv.setText("hello lunch menu");
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,10,0,0);
            layout.addView(tv,params);
        }
    }

    public void selected(ViewGroup viewGroup){
        viewGroup.removeAllViews();
        viewGroup.addView(this.layout);
    }

}
