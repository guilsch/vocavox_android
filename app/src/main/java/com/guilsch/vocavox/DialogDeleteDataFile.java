package com.guilsch.vocavox;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class DialogDeleteDataFile extends Dialog {

    private int decision;

    public DialogDeleteDataFile(Context context) {
        super(context);
        this.decision = Param.CONFIRM_CANCEL;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.dialog_layout_delete_data_file, null);

        setContentView(customView);

        TextView deleteButton = customView.findViewById(R.id.dialog_delete_button);
        TextView cancelButton = customView.findViewById(R.id.dialog_cancel_button);

        // Manage clicks
        deleteButton.setOnClickListener(v -> setDecisionAndLeave(Param.CONFIRM_DELETE_DATA_FILE));
        cancelButton.setOnClickListener(v -> setDecisionAndLeave(Param.CONFIRM_CANCEL));

        // Manage release buttons colors
        Utils.setTextViewTextColorChangeOnTouch(deleteButton, R.color.black, R.color.red);
        Utils.setTextViewTextColorChangeOnTouch(cancelButton, R.color.black, R.color.grey);
        this.getWindow().getDecorView().setBackgroundResource(R.drawable.bg_dialog);
    }

    private void setDecisionAndLeave(int decision) {
        this.decision = decision;
        this.dismiss();
    }

    public int getDecision() {
        return decision;
    }
}
