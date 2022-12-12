package com.guilsch.multivoc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class ExploreActivity extends AppCompatActivity {

    ListView simpleList;

    static Deck deck;
    DeckAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        findViewById(R.id.back_arrow).setOnClickListener(view -> onBackPressed());

        deck = new Deck();
        deck.init();

        deck.showCards();

        simpleList = (ListView) findViewById(R.id.deckListView);
        adapter = new DeckAdapter(getApplicationContext(), deck, this);
        simpleList.setAdapter(adapter);

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