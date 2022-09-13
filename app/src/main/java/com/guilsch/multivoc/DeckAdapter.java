package com.guilsch.multivoc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class DeckAdapter extends BaseAdapter {

    LayoutInflater inflater;

    Deck deck;

    public DeckAdapter(Context applicationContext, Deck deck) {
        this.deck = deck;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return deck.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_list_view, null);
        TextView item1 = view.findViewById(R.id.item1_textView);
        TextView item2 = view.findViewById(R.id.item2_textView);
        TextView pack = view.findViewById(R.id.pack_textView);
        Button setStateButton = view.findViewById(R.id.setStateButton);
        item1.setText(deck.get(i).getItem1());
        item2.setText(deck.get(i).getItem2());
        pack.setText(deck.get(i).getPack());
        setStateButton.setText(utils.getStringState(deck.get(i).getState()));

        setStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deck.get(i).setState(utils.nextStateForButton(deck.get(i).getState()));
                deck.get(i).updateDatabase(deck.get(i).getItem1());
                setStateButton.setText(utils.getStringState(deck.get(i).getState()));
            }
        });

        return view;
    }
}
