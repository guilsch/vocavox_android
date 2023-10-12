package com.guilsch.multivoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;

public class ActivityExplore extends AppCompatActivity {

    SwipeMenuListView dataList;
    TextView noCardText;
    ImageView filterButton;
    SearchView searchView;
    ConstraintLayout backLayout;

    static Deck filteredDeck;
    static AdapterDeck adapter;

    int sortingCriterion;
    ComparatorCard cardsComparator;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        dataList = findViewById(R.id.deckListView);
        backLayout = findViewById(R.id.back_layout);
        searchView = findViewById(R.id.explore_search_view);
        noCardText = findViewById(R.id.no_card_text);
        filterButton = findViewById(R.id.sort_button);

        backLayout.setOnClickListener(v -> onBackPressed());

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View custo = DialogFilterCards.showCustomDialog(ActivityExplore.this);

                radioGroup = custo.findViewById(R.id.radioGroup);
                defaultCheckRadioButton();

                // Filter cards
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == R.id.alphabetical_user) {
                            sortingCriterion = Param.SORT_ALPHABETICALLY_USER;
                        } else if (checkedId == R.id.creation_date) {
                            sortingCriterion = Param.SORT_BY_CREATION_DATE;
                        } else if (checkedId == R.id.training_date) {
                            sortingCriterion = Param.SORT_BY_TRAINING_DATE;
                        }

                        sortFilteredDeck();
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        // Deck management
        filteredDeck = (Deck) Param.GLOBAL_DECK.clone();
        ifNoResult(filteredDeck);

        // List View
        adapter = new AdapterDeck(getApplicationContext(), filteredDeck, this);
        dataList.setAdapter(adapter);

//        SwipeMenuCustomCreator creator = new SwipeMenuCustomCreator(getApplicationContext()); // Swipe
//        dataList.setMenuCreator(creator);
//        dataList.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
//        dataList.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        // Sorting filter
        sortingCriterion = Param.SORT_ALPHABETICALLY_USER;
        cardsComparator = new ComparatorCard(sortingCriterion);
        sortFilteredDeck();

        // Interaction Management
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newKeyWord) {
                filteredDeck = searchInDeck(newKeyWord);
                sortFilteredDeck();
                adapter.notifyDataSetChanged();
                return false;
            }
        });

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

    private void defaultCheckRadioButton() {
        switch (sortingCriterion){
            case Param.SORT_BY_CREATION_DATE:
                radioGroup.check(R.id.creation_date);
                break;
            case Param.SORT_BY_TRAINING_DATE:
                radioGroup.check(R.id.training_date);
                break;
            case Param.SORT_ALPHABETICALLY_USER:
                radioGroup.check(R.id.alphabetical_user);
                break;
        }
    }

    private void sortFilteredDeck() {
        cardsComparator.setSortingLogic(sortingCriterion);
        filteredDeck.sort(cardsComparator);
    }

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