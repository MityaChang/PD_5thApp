package com.example.dailybudget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash extends AppCompatActivity {

    TextView tvSplashTitle, tvSplashQuotes;
    ImageView imgPiggy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tvSplashTitle = findViewById(R.id.tvSplashtitle);
        tvSplashQuotes = findViewById(R.id.tvSplashQuotes);
        imgPiggy = findViewById(R.id.piggyLogo);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash);
        tvSplashQuotes.startAnimation(animation);
        tvSplashTitle.startAnimation(animation);
        imgPiggy.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        }, 4000);

    }
}