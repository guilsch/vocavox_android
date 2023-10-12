package com.guilsch.multivoc;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class DialogFilterCards {

    public static View showCustomDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View customView = inflater.inflate(R.layout.filter_cards_dialog_layout, null);
        builder.setView(customView);

        AlertDialog dialog = builder.create();

        dialog.getWindow().getDecorView().setBackgroundResource(R.drawable.dialog_background);

        dialog.show();

        return customView;
    }
}

