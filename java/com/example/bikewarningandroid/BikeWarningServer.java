package com.example.bikewarningandroid;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class BikeWarningServer implements Runnable{
    private static final String TAG = "BikeWarningServer";
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BikeProtocol bikeProtocol;
    private PrintWriter out;
    private BufferedReader in;

    public void run() {
        try {
            Log.d(TAG, "run: Running thread.");
            serverSocket = new ServerSocket(AppUtils.PORT_NUMBER);
            bikeProtocol = new BikeProtocol();
            Log.d(TAG, "run: input = No input received --> Before accept()");
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String input;
            Log.d(TAG, "run: input = No input received --> After accept()");
            while ((input = in.readLine()) != null) {
                out.println(bikeProtocol.processSignal(input));
            }
        } catch (IOException e) {
            Log.d(TAG, "run: IOException = " + e.getMessage());
        }
    }

    public void startListening() {
        Log.d(TAG, "startListening: Starting to listen for client connection."); 
        new Thread(this).start();
        //runOnUiThread
    }

    public void stopListening() {
        Log.d(TAG, "stopListening: stopping to listen.");
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            Log.d(TAG, "stopListening: IOException = " + e.getMessage());
        }

    }
}
