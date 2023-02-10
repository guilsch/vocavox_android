package com.guilsch.multivoc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class CardsSelectionDeckAdapter extends BaseAdapter {

    LayoutInflater inflater;

    List<Card> cardsList;

    public CardsSelectionDeckAdapter(Context applicationContext, List<Card> cardsList) {
        this.cardsList = cardsList;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return cardsList.size();
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
        item1.setText(cardsList.get(i).getItem1());
        item2.setText(cardsList.get(i).getItem2());
        pack.setText(cardsList.get(i).getPack());

        selectCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectCardButton.isChecked()) {
                    System.out.println(cardsList.get(i).getItem1() + " is checked");
                    if (!LearnActivity.learningQueueContains(cardsList.get(i))) {
                        LearnActivity.addToLearningQueue(cardsList.get(i));
                    }
                }
                else if (!selectCardButton.isChecked()) {
                    System.out.println(cardsList.get(i).getItem1() + " is unchecked");
                    if (LearnActivity.learningQueueContains(cardsList.get(i))) {
                        LearnActivity.removeFromLearningQueue(cardsList.get(i));
                    }
                }
            }
        });

        return view;
    }
}
