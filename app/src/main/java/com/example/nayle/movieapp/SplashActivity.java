package com.example.nayle.movieapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Nayle on 12/10/2015.
 */
public class SplashActivity extends Activity {
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
               startActivity(mainIntent);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                // Toast.makeText(SplashActivity.this, " SPLASH " , Toast.LENGTH_LONG).show();
                SplashActivity.this.finish();

            }
        }, SPLASH_DISPLAY_LENGTH);

    }

}
