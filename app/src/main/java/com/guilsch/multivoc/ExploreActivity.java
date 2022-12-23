package com.guilsch.multivoc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;

public class ExploreActivity extends AppCompatActivity {

    ListView simpleList;

    static Deck originalDeck;
    static Deck filteredDeck;
    static DeckAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        findViewById(R.id.back_arrow).setOnClickListener(view -> onBackPressed());

        originalDeck = new Deck();
        originalDeck.init();
        filteredDeck = (Deck) originalDeck.clone();

        simpleList = (ListView) findViewById(R.id.deckListView);
        adapter = new DeckAdapter(getApplicationContext(), filteredDeck, this);
        simpleList.setAdapter(adapter);

        SearchView searchView = findViewById(R.id.explore_search_view);

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
            filteredDeck.addAll(originalDeck);
        } else {
            final String filterPattern = keyWord.toString().toLowerCase().trim();

            for (Card card : originalDeck) {
                if (card.getItem1().toLowerCase().contains(filterPattern) || card.getItem2().toLowerCase().contains(filterPattern)) {
                    filteredDeck.add(card);
                }
            }
        }

        return filteredDeck;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        System.out.println("Bonj");
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(menuActivity);
        finish();
    }
}