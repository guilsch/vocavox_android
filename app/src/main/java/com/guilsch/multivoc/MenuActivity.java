package com.guilsch.multivoc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.IOException;

public class MenuActivity extends AppCompatActivity {

    private ImageView flag;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        flag = findViewById(R.id.flag);

        findViewById(R.id.Revision).setOnClickListener(view -> changeActivity(RevisionActivity.class));
        findViewById(R.id.Settings).setOnClickListener(view -> changeActivity(SettingsActivity.class));
        findViewById(R.id.Learning).setOnClickListener(view -> changeActivity(LearnActivity.class));
        findViewById(R.id.Explore).setOnClickListener(view -> changeActivity(ExploreActivity.class));
        findViewById(R.id.New_Card).setOnClickListener(view -> changeActivity(NewCardActivity.class));
        findViewById(R.id.Translation).setOnClickListener(view -> changeActivity(TranslationActivity.class));
        flag.setOnClickListener((view -> changeActivity(MainActivity.class)));


        // Prepare Excel file
        utils.prepareDataFile();

        setLanguageVisuals();

    }

    public void setLanguageVisuals() {
        switch (Param.TARGET_LANGUAGE) {
            case "English" :
                flag.setImageDrawable(getResources().getDrawable(R.drawable.ic_gb));
                break;

            case "German" :
                flag.setImageDrawable(getResources().getDrawable(R.drawable.ic_de));
                break;

            case "Italian" :
                flag.setImageDrawable(getResources().getDrawable(R.drawable.ic_it));
                break;

            case "Russian" :
                flag.setImageDrawable(getResources().getDrawable(R.drawable.ic_ru));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + Param.TARGET_LANGUAGE);
        }
    }

    public void changeActivity(Class newActivityClass) {
        Intent newActivity = new Intent(getApplicationContext(), newActivityClass);
        startActivity(newActivity);
        finish();
    }

}