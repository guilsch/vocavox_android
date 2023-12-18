package com.guilsch.vocavox;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class DialogQuitLearning {

    public static View showCustomDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View customView = inflater.inflate(R.layout.dialog_layout_quit_learning, null);
        builder.setView(customView);

        TextView deleteButton = customView.findViewById(R.id.dialog_quit_button);
        TextView cancelButton = customView.findViewById(R.id.dialog_cancel_button);

        AlertDialog dialog = builder.create();

        // Manage quit click
        deleteButton.setOnClickListener(v -> onQuitActivityClick(context));

        // Manage cancel click
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        // Manage release buttons colors
        Utils.setTextViewTextColorChangeOnTouch(deleteButton, R.color.black, R.color.red);
        Utils.setTextViewTextColorChangeOnTouch(cancelButton, R.color.black, R.color.grey);

        dialog.getWindow().getDecorView().setBackgroundResource(R.drawable.bg_dialog);

        dialog.show();

        return customView;
    }

    public static void onQuitActivityClick(Context context) {
        Intent menuActivity = new Intent(context, ActivityMenu.class);
        context.startActivity(menuActivity);
        ((Activity) context).finish();
    }
}

