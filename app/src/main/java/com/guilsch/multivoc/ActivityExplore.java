package com.guilsch.multivoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;

public class ActivityExplore extends AppCompatActivity {

    SwipeMenuListView dataList;
    TextView noCardText;
    ImageView sortButton;
    SearchView searchView;
    ConstraintLayout backLayout;

    static Deck filteredDeck;
    static AdapterDeck adapter;

    int sortingCriterion;
    int previousSortingCriterion;
    Boolean sortOrder;
    ComparatorCard cardsComparator;
    ImageView sortArrow;
    RadioButton radioButton1;
    RadioButton radioButton2;
    RadioButton radioButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        // Layout init
        dataList = findViewById(R.id.deckListView);
        backLayout = findViewById(R.id.back_layout);
        searchView = findViewById(R.id.explore_search_view);
        noCardText = findViewById(R.id.no_card_text);
        sortButton = findViewById(R.id.sort_button);

        // Sorting filter dialog
        sortOrder = Boolean.TRUE;
        sortingCriterion = Param.SORT_ALPHABETICALLY_USER;
        previousSortingCriterion = Param.SORT_ALPHABETICALLY_USER;
        cardsComparator = new ComparatorCard(sortingCriterion);

        // Deck management
        filteredDeck = (Deck) Param.GLOBAL_DECK.clone();
        ifNoResult(filteredDeck);
        adapter = new AdapterDeck(getApplicationContext(), filteredDeck, this);
        dataList.setAdapter(adapter);
        sortFilteredDeck();

        // Interactions
        backLayout.setOnClickListener(v -> onBackPressed());

        // Searching Management
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newKeyWord) {
                filteredDeck = searchInDeck(newKeyWord);
                sortFilteredDeck();
                return false;
            }
        });

        // Sort
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialog = DialogSortCards.showCustomDialog(ActivityExplore.this);

                sortArrow = dialog.findViewById(R.id.sort_arrow);
                sortArrow.setRotation(getRotationFromCurrentOrder());

                radioButton1 = dialog.findViewById(R.id.alphabetical_user_radio_button);
                radioButton2 = dialog.findViewById(R.id.creation_date_radio_button);
                radioButton3 = dialog.findViewById(R.id.training_date_radio_button);

                setCheckedRadioButton();

                radioButton1.setOnClickListener(v -> manageClick(Param.SORT_ALPHABETICALLY_USER));
                radioButton2.setOnClickListener(v -> manageClick(Param.SORT_BY_CREATION_DATE));
                radioButton3.setOnClickListener(v -> manageClick(Param.SORT_BY_TRAINING_DATE));
            }});

        //        SwipeMenuCustomCreator creator = new SwipeMenuCustomCreator(getApplicationContext()); // Swipe
//        dataList.setMenuCreator(creator);
//        dataList.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
//        dataList.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

//        dataList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//                switch (index) {
//                    case 0:
//                        System.out.println("Opeeeen");
//                        break;
//                    case 1:
//                        System.out.println("Deleeete");
//                        break;
//                }
//                return false;
//            }
//        });
    }

    private float getRotationFromCurrentOrder() {
        if (sortOrder) {
            return 0.0f;
        }

        return 180.0f;
    }

    ///// Sorting cards management /////
    private void manageClick(int newCriterion) {
        previousSortingCriterion = sortingCriterion;
        sortingCriterion = newCriterion;

        if(sortingCriterion == previousSortingCriterion) {
            // Don't change criterion, invert order
            sortOrder = !sortOrder;
        }
        else {
            // Change criterion, original order
            sortOrder = Boolean.TRUE;
        }

        launchArrowRotation();
        setCheckedRadioButton();
        sortFilteredDeck();
    }

    private void launchArrowRotation() {
        ObjectAnimator rotateAnimator = ObjectAnimator
                .ofFloat(sortArrow, "rotation", sortArrow.getRotation(), getRotationFromCurrentOrder());
        rotateAnimator.setInterpolator(new DecelerateInterpolator());
        rotateAnimator.setDuration(1000);
        rotateAnimator.start();
    }

    private void setCheckedRadioButton() {
        switch (sortingCriterion){
            case Param.SORT_ALPHABETICALLY_USER:
//                radioGroup.check(R.id.alphabetical_user_radio_button);
                radioButton1.setChecked(Boolean.TRUE);
                radioButton2.setChecked(Boolean.FALSE);
                radioButton3.setChecked(Boolean.FALSE);
                break;
            case Param.SORT_BY_CREATION_DATE:
//                radioGroup.check(R.id.creation_date_radio_button);
                radioButton1.setChecked(Boolean.FALSE);
                radioButton2.setChecked(Boolean.TRUE);
                radioButton3.setChecked(Boolean.FALSE);
                break;
            case Param.SORT_BY_TRAINING_DATE:
//                radioGroup.check(R.id.training_date_radio_button);
                radioButton1.setChecked(Boolean.FALSE);
                radioButton2.setChecked(Boolean.FALSE);
                radioButton3.setChecked(Boolean.TRUE);
                break;

        }
    }

    private void sortFilteredDeck() {
        cardsComparator.setSortingLogic(sortingCriterion);
        cardsComparator.setSortingOrder(sortOrder);
        filteredDeck.sort(cardsComparator);
        adapter.notifyDataSetChanged();
    }
    ///// End sorting cards management /////

    ///// Searching cards management /////
    protected Deck searchInDeck(String keyWord) {
        filteredDeck.clear();

        if (keyWord == null || keyWord.length() == 0) {
            filteredDeck.addAll(Param.GLOBAL_DECK);
        } else {
            final String filterPattern = keyWord.toString().toLowerCase().trim();

            for (Card card : Param.GLOBAL_DECK) {
                if (card.getItem1().toLowerCase().contains(filterPattern) || card.getItem2().toLowerCase().contains(filterPattern)) {
                    filteredDeck.add(card);
                }
            }
        }

        ifNoResult(filteredDeck);
        return filteredDeck;
    }

    /***
     * Show text message if list is empty
     * @param filteredDeck
     */
    protected void ifNoResult(Deck filteredDeck) {
        if (filteredDeck.isEmpty()) {
            noCardText.setVisibility(View.VISIBLE);
        }
        else {
            noCardText.setVisibility(View.INVISIBLE);
        }
    }
    ///// End searching cards management /////

    static void deleteCard(String uuid) {
        // Delete from the global deck
        Param.GLOBAL_DECK.deleteCardFromDatafile(uuid);
        Param.GLOBAL_DECK.deleteCardFromDeck(uuid);

        // Delete from the filtered deck (in order to make the card disappear from the list)
        filteredDeck.deleteCardFromDeck(uuid);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        Intent menuActivityIntent = new Intent(getApplicationContext(), ActivityMenu.class);
        menuActivityIntent.putExtra("FRAG_INDEX", 2);
        startActivity(menuActivityIntent);
        finish();
    }
}