package com.example.shopapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.shopapp.Utils.FirebaseUtils;
import com.google.firebase.database.DatabaseReference;

public class SplashActivity extends AppCompatActivity {
    DatabaseReference userRef = FirebaseUtils.getChildRef("user");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();
            }
        }, 2000);
    }

    private void nextActivity() {
        Intent intent = new Intent(getBaseContext(), DangNhapActivity.class);
        startActivity(intent);
    }
}