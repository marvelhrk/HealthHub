package com.example.healthhub.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.example.healthhub.LocalStorage;
import com.example.healthhub.R;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 900;
    Timer timer;
    ImageView image;
    LocalStorage localStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(() -> {
            localStorage = new LocalStorage(getApplicationContext());


//            Intent homeintent = new Intent(MainActivity.this, Welcome.class);
//            startActivity(homeintent);
//            finish();

            if (localStorage.isUserLoggedIn()) {
                startActivity(new Intent(getApplicationContext(), MainScreen.class));
                finish();
            }
            else{
                Intent homeintent = new Intent(MainActivity.this, Welcome.class);
                startActivity(homeintent);
                finish();
            }
        },SPLASH_TIME_OUT);

        image = findViewById(R.id.imageView);
    }
//    @Override
//    protected void onStop() {
//        timer.cancel();
//        super.onStop();
//    }
//
//    @Override
//    protected void onPause() {
//        timer.cancel();
//        super.onPause();
//    }

}