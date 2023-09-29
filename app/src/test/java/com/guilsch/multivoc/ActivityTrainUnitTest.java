//package com.guilsch.multivoc;
//
//import androidx.test.ext.junit.runners;
////import androidx.test.rule.ActivityTestRule;
//
//import com.guilsch.multivoc.ActivityTrain;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.JUnit4;
//
//import static org.junit.Assert.assertTrue;
//
//@RunWith(JUnit4.class)
//public class ActivityTrainTest {
//
//    @Rule
//    public ActivityTestRule<ActivityTrain> activityRule = new ActivityTestRule<>(ActivityTrain.class);
//
//    private ActivityTrain activity;
//
//    @Before
//    public void setUp() {
//        activity = activityRule.getActivity();
//    }
//
//    @Test
//    public void testCardPracticeDate() {
//        // Suppose que la première carte dans la file d'attente de formation est la carte actuelle
//        Card currentCard = activity.getCurrentCard();
//
//        // Vérifie si la condition est vraie (date de pratique suivante antérieure à la date actuelle)
//        boolean condition = currentCard.getNextPracticeDate().before(Utils.giveCurrentDate());
//
//        assertTrue("La condition doit être vraie", condition);
//    }
//
//    // Vous pouvez ajouter d'autres tests pour d'autres cartes ici
//}
