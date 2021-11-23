package com.example.bikewarningandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class WarningsActivity extends AppCompatActivity {
    private static final String TAG = "WarningsActivity";
    private BikeWarningServer bikeWarningServer;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BikeProtocol bikeProtocol;
    private PrintWriter out;
    private BufferedReader in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warnings);
        //bikeWarningServer = new BikeWarningServer();
    }

    public void startListening(View view) {
        Log.d(TAG, "startListening: Button clicked.");
        if (bikeWarningServer != null) {
            //bikeWarningServer.startListening();
            // runOnUiThread(bikeWarningServer);
            Thread thread = new Thread(bikeWarningServer);
            thread.start();
//            while (true) {
//                thread.start();
//            }
            if(thread.isAlive()) {
                Log.d(TAG, "startListening: Thread is alive and listening.");
            }
        }
//        Handler mainHandler = new Handler(Looper.getMainLooper());
//        Runnable myRunnable = new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Log.d(TAG, "run: Running thread.");
//                    serverSocket = new ServerSocket(AppUtils.PORT_NUMBER);
//                    bikeProtocol = new BikeProtocol();
//                    clientSocket = serverSocket.accept();
//                    out = new PrintWriter(clientSocket.getOutputStream(), true);
//                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//                    String input;
//                    while ((input = in.readLine()) != null) {
//                        out.println(bikeProtocol.processSignal(input));
//                    }
//                    if (input == null) {
//                        Log.d(TAG, "run: No input received.");
//                    }
//                } catch (IOException e) {
//                    Log.d(TAG, "run: IOException = " + e.getMessage());
//                }
//            }
//        };
//        mainHandler.post(myRunnable);
    }

//    public void stopListening(View view) {
//        if (bikeWarningServer != null) {
//            bikeWarningServer.stopListening();
//        }
//    }
}