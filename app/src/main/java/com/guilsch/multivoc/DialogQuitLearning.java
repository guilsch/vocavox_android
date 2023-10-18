package com.guilsch.multivoc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

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
//        deleteButton.setOnClickListener(v -> ((Activity) context).finish());

        // Manage cancel click
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        // Manage release buttons colors
        Utils.setTextViewTextColorChangeOnTouch(deleteButton, R.color.black, R.color.red);
        Utils.setTextViewTextColorChangeOnTouch(cancelButton, R.color.black, R.color.grey);

        dialog.getWindow().getDecorView().setBackgroundResource(R.drawable.dialog_background);

        dialog.show();

        return customView;
    }
}
