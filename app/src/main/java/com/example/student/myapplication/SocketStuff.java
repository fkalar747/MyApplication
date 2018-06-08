package com.example.student.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketStuff {

    String ip = "70.12.110.184";
    int port = 6666;

    private static SocketStuff instance = null;
    private Handler handler;
    private PostMsgable activity;

    private Socket client;
    private ClientThread clientThread;
    private volatile boolean isReady;



    public SocketStuff(PostMsgable activity){
        this.activity = activity;
        this.handler = new Handler(Looper.getMainLooper());
    }

    private void send_internal(final String msg){

        int count = 0;
        while(!isReady){
            Log.d("SocketSuff","is ready not true attempting thread sleep");
            //connect(); 하면안될듯
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Log.d("SocketSuff","thread sleep exception");
            }
            if(++count>2){
                activity.postToast("연결 안됨");
                return;
            }
        }
        new Thread() {
                @Override
        public void run() {
                    clientThread.send(msg);
           }
        }.start();

    }

    public void connect(){

        Log.d("clientThread","  !! start1");
        new Thread(){
            public void run() {
                try {

                    Log.d("clientThread","  !! start2");
                    client = new Socket(ip, port);
                    Log.d("clientThread","  !! start3");
                    clientThread = new ClientThread(client, handler,activity);
                    Log.d("clientThread","  !! start4");
                    clientThread.start();
                    isReady = true;
                } catch (UnknownHostException e) {
                    Log.d("unclassified exception","32525");
                } catch (IOException e) {
                    Log.d("unclassified exception","32245");
                }
            }
        }.start();
    }

    private void setPostMsgable(PostMsgable postMsgable){
        activity = postMsgable;
    }

    public static final void prepareConnection(PostMsgable postMsgable){
        if(instance != null){
            instance.setPostMsgable(postMsgable);
        }else {
            Log.d("SocketStuff"," socket instance null instanciating ... ");
            instance = new SocketStuff(postMsgable);
            instance.connect();
        }
    }

    public static final void send(String s){
        instance.send_internal(s);
    }

    public static final String format_login(String id, String pw){
        StringBuilder sb = new StringBuilder();
        sb.append("1,");
        sb.append(id);
        sb.append(",");
        sb.append("2,");
        sb.append(pw);

        return sb.toString();
    }

}
