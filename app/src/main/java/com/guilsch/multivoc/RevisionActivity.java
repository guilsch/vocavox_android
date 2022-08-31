package com.guilsch.multivoc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.Iterator;

public class RevisionActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_QUESTION_ACTIVITY = 42;
    public static final String BUNDLE_EXTRA_ITEM1 = "BUNDLE_EXTRA_ITEM1";

    private static final int REQUEST_CODE_ANSWER_ACTIVITY = 52;
    public static final String BUNDLE_EXTRA_ITEM2 = "BUNDLE_EXTRA_ITEM2";

    public static Boolean finishedQuestion;
    public static Boolean finishedAnswer;

    private Card card;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("RevisionActivity", "onCreate() called");

        finishedQuestion = Boolean.TRUE;
        finishedAnswer = Boolean.TRUE;

        setContentView(R.layout.activity_revision);

        // Manually create a deck of 2 cards
        Card card1 = new Card();
        Card card2 = new Card();
        card2.setItem1("maison");
        card2.setItem2("casa");
        Deck deck = new Deck();
        deck.add(card1);
        deck.add(card2);
        //

        deck.showCards();

        Iterator<Card> cardIterator = deck.iterator();

        // Iterate through the deck
        while(cardIterator.hasNext() & finishedQuestion & finishedAnswer) {

            finishedQuestion = Boolean.FALSE;
            finishedAnswer = Boolean.FALSE;

            card = cardIterator.next();

            sendToQuestionActivity(card);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("RevisionActivity", "onActivityResult() called");
        switch(requestCode) {
            case REQUEST_CODE_QUESTION_ACTIVITY:
                sendToAnswerActivity(card);
                break;
            case REQUEST_CODE_ANSWER_ACTIVITY:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendToQuestionActivity(Card card) {
        Log.d("RevisionActivity", "sendToQuestionActivity() called");
        Intent intent = new Intent(RevisionActivity.this, QuestionActivity.class);
        intent.putExtra(BUNDLE_EXTRA_ITEM1, card.getItem1());
        startActivityForResult(intent, REQUEST_CODE_QUESTION_ACTIVITY);
    }

    private void sendToAnswerActivity(Card card) {
        Log.d("RevisionActivity", "sendToAnswerActivity() called");
        Intent intent = new Intent(RevisionActivity.this, AnswerActivity.class);
        intent.putExtra(BUNDLE_EXTRA_ITEM2, card.getItem2());
        startActivityForResult(intent, REQUEST_CODE_ANSWER_ACTIVITY);
    }
}