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
        this.activity = postMsgable;
        if(clientThread!=null)clientThread.setActivity(postMsgable);
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

    public static final String formatInt(String bday){
        StringBuilder sb = new StringBuilder();
        int bdayint = Integer.parseInt(bday);
        sb.append("19");
        sb.append(bdayint/10000);
        sb.append("-");
        sb.append(bdayint/100%100);
        sb.append("-");
        sb.append(String.format("%02d", bdayint%100));
        return sb.toString();
    }

    public static final String format_login(String name, String bday){
        StringBuilder sb = new StringBuilder();
        sb.append("1/");
        try {
            sb.append(formatInt(bday));
        }catch (NumberFormatException e){
            Log.d("SocketStuff", "number format exception");
        }
        sb.append("/");
        sb.append(name);
        return sb.toString();
    }

    public static final String format_qr_(String code){
        StringBuilder sb = new StringBuilder();
        sb.append("3/");
        sb.append("asdf1234s");
        //sb.append(code);
        sb.append("/");
        sb.append(formatInt(instance.birth));
        sb.append("/");
        sb.append(instance.name);
        return sb.toString();
    }

    String name;
    String birth;
    public static final void setSession(String name,String birth){
        instance.name = name;
        instance.birth = birth;
    }
    public static final String getSesssionName(){
        return instance.name;
    }
    public static final String getSesssionBirth(){
        return instance.birth;
    }


}
