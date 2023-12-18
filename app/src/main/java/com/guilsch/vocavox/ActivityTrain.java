package com.guilsch.vocavox;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import life.sabujak.roundedbutton.RoundedButton;
import android.speech.tts.TextToSpeech;
/**
 * This class is the activity corresponding to the training activity
 *
 * @author Guilhem Schena
 */
public class ActivityTrain extends AppCompatActivity implements View.OnClickListener {

    // Layout
    private RoundedButton mSeeAnswerButton;
    private RoundedButton mAnswerButton1;
    private RoundedButton mAnswerButton2;
    private RoundedButton mAnswerButton3;
    private RoundedButton mAnswerButton4;
    private RoundedButton mBackToMenuRevisionButton;

    private TextView mTextViewQuestion;
    private TextView mTextViewItem2;
    private TextView cardsLeftText;

    private ImageView speaker;

    // Progress Bar
    private ProgressBar CardsRemainingPB;

    // Variables
    private Card currentCard;
    private Queue<Card> processedCardsQueue;
    private static Queue<Card> trainingCardsQueue;
    private Random rand;
    private int langDirection;
    private int cardsNBInit;

    // Back Arrow
    private ConstraintLayout backLayout;

    // Debug
    Card memoryCard;

    // Thread
    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    // Text to speech
    TextToSpeech textToSpeech;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize variables
        rand = new Random();
        processedCardsQueue = new LinkedList<>();

        // Initialize text to speech
        textToSpeech = Pronunciator.initPronunciator(this, Param.TARGET_LANGUAGE_ISO);

        // Get cards to train in revision_queue
        trainingCardsQueue = Param.GLOBAL_DECK.getTrainingQueue();
        cardsNBInit = trainingCardsQueue.size();

        // Debug
        if (Param.DEBUG) {
            Utils.writeDebugLine(String.format("Session date : %s", Utils.giveCurrentDate().toString()));
            Utils.writeDebugLine("trainingCardsQueue size : " + String.valueOf(trainingCardsQueue.size()) +
                    " ; processedCardsQueue size : " + String.valueOf(processedCardsQueue.size()));
        }

        // Start scroll or show end screen
        if (trainingCardsQueue.isEmpty()) {
            // No cards to train
            showEndOfRevision();

        } else {
            // Start cards scroll
            NextOrEndForTraining();
        }
    }

    String getDebugLine(Card card, String cardVersion) {
        return Utils.giveCurrentDate().toString() +
                " | " + cardVersion +
                " ; trainingCardsQueue size : " + String.valueOf(trainingCardsQueue.size()) +
                " ; processedCardsQueue size : " + String.valueOf(processedCardsQueue.size()) +
                " ; " +
                card.getInfoText();
    }

    /**
     * Occurs after the user gave an answer. Manage the score given by the user for the current
     * card. Mark the card active and calculate the new training date thanks to SuperMemo algorithm.
     *
     * @param quality 2 to 4, quality of the answer given by the user (not used if quality is 1)
     */
    private void setCardParam(int quality) {
        // If the quality is not 1, card is not repeated so it is considered processed
        if (quality != 1){
            // Change card values
//            currentCard.setState(Param.ACTIVE);
            memoryCard = currentCard;
            MemoAlgo.SuperMemo2(currentCard, quality);

            if (currentCard.getNextPracticeDate().before(Utils.giveCurrentDate())) {
                currentCard.info();
                memoryCard.info();
                Utils.writeDebugLine("!!!Check below!!!");
            }

            if (Param.DEBUG) {
                Utils.writeDebugLine(getDebugLine(memoryCard, "Previous"));
            }

//            currentCard.updateInDatabaseOnSeparateThread();
            Utils.updateInDatabaseOnSeparateThreadMultiShot(singleThreadExecutor, currentCard);
            processedCardsQueue.add(currentCard);

            if (Param.DEBUG) {
                Utils.writeDebugLine(getDebugLine(currentCard, "Updated"));
            }
        }
    }

    /**
     * Check if there is another card in the queue to be trained, show the question side if so.
     * Otherwise, save training data in global deck and in datafile and show end of training screen.
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void NextOrEndForTraining() {
        Utils.printNBCards();
        currentCard = trainingCardsQueue.poll();

        if (currentCard != null) {
            // Attribute the translation direction
            langDirection = (rand.nextDouble() > ((double) (Param.LANG_DIRECTION_FREQ)) / 10 ? 1 : -1);
            showQuestionSide();
        }
        else {
            // Training is over
            Utils.threadShutdown(singleThreadExecutor);
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
            onBackPressed();
        }
        else {
            throw new IllegalStateException("Unknown clicked view : " + v);
        }
    }


    /**
     * Set the end of revision layout when the user is done with the training
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void showEndOfRevision() {
        setContentView(R.layout.activity_training_end_of_training);

        mBackToMenuRevisionButton = findViewById(R.id.skip_btn);
        mBackToMenuRevisionButton.setOnClickListener(v -> onBackPressed());

        Param.GLOBAL_DECK.updateDeckDataVariables();
    }

    /**
     * Set the layout for the question side for the current card
     */
    private void showQuestionSide() {
        setContentView(R.layout.card_question_side);

        backLayout = findViewById(R.id.back_layout);
        backLayout.setOnClickListener(v -> onBackPressed());

        mTextViewQuestion = findViewById(R.id.question_side_item1);
        mSeeAnswerButton = findViewById(R.id.question_side_button);
        cardsLeftText = findViewById(R.id.cards_left);

        updateCardsRemainingProgressBar();
        cardsLeftText.setText(String.valueOf(Param.GLOBAL_DECK.getCardsToReviewNb()));

        mSeeAnswerButton.setOnClickListener(this);

        // Depends on the direction of revision for each card
        if (this.langDirection == 1) {
            mTextViewQuestion.setText(this.currentCard.getItem1());
        }
        else {
            mTextViewQuestion.setText(this.currentCard.getItem2());
        }
    }

    /**
     * Update cards remaining progress bar
     */
    private void updateCardsRemainingProgressBar() {
        CardsRemainingPB = findViewById(R.id.remaining_cards_progress_bar);
        CardsRemainingPB.setMax(cardsNBInit);
        CardsRemainingPB.setProgress(processedCardsQueue.size());
    }

    /**
     * Set the layout for the answer side for the current card
     */
    private void showAnswerSide() {
        setContentView(R.layout.card_answer_side);

        backLayout = findViewById(R.id.back_layout);
        backLayout.setOnClickListener(v -> onBackPressed());

        mAnswerButton1 = findViewById(R.id.answer_side_button1);
        mAnswerButton2 = findViewById(R.id.answer_side_button2);
        mAnswerButton3 = findViewById(R.id.answer_side_button3);
        mAnswerButton4 = findViewById(R.id.answer_side_button4);
        mTextViewQuestion = findViewById(R.id.answer_side_item2);
        mTextViewItem2 = findViewById(R.id.answer_side_item1);
        cardsLeftText = findViewById(R.id.cards_left);
        speaker = findViewById(R.id.speaker_ic);

        cardsLeftText.setText(String.valueOf(Param.GLOBAL_DECK.getCardsToReviewNb()));
        updateCardsRemainingProgressBar();

        mAnswerButton1.setOnClickListener(this);
        mAnswerButton2.setOnClickListener(this);
        mAnswerButton3.setOnClickListener(this);
        mAnswerButton4.setOnClickListener(this);

        // Pronunciation
        if (Param.AUTOMATIC_SPEECH) {
            textToSpeech.speak(currentCard.getItem2(), TextToSpeech.QUEUE_FLUSH, null, null);
        }
        speaker.setOnClickListener(v -> textToSpeech.speak(currentCard.getItem2(),
                TextToSpeech.QUEUE_FLUSH, null, null));

        // Depends on the direction of revision for each card
        if (this.langDirection == 1) {
            mTextViewQuestion.setText(this.currentCard.getItem2());
            mTextViewItem2.setText(this.currentCard.getItem1());
        }
        else {
            mTextViewQuestion.setText(this.currentCard.getItem1());
            mTextViewItem2.setText(this.currentCard.getItem2());
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


    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }


    /**
     * Manage click on the back arrow
     */
    @Override
    public void onBackPressed() {
        // Shutdown thread
        Utils.threadShutdown(singleThreadExecutor);

        // Save changes in deck
        Param.GLOBAL_DECK.updateDeckDataVariables();

        Intent menuActivity = new Intent(getApplicationContext(), ActivityMenu.class);
        startActivity(menuActivity);
        finish();
    }
}