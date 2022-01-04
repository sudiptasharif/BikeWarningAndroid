package com.example.bikewarningandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Thread serverThread;
    private BikeWarningServer bikeWarningServer;
    public static volatile int switchState;
    private Button btnServerStart;
    private GifImageView gifImageViewWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Log.d(TAG, "onCreate: Port Number = " + AppUtils.PORT_NUMBER);
        switchState = AppUtils.ON;
        btnServerStart = findViewById(R.id.button_start_server);
        gifImageViewWarning = findViewById(R.id.gif_image_view_warning);
        bikeWarningServer = new BikeWarningServer(this, btnServerStart, gifImageViewWarning);
        bikeWarningServer.setLooper(Looper.getMainLooper());
        //logServerThreadState();
    }

    public void startServer(View view) {
        if(switchState == AppUtils.ON) {
            //Log.d(TAG, "startServer: starting new thread");
            serverThread = new Thread(bikeWarningServer);
            serverThread.start();
            btnServerStart.setText(getString(R.string.button_label_stop_listening));
            switchState = AppUtils.OFF;
        } else {
            //Log.d(TAG, "startServer: interrupting/stopping thread");
            serverThread.interrupt();
            btnServerStart.setText(getString(R.string.button_label_start_listening));
            switchState = AppUtils.ON;
        }
        //logServerThreadState();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.d(TAG, "onStart: invoked");
//        logServerThreadState();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.d(TAG, "onPause: invoked");
//        logServerThreadState();
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Log.d(TAG, "onRestart: invoked");
//        logServerThreadState();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.d(TAG, "onResume: invoked");
//        logServerThreadState();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.d(TAG, "onStop: invoked");
//        logServerThreadState();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Log.d(TAG, "onDestroy: invoked");
        if(serverThread != null && serverThread.isAlive()) {
            Log.d(TAG, "onDestroy: interrupting thread");
            serverThread.interrupt();
            switchState = AppUtils.ON;
        }
        //logServerThreadState();
    }

//    private void logServerThreadState() {
//        if(serverThread == null) {
//            Log.d(TAG, "logServerThreadState: serverThread = null");
//        }else {
//            Log.d(TAG, "logServerThreadState: serverThread != null");
//            if(serverThread.isAlive()) {
//                Log.d(TAG, "logServerThreadState: serverThread = alive");
//            }else {
//                Log.d(TAG, "logServerThreadState: serverThread != alive");
//            }
//
//            if(serverThread.isInterrupted()) {
//                Log.d(TAG, "logServerThreadState: serverThread = interrupted");
//            } else {
//                Log.d(TAG, "logServerThreadState: serverThread != interrupted");
//            }
//
//        }
//    }
}