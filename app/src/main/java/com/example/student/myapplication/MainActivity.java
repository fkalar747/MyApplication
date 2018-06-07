package com.example.student.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    EditText name, birthday;
    Button btn1, btn2;
    String loginname, loginbirthday;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = (EditText)findViewById(R.id.inputname);
        birthday = (EditText)findViewById(R.id.inputbirthday);
        btn1 = (Button)findViewById(R.id.btnDone);
        btn2 = (Button)findViewById(R.id.btnCancel);
        SharedPreferences prefs = getSharedPreferences("PrefName", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        loginname = prefs.getString("inputname",null);
        loginbirthday = prefs.getString("inputbirthday",null);

        if(loginname !=null && loginbirthday != null) {
            if(loginname.equals("name") && loginbirthday.equals("birth")) {
                Toast.makeText(MainActivity.this, name.getText().toString()+"님 환영합니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
                finish();
            }
        }

        else if(loginname == null && loginbirthday == null){
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (name.getText().toString().equals("name") && birthday.getText().toString().equals("birth")) {
                        SharedPreferences prefs = getSharedPreferences("PrefName", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("inputname", name.getText().toString());
                        editor.putString("inputbirthday", birthday.getText().toString());
                        editor.commit();
                        Toast.makeText(MainActivity.this, name.getText().toString()+"님 환영합니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });

        }
    }
}


