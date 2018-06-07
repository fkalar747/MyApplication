package com.example.student.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketStuff {

    Socket client;
    String ip = "70.12.110.184";
    int port = 6666;
    Thread thread;
    ClientThread clientThread;
    Handler handler;



    public void connect(){

        handler = new Handler(){

            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                Log.d("socket","message received");

                Bundle bundle = msg.getData();
            }

        };


        thread = new Thread(){
            public void run() {
                super.run();
                try {

                    client = new Socket(ip, port);
                    clientThread = new ClientThread(client, handler);

                    Log.d("clientThread","  !! start");
                    clientThread.start();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {

                    e.printStackTrace();

                }
            }
        };
        thread.start();
    }
}
