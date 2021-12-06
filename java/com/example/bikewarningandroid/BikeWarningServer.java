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
    private Looper mainActivityLooper;
    private Button btnStartServer;
    private GifImageView gifImageViewWarning;
    private Context mainActivityContext;
    private MediaPlayenr player;

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
                Log.d(TAG, "run: connected to a client");
                String input;
                while ((input = in.readLine()) != null) {
                    Log.d(TAG, "run: signal code = " + input);
                    out.println(processSignal(Integer.parseInt(input)));
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

    private String processSignal(int signalCode) {
        String t3 = "";
        if(signalCode == AppUtils.RED_CIRCLE_TONE) {
            t3 = launchRedCircleToneWarning();
            SystemClock.sleep(AppUtils.WARNING_SIGNAL_DURATION);
            stopWarning();
        } else if(signalCode == AppUtils.RED_CIRCLE_SPEECH) {
            t3 = launchRedCircleSpeechWarning();
            SystemClock.sleep(AppUtils.WARNING_SIGNAL_DURATION);
            stopWarning();
        } else if(signalCode == AppUtils.ORANGE_CIRCLE_TONE) {
            t3 = launchOrangeCircleToneWarning();
            SystemClock.sleep(AppUtils.WARNING_SIGNAL_DURATION);
            stopWarning();
        } else if (signalCode == AppUtils.ORANGE_CIRCLE_SPEECH) {
            t3 = launchOrangeCircleSpeechWarning();
            SystemClock.sleep(AppUtils.WARNING_SIGNAL_DURATION);
            stopWarning();
        } else {
            Log.d(TAG, "processSignal: warning code not supported yet");
        }
        return t3;
    }

    private String launchRedCircleToneWarning() {
        Log.d(TAG, "launchRedCircleToneWarning: start warning");
        Handler handler = new Handler(mainActivityLooper);
        handler.post(new Runnable() {
            @Override
            public void run() {
                btnStartServer.setVisibility(View.INVISIBLE);
                gifImageViewWarning.setImageResource(R.drawable.red_circle);
                gifImageViewWarning.setVisibility(View.VISIBLE);
                if(player == null) {
                    player = MediaPlayer.create(mainActivityContext, R.raw.tone);
                }
                player.start();
            }
        });
        return Long.toString(System.currentTimeMillis());
    }

    private void stopWarning() {
        Log.d(TAG, "stopWarning: stop warning");
        Handler handler = new Handler(mainActivityLooper);
        handler.post(new Runnable() {
            @Override
            public void run() {
                gifImageViewWarning.setImageResource(R.drawable.red_circle_img);
                gifImageViewWarning.setVisibility(View.INVISIBLE);
                btnStartServer.setVisibility(View.VISIBLE);
                if(player != null) {
                    player.release();
                    player = null;
                }
            }
        });
    }


    private String launchRedCircleSpeechWarning() {
        Log.d(TAG, "launchRedCircleSpeechWarning: start warning");
        Handler handler = new Handler(mainActivityLooper);
        handler.post(new Runnable() {
            @Override
            public void run() {
                btnStartServer.setVisibility(View.INVISIBLE);
                gifImageViewWarning.setImageResource(R.drawable.red_circle);
                gifImageViewWarning.setVisibility(View.VISIBLE);
                if(player == null) {
                    player = MediaPlayer.create(mainActivityContext, R.raw.speech);
                }
                player.start();
            }
        });
        return Long.toString(System.currentTimeMillis());
    }

    private String launchOrangeCircleToneWarning() {
        Log.d(TAG, "launchOrangeCircleToneWarning: start warning");
        Handler handler = new Handler(mainActivityLooper);
        handler.post(new Runnable() {
            @Override
            public void run() {
                btnStartServer.setVisibility(View.INVISIBLE);
                gifImageViewWarning.setImageResource(R.drawable.orange_circle);
                gifImageViewWarning.setVisibility(View.VISIBLE);
                if(player == null) {
                    player = MediaPlayer.create(mainActivityContext, R.raw.tone);
                }
                player.start();
            }
        });
        return Long.toString(System.currentTimeMillis());
    }

    private String launchOrangeCircleSpeechWarning() {
        Log.d(TAG, "launchOrangeCircleSpeechWarning: start warning");
        Handler handler = new Handler(mainActivityLooper);
        handler.post(new Runnable() {
            @Override
            public void run() {
                btnStartServer.setVisibility(View.INVISIBLE);
                gifImageViewWarning.setImageResource(R.drawable.orange_circle);
                gifImageViewWarning.setVisibility(View.VISIBLE);
                if(player == null) {
                    player = MediaPlayer.create(mainActivityContext, R.raw.speech);
                }
                player.start();
            }
        });
        return Long.toString(System.currentTimeMillis());
    }
}
