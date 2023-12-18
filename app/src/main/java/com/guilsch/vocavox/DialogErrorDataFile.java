package com.guilsch.vocavox;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class DialogErrorDataFile extends Dialog {

    public DialogErrorDataFile(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.dialog_layout_error_datafile, null);

        setContentView(customView);

        this.getWindow().getDecorView().setBackgroundResource(R.drawable.bg_dialog);
    }
}
