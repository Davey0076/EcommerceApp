package com.example.myecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingScreen extends AppCompatActivity {

    private ImageView loadingImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);

        loadingImage = findViewById(R.id.loadingImage);

        // Blinking animation
        Animation blinkAnimation = new AlphaAnimation(0.0f, 1.0f);
        blinkAnimation.setDuration(1000); // You can adjust the blink speed here
        blinkAnimation.setRepeatMode(Animation.REVERSE);
        blinkAnimation.setRepeatCount(Animation.INFINITE);
        loadingImage.startAnimation(blinkAnimation);

        // Delay of 4 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Stop animation
                loadingImage.clearAnimation();
                // Start Login Activity
                Intent intent = new Intent(LoadingScreen.this, Login.class);
                startActivity(intent);
                finish();
            }
        }, 4000); // 4000 milliseconds = 4 seconds
    }
}
