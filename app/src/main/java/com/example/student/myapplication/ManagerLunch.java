package com.example.student.myapplication;

import android.support.v4.content.res.ResourcesCompat;
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
    LinearLayout menu_layout;

    boolean isLunchReady;
    String[] menu;

    public ManagerLunch(RelativeLayout layout, AppCompatActivity activity){
        this.activity = activity;
        this.layout = layout;

        Button button1 = layout.findViewById(R.id.lunch_button1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                setLunchMenuAB('A');
            }
        });

        Button button2 = layout.findViewById(R.id.lunch_button2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                setLunchMenuAB('B');
            }
        });


        menu_layout = layout.findViewById(R.id.lunch_menu);

        String[] menu ={"A","Agawegawegew","waegewA","gewagA","gewagA","awgegA","B","Agawegwe","Aawegw","gawegA","agweA"};

        this.menu = menu;
        setLunchMenuAB('A');

        isLunchReady = false;
    }





    public void setLunchMenu(String[] strings) {
        for(String s : menu){
            Log.d("ManagerLunch", "setLunchMenu: "+s);
        }
        this.menu = strings;
        isLunchReady = true;
    }

    public void setLunchMenuAB(char AB){
        menu_layout.removeAllViews();
        char locAB = ' ';
        for(int i = 0; i < menu.length ; i++){
            if(menu[i].equals("A")){
                locAB = 'A';
                continue;
            }else if(menu[i].equals("B")){
                locAB = 'B';
                continue;
            }
            if(locAB == AB){
                TextView tv = new TextView(activity);
                tv.setText(menu[i]);
                tv.setTextSize(25);
                tv.setBackgroundColor(ResourcesCompat.getColor(activity.getResources(), R.color.beige, null));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,10,0,0);

                menu_layout.addView(tv,params);
            }

        }
    }

    public void selected(ViewGroup viewGroup){
        viewGroup.removeAllViews();
        if(!isLunchReady){
            SocketStuff.send("2");
        }
        viewGroup.addView(this.layout);
    }

}
