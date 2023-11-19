package com.guilsch.multivoc;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class DialogConfirmImportDataFile extends Dialog {

    private int decision;

    public DialogConfirmImportDataFile(Context context) {
        super(context);
        this.decision = Param.CONFIRM_CANCEL;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.dialog_layout_import_data_file, null);

        setContentView(customView);

        TextView importButton = customView.findViewById(R.id.dialog_import_button);
        TextView cancelButton = customView.findViewById(R.id.dialog_cancel_button);

        // Manage clicks
        importButton.setOnClickListener(v -> setDecisionAndLeave(Param.CONFIRM_IMPORT));
        cancelButton.setOnClickListener(v -> setDecisionAndLeave(Param.CONFIRM_CANCEL));

        // Manage release buttons colors
        Utils.setTextViewTextColorChangeOnTouch(importButton, R.color.black, R.color.red);
        Utils.setTextViewTextColorChangeOnTouch(cancelButton, R.color.black, R.color.grey);
        this.getWindow().getDecorView().setBackgroundResource(R.drawable.dialog_background);
    }

    private void setDecisionAndLeave(int decision) {
        this.decision = decision;
        this.dismiss();
    }

    public int getDecision() {
        return decision;
    }
}
