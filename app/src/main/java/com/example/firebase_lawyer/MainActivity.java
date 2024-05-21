package com.example.firebase_lawyer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);

        Animation imageAnimation = new ScaleAnimation(
                0.5f, 1f,
                0.5f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        imageAnimation.setDuration(1500);
        imageAnimation.setRepeatMode(Animation.REVERSE);
        imageAnimation.setRepeatCount(1);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(imageAnimation);

        imageView.startAnimation(animationSet);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, LogIn.class);
            startActivity(intent);
            finish();
        }, 3500);
    }
}
