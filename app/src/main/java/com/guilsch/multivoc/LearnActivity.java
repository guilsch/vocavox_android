package com.guilsch.multivoc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * This class is the activity corresponding to the learning activity
 *
 * @author Guilhem Schena
 */
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

//    private Deck deck;
    private Card currentCard;
    private static Queue<Card> learningCardsQueue;
    private Queue<Card> processedCardsQueue;
    private List<Card> toLearnCardsList;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize variables
        processedCardsQueue = new LinkedList<>();
        learningCardsQueue = new LinkedList<>();

        toLearnCardsList = Param.GLOBAL_DECK.getCardsToLearnList();

        // Start scroll or show end screen
        if (toLearnCardsList.isEmpty()) {
            showNoCardsToLearn();

        } else {
            showCardsSelection();
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
            // Makes the card appear again
            learningCardsQueue.add(this.currentCard);
            setCardParam(1);
            NextOrEndForLearning();

        } else if (v == mAnswerButton2) {
            setCardParam(2);
            NextOrEndForLearning();

        } else if (v == mAnswerButton3) {
            setCardParam(3);
            NextOrEndForLearning();

        } else if (v == mAnswerButton4) {
            setCardParam(4);
            NextOrEndForLearning();

        } else if (v == mBackToMenuRevisionButton) {
            Intent MenuActivityIntent = new Intent(LearnActivity.this, MenuActivity.class);
            startActivity(MenuActivityIntent);

        } else {
            throw new IllegalStateException("Unknown clicked view : " + v);
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

        // If the quality is not 1, card is not repeated so it is considered processed
        if (quality != 1){
            processedCardsQueue.add(currentCard);
        }
    }

    /**
     * Check if there is another card in the queue to be learned, show the question side if so.
     * Otherwise, save training data in global deck and in datafile and show end of training screen.
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void NextOrEndForLearning() {
        currentCard = learningCardsQueue.poll();

        if (currentCard != null) {
            // Show new card
            showQuestionSide();
        }
        else {
            // Learning is over : save data
            showSavingLayout();
        }
    }

    /**
     * Set the saving layout when the user is done with the training, before end of revision layout
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void showSavingLayout() {
        setContentView(R.layout.saving_layout);

        ProgressBar progressBar = findViewById(R.id.saving_progress_bar);
        progressBar.setProgress(0);

        Param.GLOBAL_DECK.updateDeckAndDatabaseFromQueue(processedCardsQueue, progressBar);
        showEndOfLearning();
    }

    /**
     * Set the layout to select the cards to learn
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void showCardsSelection() {
        setContentView(R.layout.cards_selection);

        // Back arrow
        findViewById(R.id.back_arrow).setOnClickListener(view -> onBackPressed());

        // Cards selection
        simpleList = findViewById(R.id.cardsSelectionListView);
        CardsSelectionDeckAdapter adapter = new CardsSelectionDeckAdapter(getApplicationContext(), toLearnCardsList);
        simpleList.setAdapter(adapter);

        // Start learning button
        mStartLearningButton = findViewById(R.id.cards_selection_start_button);
        mStartLearningButton.setOnClickListener(v -> NextOrEndForLearning());
    }

    /**
     * Set the end of learning layout when the user is done with the training
     */
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

    /**
     * Set the layout for the question side for the current card
     */
    private void showQuestionSide() {
        setContentView(R.layout.question_side);

        findViewById(R.id.back_arrow).setOnClickListener(view -> onBackPressed());

        mTextViewQuestion = findViewById(R.id.question_side_item1);
        mSeeAnswerButton = findViewById(R.id.question_side_button);
        mSeeAnswerButton.setOnClickListener(this);

        mTextViewQuestion.setText(this.currentCard.getItem1());

    }

    /**
     * Set the layout for the answer side for the current card
     */
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

        mTextViewQuestion.setText(this.currentCard.getItem2());

    }

    /**
     * When the card is selected, addLearningQueue(card) is called in the CardsSelectionListAdapter.
     * This method adds the card selected in the learning queue.
     *
     * @param card Card selected to be learned
     */
    public static void addToLearningQueue(Card card) {
        System.out.println(card.getItem1() + " is added");
        learningCardsQueue.add(card);
    }

    /**
     * When the card is unselected, removeFromLearningQueue(card) is called in the
     * CardsSelectionListAdapter. This method removes the card selected from the learning queue.
     *
     * @param card Card selected to be removed
     */
    public static void removeFromLearningQueue(Card card) {
        System.out.println(card.getItem1() + " is removed");
        learningCardsQueue.remove(card);
    }

    /**
     * learningQueueContains(card) is called in the CardsSelectionListAdapter to check if a
     * particular card is currently in the learning queue.
     *
     * @param card Card selected to check if it is in the learning queue.
     */
    public static Boolean learningQueueContains(Card card) {
        Boolean contains = learningCardsQueue.contains(card);
        return contains;
    }

    @Override
    public void onBackPressed() {
        Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(menuActivity);
        finish();
    }
}