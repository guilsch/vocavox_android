package com.guilsch.multivoc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class AdapterDeck extends BaseAdapter {

    LayoutInflater inflater;

    Deck deck;
    Context context;
    TextView item1;
    TextView item2;
//    TextView pack;
//    Button setStateButton;
    ImageView deleteCard;
    Activity currentActivity;

    public AdapterDeck(Context applicationContext, Deck deck, Activity activity) {
        this.deck = deck;
        this.context = applicationContext;
        this.currentActivity = activity;
        this.inflater = (LayoutInflater.from(applicationContext));
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
            view = inflater.inflate(R.layout.adapter_card_list_view, null);
        }

        item1 = view.findViewById(R.id.adapter_card_list_view_item1_textView);
        item2 = view.findViewById(R.id.item2_textView);
//        pack = view.findViewById(R.id.pack_textView);
//        setStateButton = view.findViewById(R.id.setStateButton);
        deleteCard = view.findViewById(R.id.deleteCardButton);

        Card card = deck.get(i);

        item1.setText(card.getItem1());
        item2.setText(card.getItem2());

        view.setOnClickListener(v -> setEditCardLayout(card.getUuid()));
        deleteCard.setOnClickListener(v -> DialogDeleteCards.showCustomDialog(currentActivity, card.getUuid()));

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
