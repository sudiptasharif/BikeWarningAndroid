package com.example.bikewarningandroid;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import pl.droidsonroids.gif.GifImageView;

public class BikeWarningServer implements Runnable{
    private static final String TAG = "BikeWarningServer";
    private Button btnStartServer;
    private GifImageView gifImageViewWarning;
    private Context mainActivityContext;
    private Looper mainActivityLooper;
    private MediaPlayer player;
    private volatile long t3;

    public BikeWarningServer(Context context,Button button, GifImageView gif) {
        mainActivityContext = context;
        btnStartServer = button;
        gifImageViewWarning = gif;
    }

    public void run() {
        Log.d(TAG, "run: starting server thread");
        try (ServerSocket serverSocket = new ServerSocket(AppUtils.PORT_NUMBER);
             Socket clientSocket = serverSocket.accept();
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));)
        {
            if(!Thread.interrupted()) {
                Log.d(TAG, "run: accepted client connection request");
                out.println(AppUtils.SERVER_ON);
                Log.d(TAG, "run: sent SERVER_ON state");
                Log.d(TAG, "run: ready to accept warning signal codes");
                String input;
                while ((input = in.readLine()) != null) {
                    Log.d(TAG, "run: signal code = " + input);
                    out.println(processSignal(Integer.parseInt(input)));
                }
                updateServerStartButton();
            } else {
                Log.d(TAG, "run: Thread Interrupted");
                out.println(AppUtils.SERVER_OFF);
                Log.d(TAG, "run: sent SERVER_OFF state.");
                Log.d(TAG, "run: can't accept client connection request");
                Log.d(TAG, "run: restart server to accept any client connection request");
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

    private String processSignal(int signalCode) {
        long t3 = 0L;
        if(signalCode == AppUtils.RED_CIRCLE_TONE) {
            t3 = startWarning(R.drawable.red_circle, R.raw.tone);
            SystemClock.sleep(AppUtils.WARNING_SIGNAL_DURATION);
            stopWarning();
        } else if(signalCode == AppUtils.RED_CIRCLE_SPEECH) {
            t3 = startWarning(R.drawable.red_circle, R.raw.speech);
            SystemClock.sleep(AppUtils.WARNING_SIGNAL_DURATION);
            stopWarning();
        } else if(signalCode == AppUtils.ORANGE_CIRCLE_TONE) {
            t3 = startWarning(R.drawable.orange_circle, R.raw.tone);
            SystemClock.sleep(AppUtils.WARNING_SIGNAL_DURATION);
            stopWarning();
        } else if (signalCode == AppUtils.ORANGE_CIRCLE_SPEECH) {
            t3 = startWarning(R.drawable.orange_circle, R.raw.speech);
            SystemClock.sleep(AppUtils.WARNING_SIGNAL_DURATION);
            stopWarning();
        } else {
            Log.d(TAG, "processSignal: warning code not supported yet");
        }
        return Long.toString(t3);
    }

    private long startWarning(int gifResId, int mediaResId) {
        Log.d(TAG, "startWarning: gifResId = " + gifResId + ", mediaResId = " + mediaResId);
        Handler handler = new Handler(mainActivityLooper);
        handler.post(() -> {
            btnStartServer.setVisibility(View.INVISIBLE);
            gifImageViewWarning.setImageResource(gifResId);
            gifImageViewWarning.setVisibility(View.VISIBLE);
            if(player == null) {
                player = MediaPlayer.create(mainActivityContext, mediaResId);
            }
            player.start();
        });
        return System.currentTimeMillis();
    }

    private void stopWarning() {
        Log.d(TAG, "stopWarning: stop warning");
        Handler handler = new Handler(mainActivityLooper);
        handler.post(() -> {
            gifImageViewWarning.setImageResource(R.drawable.no_warning);
            gifImageViewWarning.setVisibility(View.INVISIBLE);
            btnStartServer.setVisibility(View.VISIBLE);
            if(player != null) {
                player.release();
                player = null;
            }
        });
    }
}
