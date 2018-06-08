package com.example.student.myapplication;

import java.io.BufferedReader;

import java.io.BufferedWriter;

import java.io.IOException;

import java.io.InputStreamReader;

import java.io.OutputStreamWriter;

import java.net.Socket;



import android.os.Bundle;

import android.os.Handler;

import android.os.Looper;
import android.os.Message;

import android.util.Log;
import android.widget.TextView;



public class ClientThread extends Thread{





    BufferedReader bufferR;

    BufferedWriter bufferW;

    Socket client;

    Handler uihandler;
    PostMsgable activity;

    public ClientThread(Socket client, Handler handler,PostMsgable activity) {

        this.uihandler = handler;
        this.activity = activity;

        try {

            this.client = client;

            //연결된 소켓으로부터 대화를 나눌 스트림을 얻음

            bufferR = new BufferedReader(new InputStreamReader(client.getInputStream()));

            bufferW = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));



        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    //보내기

    public void send(String data){



        try {


            Log.d("ClientThread","Sending data");
            bufferW.write(data+"\n");

            bufferW.flush();

        } catch (IOException e) {

            // TODO Auto-generated catch block


            Log.d("ClientThread","exception point 7395");
            e.printStackTrace();

        }

    }

    //받기

    public String listen(){

        String msg=null;

        try {

            while(true){

                msg=bufferR.readLine();

                Message m = new Message();

                Bundle bundle = new Bundle();

                bundle.putString("msg", msg);

                m.setData(bundle);

                uihandler.post(new RunnableMsg(activity,msg));

            }



        } catch (IOException e) {

            // TODO Auto-generated catch block

            Log.d("ClientThread_", "listen_exception point 19401");
            e.printStackTrace();

        }

        return msg;

    }






    @Override
    public void run() {

        super.run();

        listen();



    }

    class RunnableMsg implements Runnable{
        PostMsgable activity;
        String msg;
        RunnableMsg(PostMsgable activity,String msg){
            this.activity = activity;
            this.msg = msg;
        }
        @Override
        public void run() {
            activity.postMsg(msg);
        }
    }

}


