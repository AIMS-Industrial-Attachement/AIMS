package com.example.aims;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

import com.agrawalsuneet.dotsloader.loaders.LazyLoader;

public class SplashScreen extends AppCompatActivity {
    LazyLoader lazyLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        lazyLoader = findViewById(R.id.loader);
        LazyLoader loader = new LazyLoader(this, 30, 20, ContextCompat.getColor(this, R.color.loader_selected),
                ContextCompat.getColor(this, R.color.loader_selected),
                ContextCompat.getColor(this, R.color.loader_selected));
        loader.setAnimDuration(500);
        loader.setFirstDelayDuration(100);
        loader.setSecondDelayDuration(200);
        loader.setInterpolator(new LinearInterpolator());
        lazyLoader.addView(loader);

        Thread thread= new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
                startApp();
            }
        });

        thread.start();
    }
    public void doWork() {
        for (int progress = 50; progress <= 200; progress += 50) {
            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void startApp()
    {
        Intent intent = new Intent(SplashScreen.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
