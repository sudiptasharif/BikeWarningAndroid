package com.example.bikewarningandroid;


import android.util.Log;

public class BikeProtocol {
    private static final String TAG = "BikeProtocol";

    public String processSignal(String input) {
        Log.d(TAG, "processSignal: input received = " + input);
        String timeInMillis = Long.toString(System.currentTimeMillis());
        return timeInMillis;
    }
}
