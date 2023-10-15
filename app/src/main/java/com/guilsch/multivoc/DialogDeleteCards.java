package com.guilsch.multivoc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import org.w3c.dom.Text;

public class DialogDeleteCards {

    public static <DialogClosedListener> View showCustomDialog(Context context, String uuid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View customView = inflater.inflate(R.layout.dialog_layout_delete_card, null);
        builder.setView(customView);

        TextView deleteButton = customView.findViewById(R.id.dialog_delete_card_delete_button);
        TextView cancelButton = customView.findViewById(R.id.dialog_delete_card_cancel_button);

        AlertDialog dialog = builder.create();

        // Manage delete click
        deleteButton.setOnClickListener(v -> onDeleteCardConfirmationClick(context, dialog, uuid, deleteButton));

        // Manage cancel click
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        // Manage release buttons colors
        Utils.setTextViewTextColorChangeOnTouch(deleteButton, R.color.black, R.color.red);
        Utils.setTextViewTextColorChangeOnTouch(cancelButton, R.color.black, R.color.grey);

        dialog.getWindow().getDecorView().setBackgroundResource(R.drawable.dialog_background);

        dialog.show();

        return customView;
    }

    private static void onDeleteCardConfirmationClick(Context context, AlertDialog dialog, String uuid, TextView deleteButton) {
        deleteButton.setTextColor(ContextCompat.getColor(context, R.color.red));
        ActivityExplore.deleteCard(uuid);
        dialog.dismiss();
    }
}

