package com.example.bikewarningandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: IP Address = " + AppUtils.getIP(this));
        Log.d(TAG, "onCreate: Port Number = " + AppUtils.PORT_NUMBER);
    }

    public void startSettingsActivity(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }
}