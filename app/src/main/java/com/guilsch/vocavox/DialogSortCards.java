package com.guilsch.vocavox;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class DialogSortCards {

    public static View showCustomDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View customView = inflater.inflate(R.layout.dialog_layout_sort_cards, null);
        builder.setView(customView);

        AlertDialog dialog = builder.create();

        dialog.getWindow().getDecorView().setBackgroundResource(R.drawable.bg_dialog);

        dialog.show();

//        ImageView sortDialogCross = dialog.findViewById(R.id.cross);
//        sortDialogCross.setOnClickListener(v -> dialog.dismiss());

        return customView;
    }
}

