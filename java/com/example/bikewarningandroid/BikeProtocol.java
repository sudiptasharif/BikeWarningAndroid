package com.example.bikewarningandroid;


import android.os.Looper;
import android.util.Log;

public class BikeProtocol {
    private static final String TAG = "BikeProtocol";
    private Looper threadLooper;

    public BikeProtocol(Looper looper) {
        threadLooper = looper;
    }

    public String processSignal(String input) {
        Log.d(TAG, "processSignal: input received = " + input);
        String timeInMillis = Long.toString(System.currentTimeMillis());
        return timeInMillis;
    }
}
