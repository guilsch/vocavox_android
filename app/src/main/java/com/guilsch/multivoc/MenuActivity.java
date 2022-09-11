package com.guilsch.multivoc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MenuActivity extends AppCompatActivity {

    private ImageView revision;
    private ImageView settings;
    private ImageView learning;
    private ImageView explore;
    private ImageView newCard;
    private ImageView flag;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        revision = (ImageView) findViewById(R.id.Revision);
        settings = (ImageView) findViewById(R.id.Settings);
        learning = (ImageView) findViewById(R.id.Learning);
        explore = (ImageView) findViewById(R.id.Explore);
        newCard = (ImageView) findViewById(R.id.New_Card);
        flag = (ImageView) findViewById(R.id.flag);

        // Prepare Excel file
        utils.prepareDataFile();

        switch (Param.getLanguage()) {
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
                throw new IllegalStateException("Unexpected value: " + Param.getLanguage());
        }

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