package com.guilsch.multivoc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Iterator;

public class LearnActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mStartLearningButton;

    private TextView mTextViewQuestion;
    private Button mSeeAnswerButton;

    private Button mAnswerButton1;
    private Button mAnswerButton2;
    private Button mAnswerButton3;
    private Button mAnswerButton4;

    private Button mBackToMenuRevisionButton;

    ListView simpleList;

    private Deck deck;
    private static Deck learningDeck;
    private Card card;
    private Iterator<Card> cardIterator;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.deck = new Deck();
        this.learningDeck = new Deck();
        this.deck.init();
        this.deck.filterToLearn();

        if (this.deck.isEmpty()) {
            showNoCardsToLearn();
        }
        else {

            showCardsSelection();
            simpleList = (ListView) findViewById(R.id.cardsSelectionListView);
            CardsSelectionDeckAdapter adapter = new CardsSelectionDeckAdapter(getApplicationContext(), deck);
            simpleList.setAdapter(adapter);

            mStartLearningButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cardIterator = learningDeck.iterator();
                    learningDeck.showCards();
                    if (cardIterator.hasNext()) {
                        card = cardIterator.next();
                        showQuestionSide();
                    } else {
                        showEndOfLearning();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {

        if (v == mSeeAnswerButton) {
            showAnswerSide();
        } else if (v == mAnswerButton1){
            NextOrEnd(1);
        } else if (v == mAnswerButton2) {
            NextOrEnd(2);
        } else if (v == mAnswerButton3) {
            NextOrEnd(3);
        } else if (v == mAnswerButton4) {
            NextOrEnd(4);
        } else if (v == mBackToMenuRevisionButton) {
            Intent MenuActivityIntent = new Intent(LearnActivity.this, MenuActivity.class);
            startActivity(MenuActivityIntent);
        } else {
            throw new IllegalStateException("Unknown clicked view : " + v);
        }
    }

    private void NextOrEnd(int quality) {

        this.card.setState(Param.ACTIVE);
        MemoAlgo.SuperMemo2(this.card, quality);
        this.card.updateDatabase(this.card.getUuid());

        if (cardIterator.hasNext()) {
            this.card = cardIterator.next();
            showQuestionSide();
        }
        else {
            this.card = new Card();
            showEndOfLearning();
        }
    }

    private void showCardsSelection() {
        setContentView(R.layout.cards_selection);

        findViewById(R.id.back_arrow).setOnClickListener(view -> onBackPressed());

        mStartLearningButton = findViewById(R.id.cards_selection_start_button);
    }

    private void showEndOfLearning() {
        setContentView(R.layout.end_of_learning);

        findViewById(R.id.back_arrow).setOnClickListener(view -> onBackPressed());

        mBackToMenuRevisionButton = findViewById(R.id.end_of_learning_back_to_menu);
        mBackToMenuRevisionButton.setOnClickListener(this);
    }

    private void showNoCardsToLearn() {
        setContentView(R.layout.no_cards_to_learn);

        findViewById(R.id.back_arrow).setOnClickListener(view -> onBackPressed());

        mBackToMenuRevisionButton = findViewById(R.id.end_of_learning_back_to_menu);
        mBackToMenuRevisionButton.setOnClickListener(this);
    }

    private void showQuestionSide() {
        setContentView(R.layout.question_side);

        findViewById(R.id.back_arrow).setOnClickListener(view -> onBackPressed());

        mTextViewQuestion = findViewById(R.id.question_side_item1);
        mSeeAnswerButton = findViewById(R.id.question_side_button);
        mSeeAnswerButton.setOnClickListener(this);

        mTextViewQuestion.setText(this.card.getItem1());

    }

    private void showAnswerSide() {
        setContentView(R.layout.answer_side);

        findViewById(R.id.back_arrow).setOnClickListener(view -> onBackPressed());

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

    public static void addToLearningDeck(Card card) {
        System.out.println(card.getItem1() + " is added");
        learningDeck.add(card);
    }

    public static void removeFromLearningDeck(Card card) {
        System.out.println(card.getItem1() + " is removed");
        learningDeck.remove(card);
    }

    public static Boolean learningDeckContains(Card card) {
        return learningDeck.contains(card);
    }

    @Override
    public void onBackPressed() {
        Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(menuActivity);
        finish();
    }
}