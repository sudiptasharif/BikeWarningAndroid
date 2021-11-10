package com.example.bikewarningandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {
    private final String TAG = SettingsActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void startWarningsActivity(View view) {
        startActivity(new Intent(this, WarningsActivity.class));
    }

    public void saveSettings(View view) {
        // TODO: add the setting values to shared preferences
    }
}