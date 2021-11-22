package com.example.bikewarningandroid;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class BikeWarningServer implements Runnable{
    private static final String TAG = "BikeWarningServer";
    private BikeProtocol bikeProtocol;
    private Looper mainActivityLooper;
    private Button btnStartServer;

    public BikeWarningServer(Button button) {
        btnStartServer = button;
    }

    public void run() {
        Log.d(TAG, "run: starting server thread");
        try (ServerSocket serverSocket = new ServerSocket(AppUtils.PORT_NUMBER);
             Socket clientSocket = serverSocket.accept();
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));)
        {
            if(!Thread.interrupted()) {
                Log.d(TAG, "run: connected to a client");
                bikeProtocol = new BikeProtocol();
                String input;
                while ((input = in.readLine()) != null) {
                    Log.d(TAG, "run: processing signal");
                    out.println(bikeProtocol.processSignal(input));
                }
                updateServerStartButton();
            } else {
                Log.d(TAG, "run: Thread Interrupted");
                in.close();
                out.close();
                clientSocket.close();
                serverSocket.close();
            }
        } catch (IOException e) {
            Log.d(TAG, "run: IOException = " + e.getMessage());
        }
    }

    public void setLooper(Looper looper) {
        mainActivityLooper = looper;
    }

    private void updateServerStartButton() {
        Log.d(TAG, "run: updating server button");
        Handler handler = new Handler(mainActivityLooper);
        handler.post(() -> {
            MainActivity.switchState = AppUtils.ON;
            btnStartServer.setText(AppUtils.START_LISTENING);
        });
    }
}
