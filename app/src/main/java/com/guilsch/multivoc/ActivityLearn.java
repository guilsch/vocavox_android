package com.guilsch.multivoc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import life.sabujak.roundedbutton.RoundedButton;

/**
 * This class is the activity corresponding to the learning activity
 *
 * @author Guilhem Schena
 */
public class ActivityLearn extends AppCompatActivity implements View.OnClickListener {

    private RoundedButton mStartLearningButton;

    private TextView mTextViewQuestionStep1;
    private RoundedButton mNextCardButtonStep1;

    private RoundedButton mSeeAnswerButtonStep2;
    private TextView mTextViewQuestionStep2;
    private TextView mTextViewAnswerStep2;
    private RoundedButton mAnswerRightButtonStep2;
    private RoundedButton mAnswerWrongButtonStep2;

    private Button mStartStep1Button;
    private Button mSkipStep1Button;
    private Button mStartStep2Button;
    private Button mSkipStep2Button;
    private Button mStartStep3Button;

    private RoundedButton mSeeAnswerButtonStep3;
    private RoundedButton mAnswerButton1Step3;
    private RoundedButton mAnswerButton2Step3;
    private RoundedButton mAnswerButton3Step3;
    private RoundedButton mAnswerButton4Step3;
    private TextView mTextViewQuestionStep3;
    private TextView mTextViewAnswerStep3Item1;
    private TextView mTextViewAnswerStep3Item2;

    private RoundedButton mBackToMenuRevisionButton;

    private StateProgressBar mStepsProgressBar;
    private String[] descriptionData;

    ListView cardsSelectionList;

    private Card currentCard;
    private static Queue<Card> learningCardsQueue1;
    private static Queue<Card> learningCardsQueue2;
    private static Queue<Card> learningCardsQueue3;
    private Queue<Card> processedCardsQueue;
    private List<Card> toLearnCardsList;

    // Progress Bar
    private ProgressBar CardsRemainingPB;
    private TextView cardsLeftText;
    private int cardsNBInit;

    // Back Arrow
    private ConstraintLayout backLayout;

    // Thread
    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize variables
        learningCardsQueue1 = new LinkedList<>();
        learningCardsQueue2 = new LinkedList<>();
        learningCardsQueue3 = new LinkedList<>();
        processedCardsQueue = new LinkedList<>();

        descriptionData = new String[]{"Step 1", "Step 2", "Step 3"};

        toLearnCardsList = Param.GLOBAL_DECK.getCardsToLearnList();

        setLayoutCardsSelection();
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
            scrollCardsStep3(1);

        } else if (v == mAnswerButton2Step3) {
            scrollCardsStep3(2);

        } else if (v == mAnswerButton3Step3) {
            scrollCardsStep3(3);

        } else if (v == mAnswerButton4Step3) {
            scrollCardsStep3(4);

        } else if (v == mBackToMenuRevisionButton) {
            onBackPressed();

        } else if (v == mAnswerRightButtonStep2) {
            scrollCardsStep2(Boolean.TRUE);

        } else if (v == mAnswerWrongButtonStep2) {
            scrollCardsStep2(Boolean.FALSE);
        }

        else {
            throw new IllegalStateException("Unknown clicked view : " + v);
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
            setLayoutTransition2to3();
        }
    }

    /**
     * Occurs after the user gave an answer. Manage the score given by the user for the current
     * card. Mark the card active and calculate the new training date thanks to SuperMemo algorithm.
     * Add the current card to the processed cards queue only if the card is not repeated (ie score
     * not 1). Else remains in the learningCardsQueue3 queue.
     *
     * @param quality 1 to 4, quality of the answer given by the user
     */
    private void scrollCardsStep3(int quality) {

        if (currentCard != null) {
            // Calculates next date
            MemoAlgo.SuperMemo2(currentCard, quality);

            // Manage quality
            if (quality != 1) {
                // Change card values
                currentCard.setState(Param.ACTIVE);
                Utils.updateInDatabaseOnSeparateThreadMultiShot(singleThreadExecutor, currentCard);

                // Add to processed queue
                processedCardsQueue.add(currentCard);

            } else {
                learningCardsQueue3.add(currentCard);
            }
        }

        // Next card
        currentCard = learningCardsQueue3.poll();

        if (currentCard != null) {
            // Show new card
            showQuestionSideStep3();
        }
        else {
            Utils.threadShutdown(singleThreadExecutor);
            showEndOfLearning();
        }
    }

    /**
     * Set the layout to select the cards to learn
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void setLayoutCardsSelection() {
        setContentView(R.layout.cards_selection);

        backLayout = findViewById(R.id.back_layout);
        backLayout.setOnClickListener(v -> onBackPressed());

        // Cards selection
        cardsSelectionList = findViewById(R.id.cardsSelectionListView);
        AdapterDeckCardsSelection adapter = new AdapterDeckCardsSelection(getApplicationContext(), toLearnCardsList);
        cardsSelectionList.setAdapter(adapter);

        // Start learning button
        mStartLearningButton = findViewById(R.id.cards_selection_start_button);
        mStartLearningButton.setOnClickListener(v -> setLayoutTransition0to1());

        cardsSelectionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Card selectedCard = (Card) parent.getItemAtPosition(position);

                if (!learningQueueContains(selectedCard)) {
                    ActivityLearn.addToLearningQueue(selectedCard);
                    view.setBackgroundResource(R.color.card_selected);
                }
                else {
                    ActivityLearn.removeFromLearningQueue(selectedCard);
                    view.setBackgroundResource(R.color.white);
                }
            }
        });
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
        mTextViewAnswerStep2 = findViewById(R.id.answer_side_item2);
        mTextViewQuestionStep2 = findViewById(R.id.answer_side_item1);
        mAnswerRightButtonStep2 = findViewById(R.id.right_answer_button_step2);
        mAnswerWrongButtonStep2 = findViewById(R.id.answer_side_button1);

        // Use
        mAnswerRightButtonStep2.setOnClickListener(this);
        mAnswerWrongButtonStep2.setOnClickListener(this);
        mTextViewQuestionStep2.setText(this.currentCard.getItem1());
        mTextViewAnswerStep2.setText(this.currentCard.getItem2());
    }

    private void setLayoutTransition0to1() {
        setContentView(R.layout.learn_transition_0_to_1_layout);

        mStartStep1Button = findViewById(R.id.start_step3_button);
        mSkipStep1Button = findViewById(R.id.back_home_btn);

        mStartStep1Button.setOnClickListener(v -> {
            scrollCardsStep1();
        });
        mSkipStep1Button.setOnClickListener(v -> {
            learningCardsQueue2.addAll(learningCardsQueue1);
            setLayoutTransition1to2();
        });
    }

    private void setLayoutTransition1to2() {
        setContentView(R.layout.learn_transition_1_to_2_layout);
//        currentStep++;

        manageStepsProgressBar();

        mStartStep2Button = findViewById(R.id.start_step3_button);
        mSkipStep2Button = findViewById(R.id.back_home_btn);

        mStartStep2Button.setOnClickListener(v -> {
            scrollCardsStep2(Boolean.FALSE);
        });
        mSkipStep2Button.setOnClickListener(v -> {
            learningCardsQueue3.addAll(learningCardsQueue2);
            setLayoutTransition2to3();
        });
    }

    private void setLayoutTransition2to3() {
        setContentView(R.layout.learn_transition_2_to_3_layout);

        cardsNBInit = learningCardsQueue3.size();
//        manageStepsProgressBar();

        mStartStep3Button = findViewById(R.id.start_step3_button);
        mStartStep3Button.setOnClickListener(v -> {
            scrollCardsStep3(0);
        });
    }

    /**
     * Set the end of learning layout when the user is done with the training
     */
    private void showEndOfLearning() {
        setContentView(R.layout.end_of_learning);

        // Save changes in deck
        Param.GLOBAL_DECK.updateDeckDataVariables();

        mBackToMenuRevisionButton = findViewById(R.id.back_home_btn);
        mBackToMenuRevisionButton.setOnClickListener(this);
    }

    /**
     * Set the layout for the question side for the current card
     */
    private void showQuestionSideStep3() {
        setContentView(R.layout.question_side);

        cardsLeftText = findViewById(R.id.cards_left);
//        cardsLeftText.setVisibility(View.INVISIBLE);

        mStepsProgressBar.findViewById(R.id.remaining_cards_progress_bar);
//        mStepsProgressBar.setVisibility(View.INVISIBLE);

        mTextViewQuestionStep3 = findViewById(R.id.question_side_item1);
        mSeeAnswerButtonStep3 = findViewById(R.id.question_side_button);

        mSeeAnswerButtonStep3.setOnClickListener(this);
        mTextViewQuestionStep3.setText(this.currentCard.getItem1());

        updateCardsRemainingProgressBar();
        cardsLeftText.setText(String.valueOf(cardsNBInit - processedCardsQueue.size()));
    }

    /**
     * Set the layout for the answer side for the current card
     */
    private void showAnswerSideStep3() {
        setContentView(R.layout.answer_side);

        cardsLeftText = findViewById(R.id.cards_left);
        mStepsProgressBar.findViewById(R.id.remaining_cards_progress_bar);

//        cardsLeftText.setVisibility(View.VISIBLE);
//        mStepsProgressBar.setVisibility(View.VISIBLE);

        mTextViewAnswerStep3Item1 = findViewById(R.id.answer_side_item1);
        mTextViewAnswerStep3Item2 = findViewById(R.id.answer_side_item2);

        mAnswerButton1Step3 = findViewById(R.id.answer_side_button1);
        mAnswerButton2Step3 = findViewById(R.id.answer_side_button2);
        mAnswerButton3Step3 = findViewById(R.id.answer_side_button3);
        mAnswerButton4Step3 = findViewById(R.id.answer_side_button4);

        mAnswerButton1Step3.setOnClickListener(this);
        mAnswerButton2Step3.setOnClickListener(this);
        mAnswerButton3Step3.setOnClickListener(this);
        mAnswerButton4Step3.setOnClickListener(this);

        mTextViewAnswerStep3Item1.setText(this.currentCard.getItem1());
        mTextViewAnswerStep3Item2.setText(this.currentCard.getItem2());

        updateCardsRemainingProgressBar();
        cardsLeftText.setText(String.valueOf(cardsNBInit - processedCardsQueue.size()));
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

    /**
     * Init and manage parameters of the progress bar in the transitions layout for each step
     */
    private void manageStepsProgressBar() {
        mStepsProgressBar = (StateProgressBar) findViewById(R.id.stepsProgressBar);
//        mStepsProgressBar.setStateDescriptionData(descriptionData);
        mStepsProgressBar.setAnimationDuration(2000);

//        // init progress bar
//        mStepsProgressBar = findViewById(R.id.stepsProgressBar);
//        mStepsProgressBar.setMax((maxStep - 1)*100);
//
//        // Init animation
//        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(mStepsProgressBar,
//                "progress", (currentStep - 2)*100, (currentStep - 1)*100);
//        progressAnimator.setDuration(1000); // Durée de l'animation en millisecondes
//        progressAnimator.setInterpolator(new DecelerateInterpolator()); // Vitesse de l'animation
//
//        // Animation start
//        progressAnimator.start();
    }

    @Override
    public void onBackPressed() {
        Utils.threadShutdown(singleThreadExecutor);

        Intent menuActivity = new Intent(getApplicationContext(), ActivityMenu.class);
        startActivity(menuActivity);
        finish();
    }
}