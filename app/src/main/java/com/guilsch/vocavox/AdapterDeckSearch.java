package com.guilsch.vocavox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterDeckSearch extends BaseAdapter {

    LayoutInflater inflater;

    Deck deck;
    Context context;
    TextView item1;
    TextView item2;
    Activity currentActivity;

    public AdapterDeckSearch(Context applicationContext, Deck deck, Activity activity) {
        this.deck = deck;
        this.context = applicationContext;
        this.currentActivity = activity;
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

        // Check if it is possible to reuse a view
        if (view == null) {
            view = inflater.inflate(R.layout.search_deck_adapter_view, null);
        }

        item1 = view.findViewById(R.id.adapter_card_list_view_item1_textView);
        item2 = view.findViewById(R.id.item2_textView);

        Card card = deck.get(i);

        item1.setText(card.getItem1());
        item2.setText(card.getItem2());

        item1.setOnClickListener(v -> setEditCardLayout(card.getUuid()));

        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void setEditCardLayout(String uuid) {
        Intent editCardActivity = new Intent(currentActivity.getApplicationContext(), ActivityEditCard.class);
        editCardActivity.putExtra("UUID", uuid);
        currentActivity.startActivity(editCardActivity);
    }
}
