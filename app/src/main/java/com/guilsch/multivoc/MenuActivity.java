package com.guilsch.multivoc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    private ImageView flag;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        flag = findViewById(R.id.flag);

        findViewById(R.id.menu_train).setOnClickListener(view -> changeActivity(RevisionActivity.class));
        findViewById(R.id.menu_settings).setOnClickListener(view -> changeActivity(SettingsActivity.class));
        findViewById(R.id.menu_learning).setOnClickListener(view -> changeActivity(LearnActivity.class));
        findViewById(R.id.menu_explore).setOnClickListener(view -> changeActivity(ExploreActivity.class));
        findViewById(R.id.menu_new_card).setOnClickListener(view -> changeActivity(NewCardActivity.class));
        findViewById(R.id.menu_translation).setOnClickListener(view -> changeActivity(TranslationActivity.class));
        flag.setOnClickListener((view -> changeActivity(MainActivity.class)));

        TextView mTextViewCardsToReview = findViewById(R.id.cards_to_review);
        TextView mTextViewTotalCards = findViewById(R.id.total_cards_nb);
        mTextViewCardsToReview.setText(String.valueOf(Param.CARDS_TO_REVIEW_NB));
        mTextViewTotalCards.setText(String.valueOf(Param.CARDS_NB));

        // Prepare Excel file
        utils.prepareDataFile();

        flag.setImageDrawable(Param.FLAG_ICON_TARGET);

    }

    public void changeActivity(Class newActivityClass) {
        Intent newActivity = new Intent(getApplicationContext(), newActivityClass);
        startActivity(newActivity);
        finish();
    }

}