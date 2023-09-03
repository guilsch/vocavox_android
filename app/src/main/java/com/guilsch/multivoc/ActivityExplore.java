package com.guilsch.multivoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

public class ActivityExplore extends AppCompatActivity {

    ListView dataList;
    TextView noCardText;

    ConstraintLayout backLayout;

    static Deck filteredDeck;
    static AdapterDeck adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        backLayout = findViewById(R.id.back_layout);
        backLayout.setOnClickListener(v -> onBackPressed());

        filteredDeck = (Deck) Param.GLOBAL_DECK.clone();

        dataList = (ListView) findViewById(R.id.deckListView);
        adapter = new AdapterDeck(getApplicationContext(), filteredDeck, this);
        dataList.setAdapter(adapter);

        SearchView searchView = findViewById(R.id.explore_search_view);
        noCardText = findViewById(R.id.no_card_text);
        ifNoResult(filteredDeck);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newKeyWord) {
                filteredDeck = searchInDeck(newKeyWord);
                adapter.notifyDataSetChanged();
                return false;
            }
        });

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