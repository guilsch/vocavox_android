package com.guilsch.multivoc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class DeckAdapter extends BaseAdapter {

    LayoutInflater inflater;

    Deck deck;
    Context context;
    TextView item1;
    TextView item2;
    TextView pack;
    Button setStateButton;
    Button deleteCard;
    Activity currentActivity;

    public DeckAdapter(Context applicationContext, Deck deck, Activity activity) {
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
            view = inflater.inflate(R.layout.activity_list_view, null);
        }

        item1 = view.findViewById(R.id.item1_textView);
        item2 = view.findViewById(R.id.item2_textView);
        pack = view.findViewById(R.id.pack_textView);
        setStateButton = view.findViewById(R.id.setStateButton);
        deleteCard = view.findViewById(R.id.deleteCardButton);

        Card card = deck.get(i);

        item1.setText(card.getItem1());
        item2.setText(card.getItem2());
        pack.setText(card.getPack());
        setStateButton.setText(utils.getStringStateFromInt(card.getState()));

        setStateButton.setOnClickListener(v -> onStateButtonPressed(i));
        deleteCard.setOnClickListener(v -> onDeleteCardPressed(i));
        item1.setOnClickListener(v -> setEditCardLayout(card.getUuid()));

        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void setEditCardLayout(String uuid) {
        Intent editCardActivity = new Intent(currentActivity.getApplicationContext(), EditCardActivity.class);
        editCardActivity.putExtra("UUID", uuid);
        currentActivity.startActivity(editCardActivity);
    }

    private void onDeleteCardPressed(int i) {
        deck.deleteCard(deck.get(i).getUuid());
    }

    private void onStateButtonPressed(int i) {
        deck.get(i).setState(utils.nextStateForButton(deck.get(i).getState()));
        deck.get(i).updateInDatabase();
        setStateButton.setText(utils.getStringStateFromInt(deck.get(i).getState()));
    }

}
