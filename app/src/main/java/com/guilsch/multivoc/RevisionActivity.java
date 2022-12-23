package com.guilsch.multivoc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class RevisionActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextViewQuestion;
    private Button mSeeAnswerButton;

    private Button mAnswerButton1;
    private Button mAnswerButton2;
    private Button mAnswerButton3;
    private Button mAnswerButton4;

    private Button mBackToMenuRevisionButton;

    private Deck deck;
    private Card card;
    private static Queue<Card> revisionQueue;

    private Random rand;
    private int langDirection;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.deck = new Deck();
        this.deck.init();
        this.deck.filterToTrain();

        if (this.deck.isEmpty()) {
            showEndOfRevision();

        } else {

            this.rand = new Random();
            this.revisionQueue = new LinkedList<>();

            // Adding deck's cards to the queue
            for (Card card : this.deck) {
                this.revisionQueue.add(card);
            }

            NextOrEnd();
        }
    }

    private void setCardParam(int quality) {
        this.card.setState(Param.ACTIVE);
        MemoAlgo.SuperMemo2(this.card, quality);
        this.card.updateDatabase();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void NextOrEnd() {
        card = revisionQueue.poll();

        if (card != null) {

            // Attribute the translation direction
            this.langDirection = (this.rand.nextDouble() > ((double) (Param.LANG_DIRECTION_FREQ)) / 10 ? 1 : -1);

            showQuestionSide();
        }
        else {
            showEndOfRevision();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onClick(View v) {

        if (v == mSeeAnswerButton) {
            showAnswerSide();

        } else if (v == mAnswerButton1){
            revisionQueue.add(card);
            setCardParam(1);
            NextOrEnd();

        } else if (v == mAnswerButton2) {
            setCardParam(2);
            NextOrEnd();

        } else if (v == mAnswerButton3) {
            setCardParam(3);
            NextOrEnd();

        } else if (v == mAnswerButton4) {
            setCardParam(4);
            NextOrEnd();

        } else if (v == mBackToMenuRevisionButton) {
            Intent MenuActivityIntent = new Intent(RevisionActivity.this, MenuActivity.class);
            startActivity(MenuActivityIntent);

        } else {
            throw new IllegalStateException("Unknown clicked view : " + v);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    private void showEndOfRevision() {
        setContentView(R.layout.end_of_revision);

        mBackToMenuRevisionButton = findViewById(R.id.end_of_revision_back_to_menu);
        mBackToMenuRevisionButton.setOnClickListener(this);
    }

    private void showQuestionSide() {
        setContentView(R.layout.question_side);

        mTextViewQuestion = findViewById(R.id.question_side_item1);
        mSeeAnswerButton = findViewById(R.id.question_side_button);
        mSeeAnswerButton.setOnClickListener(this);

        findViewById(R.id.back_arrow).setOnClickListener(view -> onBackPressed());

        if (this.langDirection == 1) {
            mTextViewQuestion.setText(this.card.getItem1());
        }
        else {
            mTextViewQuestion.setText(this.card.getItem2());
        }


    }

    private void showAnswerSide() {
        setContentView(R.layout.answer_side);

        mTextViewQuestion = findViewById(R.id.answer_side_item2);
        mAnswerButton1 = findViewById(R.id.answer_side_button1);
        mAnswerButton2 = findViewById(R.id.answer_side_button2);
        mAnswerButton3 = findViewById(R.id.answer_side_button3);
        mAnswerButton4 = findViewById(R.id.answer_side_button4);

        mAnswerButton1.setOnClickListener(this);
        mAnswerButton2.setOnClickListener(this);
        mAnswerButton3.setOnClickListener(this);
        mAnswerButton4.setOnClickListener(this);

        if (this.langDirection == 1) {
            mTextViewQuestion.setText(this.card.getItem2());
        }
        else {
            mTextViewQuestion.setText(this.card.getItem1());
        }

    }

    @Override
    public void onBackPressed() {
        Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(menuActivity);
        finish();
    }
}