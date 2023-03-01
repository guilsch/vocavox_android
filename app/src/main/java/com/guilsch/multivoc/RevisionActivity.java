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

/**
 * This class is the activity corresponding to the revision activity
 *
 * @author Guilhem Schena
 */
public class RevisionActivity extends AppCompatActivity implements View.OnClickListener {

    // Layout
    private Button mSeeAnswerButton;
    private Button mAnswerButton1;
    private Button mAnswerButton2;
    private Button mAnswerButton3;
    private Button mAnswerButton4;
    private Button mBackToMenuRevisionButton;

    private TextView mTextViewQuestion;
    private TextView mAnswerHeader;
    private TextView mQuestionHeader;

    // Variables
    private Card currentCard;
    private Queue<Card> processedCardsQueue;
    private static Queue<Card> trainingCardsQueue;
    private Random rand;
    private int langDirection;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize variables
        rand = new Random();
        processedCardsQueue = new LinkedList<>();

        // Get cards to train in revision_queue
        trainingCardsQueue = Param.GLOBAL_DECK.getTrainingQueue();

        // Start scroll or show end screen
        if (trainingCardsQueue.isEmpty()) {
            // No cards to train
            showEndOfRevision();

        } else {
            // Start cards scroll
            NextOrEndForTraining();
        }
    }

    /**
     * Occurs after the user gave an answer. Manage the score given by the user for the current
     * card. Mark the card active and calculate the new training date thanks to SuperMemo algorithm.
     * Add the current card to the processed cards queue only if the card is not repeated (ie score
     * not 1).
     *
     * @param quality 1 to 4, quality of the answer given by the user
     */
    private void setCardParam(int quality) {
        // Change card values
        currentCard.setState(Param.ACTIVE);
        MemoAlgo.SuperMemo2(currentCard, quality);
        currentCard.updateInDatabaseOnSeparateThread();

        // If the quality is not 1, card is not repeated so it is considered processed
        if (quality != 1){
            processedCardsQueue.add(currentCard);
        }
    }

    /**
     * Check if there is another card in the queue to be trained, show the question side if so.
     * Otherwise, save training data in global deck and in datafile and show end of training screen.
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void NextOrEndForTraining() {
        currentCard = trainingCardsQueue.poll();

        if (currentCard != null) {
            // Attribute the translation direction
            langDirection = (rand.nextDouble() > ((double) (Param.LANG_DIRECTION_FREQ)) / 10 ? 1 : -1);
            showQuestionSide();
        }
        else {
            // Training is over
            showEndOfRevision();
        }
    }

    /**
     * Manage the clicks on the seeAnswerButton and the 4 answerButtons, as well as the
     * backToMenuButton
     *
     * @param v view pressed by the user
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onClick(View v) {

        if (v == mSeeAnswerButton) {
            showAnswerSide();

        } else if (v == mAnswerButton1){
            trainingCardsQueue.add(currentCard);
            setCardParam(1);
            NextOrEndForTraining();

        } else if (v == mAnswerButton2) {
            setCardParam(2);
            NextOrEndForTraining();

        } else if (v == mAnswerButton3) {
            setCardParam(3);
            NextOrEndForTraining();

        } else if (v == mAnswerButton4) {
            setCardParam(4);
            NextOrEndForTraining();

        } else if (v == mBackToMenuRevisionButton) {
            Intent MenuActivityIntent = new Intent(RevisionActivity.this, MenuActivity.class);
            startActivity(MenuActivityIntent);

        } else {
            throw new IllegalStateException("Unknown clicked view : " + v);
        }
    }


    /**
     * Set the end of revision layout when the user is done with the training
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void showEndOfRevision() {
        setContentView(R.layout.end_of_revision);

        mBackToMenuRevisionButton = findViewById(R.id.end_of_revision_back_to_menu);
        mBackToMenuRevisionButton.setOnClickListener(this);

        Param.GLOBAL_DECK.updateDeckDataVariables();
    }

    /**
     * Set the layout for the question side for the current card
     */
    private void showQuestionSide() {
        setContentView(R.layout.question_side);

        mTextViewQuestion = findViewById(R.id.question_side_item1);
        mSeeAnswerButton = findViewById(R.id.question_side_button);
        mQuestionHeader = findViewById(R.id.header);

        mQuestionHeader.setText(getRevisionHeaderText());
        mSeeAnswerButton.setOnClickListener(this);
        findViewById(R.id.back_arrow).setOnClickListener(view -> onBackPressed());

        // Depends on the direction of revision for each card
        if (this.langDirection == 1) {
            mTextViewQuestion.setText(this.currentCard.getItem1());
        }
        else {
            mTextViewQuestion.setText(this.currentCard.getItem2());
        }
    }

    /**
     * Set the layout for the answer side for the current card
     */
    private void showAnswerSide() {
        setContentView(R.layout.answer_side);

        mAnswerButton1 = findViewById(R.id.wrong_answer_button_step2);
        mAnswerButton2 = findViewById(R.id.answer_side_button2);
        mAnswerButton3 = findViewById(R.id.answer_side_button3);
        mAnswerButton4 = findViewById(R.id.right_answer_button_step2);
        mTextViewQuestion = findViewById(R.id.answer_side_item2_step2);
        mAnswerHeader = findViewById(R.id.header);

        mAnswerButton1.setOnClickListener(this);
        mAnswerButton2.setOnClickListener(this);
        mAnswerButton3.setOnClickListener(this);
        mAnswerButton4.setOnClickListener(this);
        mAnswerHeader.setText(getRevisionHeaderText());
        findViewById(R.id.back_arrow).setOnClickListener(view -> onBackPressed());

        // Depends on the direction of revision for each card
        if (this.langDirection == 1) {
            mTextViewQuestion.setText(this.currentCard.getItem2());
        }
        else {
            mTextViewQuestion.setText(this.currentCard.getItem1());
        }

    }

//    /**
//     * Set the saving layout when the user is done with the training, before end of revision layout
//     */
//    @RequiresApi(api = Build.VERSION_CODES.R)
//    private void showSavingLayout() {
//        setContentView(R.layout.saving_layout);
//
//        ProgressBar progressBar = findViewById(R.id.saving_progress_bar);
//        progressBar.setProgress(0);
//
////        Param.GLOBAL_DECK.updateDeckAndDatabaseFromQueue(processedCardsQueue, progressBar);
//        showEndOfRevision();
//    }

    /**
     * Give the header of the activity with the remaining cards to train
     *
     * @return String for the header
     */
    private String getRevisionHeaderText() {
        // Returns the header text with the number of cards to train left
        return getResources().getString(R.string.revision_header) + " - " + (trainingCardsQueue.size() + 1);
    }

    /**
     * Manage click on the back arrow
     */
    @Override
    public void onBackPressed() {
        Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(menuActivity);
        finish();
    }
}