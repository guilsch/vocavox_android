package com.guilsch.multivoc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import life.sabujak.roundedbutton.RoundedButton;

public class ActivityIntro extends AppCompatActivity {

    private ImageView nextArrow;
    private ImageView previousArrow;
    private RoundedButton startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSlide1();

    }

    private void setSlide1() {
        setContentView(R.layout.activity_intro_slide1);

        nextArrow = findViewById(R.id.arrow_next);
        nextArrow.setOnClickListener(v -> setSlide2());
    }

    private void setSlide2() {
        setContentView(R.layout.activity_intro_slide2);

        nextArrow = findViewById(R.id.arrow_next);
        previousArrow = findViewById(R.id.arrow_previous);

        nextArrow.setOnClickListener(v -> setSlide3());
        previousArrow.setOnClickListener(v -> setSlide1());
    }

    private void setSlide3() {
        setContentView(R.layout.activity_intro_slide3);

        nextArrow = findViewById(R.id.arrow_next);
        previousArrow = findViewById(R.id.arrow_previous);

        nextArrow.setOnClickListener(v -> setSlide4());
        previousArrow.setOnClickListener(v -> setSlide2());
    }

    private void setSlide4() {
        setContentView(R.layout.activity_intro_slide4);

        startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(v -> changeActivity(ActivityMenu.class));
    }

    /**
     * Called when pressing somewhere to change activity
     */
    public void changeActivity(Class newActivityClass) {
        Param.FIRST_LAUNCH = false;
        Pref.savePreference(this, Param.FIRST_LAUNCH_KEY, false);

        Intent newActivity = new Intent(this, newActivityClass);
        startActivity(newActivity);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent activityIntent = new Intent(getApplicationContext(), ActivityMain.class);
        startActivity(activityIntent);
        finish();
    }
}
