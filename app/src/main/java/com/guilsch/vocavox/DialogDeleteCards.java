package com.guilsch.vocavox;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class DialogDeleteCards {

    public static <DialogClosedListener> View showCustomDialog(Context context, String uuid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View customView = inflater.inflate(R.layout.dialog_layout_delete_card, null);
        builder.setView(customView);

        TextView deleteButton = customView.findViewById(R.id.dialog_quit_button);
        TextView cancelButton = customView.findViewById(R.id.dialog_cancel_button);

        AlertDialog dialog = builder.create();

        // Manage delete click
        deleteButton.setOnClickListener(v -> onDeleteCardConfirmationClick(context, dialog, uuid, deleteButton));

        // Manage cancel click
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        // Manage release buttons colors
        Utils.setTextViewTextColorChangeOnTouch(deleteButton, R.color.black, R.color.red);
        Utils.setTextViewTextColorChangeOnTouch(cancelButton, R.color.black, R.color.grey);

        dialog.getWindow().getDecorView().setBackgroundResource(R.drawable.bg_dialog);


        dialog.show();

        return customView;
    }

    private static void onDeleteCardConfirmationClick(Context context, AlertDialog dialog, String uuid, TextView deleteButton) {
        deleteButton.setTextColor(ContextCompat.getColor(context, R.color.red));
        ActivityExplore.deleteCard(uuid);
        dialog.dismiss();
    }
}

