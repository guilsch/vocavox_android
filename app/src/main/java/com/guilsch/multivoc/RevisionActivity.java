package com.guilsch.multivoc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class RevisionActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextViewQuestion;
    private Button mSeeAnswerButton;

    private Button mAnswerButton1;
    private Button mAnswerButton2;
    private Button mAnswerButton3;
    private Button mAnswerButton4;

    private Button mBackToMenuRevisionButton;

    public static Boolean finishedQuestion;
    public static Boolean finishedAnswer;

    private Card card;
    private Iterator<Card> cardIterator;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d("RevisionActivity", "onCreate() called");

//        finishedQuestion = Boolean.TRUE;
//        finishedAnswer = Boolean.TRUE;

        // Manually create a deck of 2 cards
        Card card1 = new Card();
        Card card2 = new Card();
        card2.setItem1("maison");
        card2.setItem2("casa");
        Deck deck = new Deck();
        deck.add(card1);
        deck.add(card2);
        //

        this.cardIterator = deck.iterator();
        this.card = cardIterator.next();
        showQuestionSide();

    }

    @Override
    public void onClick(View v) {
        int index;

        if (v == mSeeAnswerButton) {
            System.out.println("yes");
            showAnswerSide();
        } else if (v == mAnswerButton1){
            index = 0;
            NextOrEnd();
        } else if (v == mAnswerButton2) {
            index = 1;
            NextOrEnd();
        } else if (v == mAnswerButton3) {
            index = 2;
            NextOrEnd();
        } else if (v == mAnswerButton4) {
            index = 3;
            NextOrEnd();
        } else if (v == mBackToMenuRevisionButton) {
            Intent MenuActivityIntent = new Intent(RevisionActivity.this, MenuActivity.class);
            startActivity(MenuActivityIntent);
        } else {
            throw new IllegalStateException("Unknown clicked view : " + v);
        }
    }

    private void NextOrEnd() {
        if (cardIterator.hasNext()) {
            this.card = cardIterator.next();
            showQuestionSide();
        }
        else {
            this.card = new Card();
            showEndOfRevision();
        }
    }

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

        mTextViewQuestion.setText(this.card.getItem1());

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

        mTextViewQuestion.setText(this.card.getItem2());

    }
}