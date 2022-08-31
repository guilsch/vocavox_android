package com.guilsch.multivoc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QuestionActivity extends AppCompatActivity {

    public static final String BUNDLE_EXTRA_ITEM1 = "BUNDLE_EXTRA_ITEM1";

    private TextView mTextViewQuestion;
    private Button mSeeAnswerButton;

    private String item1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("QuestionActivity", "onCreate() called");

        setContentView(R.layout.activity_question);

        mTextViewQuestion = findViewById(R.id.question_activity_question_textview);
        mSeeAnswerButton = findViewById(R.id.question_activity_see_answer_button);

        Bundle bundle = getIntent().getExtras();
        item1 = bundle.getString(BUNDLE_EXTRA_ITEM1);

        mTextViewQuestion.setText(item1);

        mSeeAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                RevisionActivity.finishedQuestion = Boolean.TRUE;
                finish();
            }
        });
    }
}