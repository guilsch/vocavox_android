package com.guilsch.multivoc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AnswerActivity extends AppCompatActivity {

    public static final String BUNDLE_EXTRA_ITEM2 = "BUNDLE_EXTRA_ITEM2";

    private Button mAnswerButton1;
//    private Button mAnswerButton2;
//    private Button mAnswerButton3;
//    private Button mAnswerButton4;

    private TextView mTextViewAnswer;

    private String item2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        mAnswerButton1 = findViewById((R.id.answer_activity_button1));
        mTextViewAnswer = findViewById(R.id.answer_activity_answer_textview);

        Bundle bundle = getIntent().getExtras();
        item2 = bundle.getString(BUNDLE_EXTRA_ITEM2);

        mTextViewAnswer.setText(item2);

        mAnswerButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}