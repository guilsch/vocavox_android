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

    private LinearLayout revision;
    private LinearLayout settings;
    private LinearLayout learning;
    private LinearLayout explore;
    private LinearLayout newCard;
    private ImageView flag;
    private LinearLayout drive;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        revision = findViewById(R.id.Revision);
        settings = findViewById(R.id.Settings);
        learning = findViewById(R.id.Learning);
        explore = findViewById(R.id.Explore);
        newCard = findViewById(R.id.New_Card);
        flag = findViewById(R.id.flag);

        drive = findViewById(R.id.Drive);

        // Prepare Excel file
        utils.prepareDataFile();

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

        drive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newCardActivity = new Intent(getApplicationContext(), DriveActivity.class);
                startActivity(newCardActivity);
                finish();
            }
        });

        newCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newCardActivity = new Intent(getApplicationContext(), NewCardActivity.class);
                startActivity(newCardActivity);
                finish();
            }
        });

        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent exploreActivity = new Intent(getApplicationContext(), ExploreActivity.class);
                startActivity(exploreActivity);
                finish();
            }
        });

        revision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent revisionActivity = new Intent(getApplicationContext(), RevisionActivity.class);
                startActivity(revisionActivity);
                finish();
            }
        });

        learning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent learnActivity = new Intent(getApplicationContext(), LearnActivity.class);
                startActivity(learnActivity);
                finish();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsActivity = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsActivity);
                finish();
            }
        });

        flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent languageActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(languageActivity);
                finish();
            }
        });
    }
}