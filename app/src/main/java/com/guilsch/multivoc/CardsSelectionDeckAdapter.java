package com.guilsch.multivoc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

public class CardsSelectionDeckAdapter extends BaseAdapter {

    LayoutInflater inflater;

    Deck deck;

    public CardsSelectionDeckAdapter(Context applicationContext, Deck deck) {
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
        view = inflater.inflate(R.layout.cards_selection_list_view, null);
        TextView item1 = view.findViewById(R.id.item1_textView);
        TextView item2 = view.findViewById(R.id.item2_textView);
        TextView pack = view.findViewById(R.id.pack_textView);
        ToggleButton selectCardButton = view.findViewById(R.id.selectCardButton);
        item1.setText(deck.get(i).getItem1());
        item2.setText(deck.get(i).getItem2());
        pack.setText(deck.get(i).getPack());

        selectCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectCardButton.isChecked()) {
                    System.out.println(deck.get(i).getItem1() + " is checked");
                    if (!LearnActivity.learningDeckContains(deck.get(i))) {
                        LearnActivity.addToLearningDeck(deck.get(i));
                    }
                }
                else if (!selectCardButton.isChecked()) {
                    System.out.println(deck.get(i).getItem1() + " is unchecked");
                    if (LearnActivity.learningDeckContains(deck.get(i))) {
                        LearnActivity.removeFromLearningDeck(deck.get(i));
                    }
                }
            }
        });

        return view;
    }
}
