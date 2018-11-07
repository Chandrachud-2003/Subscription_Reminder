package com.example.rohitandchandra.Due;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import spencerstudios.com.bungeelib.Bungee;

public class splashScreen extends AppCompatActivity {
    private ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 1000ms

                mImageView = findViewById(R.id.splash);

                YoYo.with(Techniques.Pulse)
                        .duration(800)
                        .repeat(2)
                        .playOn(mImageView);
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        YoYo.with(Techniques.ZoomOut)
                                .duration(800)
                                .playOn(mImageView);
                        Handler handler2 = new Handler();
                        handler2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(splashScreen.this, MainActivity.class);
                                startActivity(intent);
                                splashScreen.this.finish();
                                Bungee.zoom(splashScreen.this);

                            }
                        },350);


                    }
                }, 2200);

            }
        }, 1000);




}
    }
