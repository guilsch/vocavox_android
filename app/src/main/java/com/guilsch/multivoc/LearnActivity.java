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

    private TextView mTextViewQuestionStep1;
    private Button mNextCardButtonStep1;

    private Button mSeeAnswerButtonStep2;
    private TextView mTextViewQuestionStep2;
    private TextView mTextViewAnswerStep2;
    private Button mAnswerRightButtonStep2;
    private Button mAnswerWrongButtonStep2;

    private Button mStartStep1Button;
    private Button mSkipStep1Button;
    private Button mStartStep2Button;
    private Button mSkipStep2Button;
    private Button mStartStep3Button;

    private Button mSeeAnswerButtonStep3;
    private Button mAnswerButton1Step3;
    private Button mAnswerButton2Step3;
    private Button mAnswerButton3Step3;
    private Button mAnswerButton4Step3;
    private TextView mTextViewQuestionStep3;
    private TextView mTextViewAnswerStep3;

    private Button mBackToMenuRevisionButton;

    ListView cardsSelectionList;

    private Card currentCard;
    private static Queue<Card> learningCardsQueue1;
    private static Queue<Card> learningCardsQueue2;
    private static Queue<Card> learningCardsQueue3;
    private Queue<Card> processedCardsQueue;
    private List<Card> toLearnCardsList;

//    private ProgressBar progressBar;
    private int currentStep;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize variables
        learningCardsQueue1 = new LinkedList<>();
        learningCardsQueue2 = new LinkedList<>();
        learningCardsQueue3 = new LinkedList<>();
        processedCardsQueue = new LinkedList<>();

        currentStep = 1;

        toLearnCardsList = Param.GLOBAL_DECK.getCardsToLearnList();

        // Start scroll or show end screen
        if (toLearnCardsList.isEmpty()) {
            showNoCardsToLearn();

        } else {
            setLayoutCardsSelection();
        }
    }

    private void initStep1() {
//        setContentView(R.layout.learn_step1);

//        progressBar = findViewById(R.id.stepProgressBar);
//        progressBar.setMax(3);
//        progressBar.setProgress(1);

        scrollCardsStep1();

    }

    private void initStep2() {
//        setContentView(R.layout.learn_step2);

//        progressBar = findViewById(R.id.stepProgressBar);
//        progressBar.setMax(3);
//        progressBar.setProgress(2);

        scrollCardsStep2(Boolean.FALSE);
    }

    private void initStep3() {
//        setContentView(R.layout.learn_step3);

//        progressBar = findViewById(R.id.stepProgressBar);
//        progressBar.setMax(3);
//        progressBar.setProgress(3);

        scrollCardsStep3(0);
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

        if (v == mSeeAnswerButtonStep3) {
            showAnswerSideStep3();

        } else if (v == mAnswerButton1Step3){
            setCardParam(1);
            scrollCardsStep3(1);

        } else if (v == mAnswerButton2Step3) {
            setCardParam(2);
            scrollCardsStep3(2);

        } else if (v == mAnswerButton3Step3) {
            setCardParam(3);
            scrollCardsStep3(3);

        } else if (v == mAnswerButton4Step3) {
            setCardParam(4);
            scrollCardsStep3(4);

        } else if (v == mBackToMenuRevisionButton) {
            Intent MenuActivityIntent = new Intent(LearnActivity.this, MenuActivity.class);
            startActivity(MenuActivityIntent);

        } else if (v == mAnswerRightButtonStep2) {
            scrollCardsStep2(Boolean.TRUE);

        } else if (v == mAnswerWrongButtonStep2) {
            scrollCardsStep2(Boolean.FALSE);
        }

        else {
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
        currentCard.updateInDatabaseOnSeparateThread();

        // If the quality is not 1, card is not repeated so it is considered processed
        if (quality != 1){
            processedCardsQueue.add(currentCard);
        }
    }

    private void scrollCardsStep1() {
        // Add current card to the next queue if it has already be shown
        if (currentCard != null) {
            learningCardsQueue2.add(currentCard);
        }

        currentCard = learningCardsQueue1.poll();

        if (currentCard != null) {
            // Show new card
            setLayoutStep1QuestionSide();
        }
        else {
            currentStep++;
//            progressBar.setProgress(currentStep);
//            progressBar.setProgress(currentStep);
            setLayoutTransition1to2();
        }
    }

    private void scrollCardsStep2(Boolean rightAnswer) {
        // Add card to the next queue if answer is correct, add it to the end of the current queue
        // if not
        if (rightAnswer & currentCard != null) {
            learningCardsQueue3.add(currentCard);
        } else if (!rightAnswer & currentCard != null) {
            learningCardsQueue2.add(currentCard);
        }

        // Next card
        currentCard = learningCardsQueue2.poll();

        if (currentCard != null) {
            // Show new card
            setLayoutStep2QuestionSide();
        }
        else {
            currentStep++;
//            progressBar.setProgress(currentStep);
//            progressBar.setProgress(currentStep);
            setLayoutTransition2to3();
        }
    }

    private void scrollCardsStep3(int quality) {
        // Add card to the next queue if answer is correct, add it to the end of the current queue
        // if not
        if (quality != 1 & currentCard != null) {
            processedCardsQueue.add(currentCard);
        } else if (quality == 1 & currentCard != null) {
            learningCardsQueue3.add(currentCard);
        }

        // Next card
        currentCard = learningCardsQueue3.poll();

        if (currentCard != null) {
            // Show new card
            showQuestionSideStep3();
        }
        else {
//            progressBar.setProgress(currentStep);
            showEndOfLearning();
        }
    }

    /**
     * Set the layout to select the cards to learn
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void setLayoutCardsSelection() {
        setContentView(R.layout.cards_selection);

        // Back arrow
        findViewById(R.id.back_arrow).setOnClickListener(view -> onBackPressed());

        // Cards selection
        cardsSelectionList = findViewById(R.id.cardsSelectionListView);
        CardsSelectionDeckAdapter adapter = new CardsSelectionDeckAdapter(getApplicationContext(), toLearnCardsList);
        cardsSelectionList.setAdapter(adapter);

        // Start learning button
        mStartLearningButton = findViewById(R.id.cards_selection_start_button);
        mStartLearningButton.setOnClickListener(v -> setLayoutTransition0to1());
    }

    private void setLayoutStep1QuestionSide() {
        setContentView(R.layout.learn_step1);

        mTextViewQuestionStep1 = findViewById(R.id.question_side_item1_step1);
        mNextCardButtonStep1 = findViewById(R.id.next_card_button_step1);

        mNextCardButtonStep1.setOnClickListener(v -> scrollCardsStep1());
        mTextViewQuestionStep1.setText(this.currentCard.getItem1() + " = " + this.currentCard.getItem2());
    }

    private void setLayoutStep2QuestionSide() {
        setContentView(R.layout.learn_step2_question_side);

        // Initialization
        mTextViewQuestionStep2 = findViewById(R.id.question_side_item1_step2);
        mSeeAnswerButtonStep2 = findViewById(R.id.see_answer_button_step2);

        // Use
        mSeeAnswerButtonStep2.setOnClickListener(v -> setLayoutStep2AnswerSide());
        mTextViewQuestionStep2.setText(this.currentCard.getItem2());
    }

    private void setLayoutStep2AnswerSide() {
        setContentView(R.layout.learn_step2_answer_side);

        // Initialization
        mTextViewAnswerStep2 = findViewById(R.id.answer_side_item2_step2);
        mAnswerRightButtonStep2 = findViewById(R.id.right_answer_button_step2);
        mAnswerWrongButtonStep2 = findViewById(R.id.wrong_answer_button_step2);

        // Use
        mAnswerRightButtonStep2.setOnClickListener(this);
        mAnswerWrongButtonStep2.setOnClickListener(this);
        mTextViewAnswerStep2.setText(this.currentCard.getItem1());
    }

    private void setLayoutTransition0to1() {
        setContentView(R.layout.learn_transition_0_to_1_layout);

        mStartStep1Button = findViewById(R.id.start_step2_button);
        mSkipStep1Button = findViewById(R.id.skip_step2_button);

        mStartStep1Button.setOnClickListener(v -> initStep1());
        mSkipStep1Button.setOnClickListener(v -> {
            learningCardsQueue2.addAll(learningCardsQueue1);
            setLayoutTransition1to2();
        });
    }

    private void setLayoutTransition1to2() {
        setContentView(R.layout.learn_transition_1_to_2_layout);

        mStartStep2Button = findViewById(R.id.start_step2_button);
        mSkipStep2Button = findViewById(R.id.skip_step2_button);

        mStartStep2Button.setOnClickListener(v -> initStep2());
        mSkipStep2Button.setOnClickListener(v -> {
            learningCardsQueue3.addAll(learningCardsQueue2);
            setLayoutTransition2to3();
        });
    }

    private void setLayoutTransition2to3() {
        setContentView(R.layout.learn_transition_2_to_3_layout);

        mStartStep3Button = findViewById(R.id.go_to_step3_button);
        mStartStep3Button.setOnClickListener(v -> initStep3());
    }

    /**
     * Set the end of learning layout when the user is done with the training
     */
    private void showEndOfLearning() {
        setContentView(R.layout.end_of_learning);

        findViewById(R.id.back_arrow).setOnClickListener(view -> onBackPressed());

        mBackToMenuRevisionButton = findViewById(R.id.skip_step2_button);
        mBackToMenuRevisionButton.setOnClickListener(this);
    }

    private void showNoCardsToLearn() {
        setContentView(R.layout.no_cards_to_learn);

        findViewById(R.id.back_arrow).setOnClickListener(view -> onBackPressed());

        mBackToMenuRevisionButton = findViewById(R.id.skip_step2_button);
        mBackToMenuRevisionButton.setOnClickListener(this);
    }

    /**
     * Set the layout for the question side for the current card
     */
    private void showQuestionSideStep3() {
        setContentView(R.layout.question_side);

        findViewById(R.id.back_arrow).setOnClickListener(view -> onBackPressed());

        mTextViewQuestionStep3 = findViewById(R.id.question_side_item1);
        mSeeAnswerButtonStep3 = findViewById(R.id.question_side_button);

        mSeeAnswerButtonStep3.setOnClickListener(this);
        mTextViewQuestionStep3.setText(this.currentCard.getItem1());

    }

    /**
     * Set the layout for the answer side for the current card
     */
    private void showAnswerSideStep3() {
        setContentView(R.layout.answer_side);

        findViewById(R.id.back_arrow).setOnClickListener(view -> onBackPressed());

        mTextViewAnswerStep3 = findViewById(R.id.answer_side_item2_step2);
        mAnswerButton1Step3 = findViewById(R.id.wrong_answer_button_step2);
        mAnswerButton2Step3 = findViewById(R.id.answer_side_button2);
        mAnswerButton3Step3 = findViewById(R.id.answer_side_button3);
        mAnswerButton4Step3 = findViewById(R.id.right_answer_button_step2);

        mAnswerButton1Step3.setOnClickListener(this);
        mAnswerButton2Step3.setOnClickListener(this);
        mAnswerButton3Step3.setOnClickListener(this);
        mAnswerButton4Step3.setOnClickListener(this);

        mTextViewAnswerStep3.setText(this.currentCard.getItem2());

    }

    /**
     * When the card is selected, addLearningQueue(card) is called in the CardsSelectionListAdapter.
     * This method adds the card selected in the learning queue.
     *
     * @param card Card selected to be learned
     */
    public static void addToLearningQueue(Card card) {
        System.out.println(card.getItem1() + " is added");
        learningCardsQueue1.add(card);
    }

    /**
     * When the card is unselected, removeFromLearningQueue(card) is called in the
     * CardsSelectionListAdapter. This method removes the card selected from the learning queue.
     *
     * @param card Card selected to be removed
     */
    public static void removeFromLearningQueue(Card card) {
        System.out.println(card.getItem1() + " is removed");
        learningCardsQueue1.remove(card);
    }

    /**
     * learningQueueContains(card) is called in the CardsSelectionListAdapter to check if a
     * particular card is currently in the learning queue.
     *
     * @param card Card selected to check if it is in the learning queue.
     */
    public static Boolean learningQueueContains(Card card) {
        Boolean contains = learningCardsQueue1.contains(card);
        return contains;
    }

    @Override
    public void onBackPressed() {
        Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(menuActivity);
        finish();
    }
}