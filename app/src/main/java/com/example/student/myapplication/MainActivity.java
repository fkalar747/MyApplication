package com.example.student.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements PostMsgable{

    EditText name, birthday;
    Button btn1, btn2;
    String loginname, loginbirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        VersionProps.init();
        SocketStuff.prepareConnection(this);

        name = (EditText)findViewById(R.id.inputname);
        birthday = (EditText)findViewById(R.id.inputbirthday);
        btn1 = (Button)findViewById(R.id.btnDone);
        btn2 = (Button)findViewById(R.id.btnCancel);
        SharedPreferences prefs = getSharedPreferences("PrefName", AppCompatActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        loginname = prefs.getString("inputname",null);
        loginbirthday = prefs.getString("inputbirthday",null);


        if(loginname !=null && loginbirthday != null&&!loginname.equals("")&&!loginbirthday.equals("")) {
            Log.d("MainActivity"," shared ref vals are not null");
            SocketStuff.send(SocketStuff.format_login(loginname,loginbirthday));
        }else{
            Log.d("MainActivity"," shared ref vals are null");
        }

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity","button clicked");
                loginname = name.getText().toString();
                loginbirthday = birthday.getText().toString();
                SocketStuff.send(SocketStuff.format_login(loginname,loginbirthday));
            }
        });

        goToMain2();

    }

    public void goToMain2(){
        Toast.makeText(MainActivity.this, name.getText().toString()+"님 환영합니다.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void postMsg(String msg) {
        switch (msg.charAt(0)){
            case ('1'):
                SharedPreferences prefs = getSharedPreferences("PrefName", AppCompatActivity.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("inputname", loginname);
                editor.putString("inputbirthday", loginbirthday);
                SocketStuff.setSession(loginname,loginbirthday);
                editor.commit();
                goToMain2();
                break;
        }
    }

    @Override
    public void postToast(String s){
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}


