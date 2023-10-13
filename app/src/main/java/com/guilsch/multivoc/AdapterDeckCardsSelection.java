package com.guilsch.multivoc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

public class AdapterDeckCardsSelection extends BaseAdapter implements ListAdapter {

    LayoutInflater inflater;
    List<Card> cardsList;

    public AdapterDeckCardsSelection(Context applicationContext, List<Card> cardsList) {
        this.cardsList = cardsList;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return cardsList.size();
    }

    @Override
    public Object getItem(int position) {
        return cardsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflater.inflate(R.layout.cards_selection_list_view, null);

        TextView item1 = view.findViewById(R.id.adapter_card_list_view_item1_textView);
        TextView item2 = view.findViewById(R.id.item2_textView);

        item1.setText(cardsList.get(i).getItem1());
        item2.setText(cardsList.get(i).getItem2());

        return view;
    }
}
