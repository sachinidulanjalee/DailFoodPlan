package com.example.dailyfoodplan.view;

import android.content.Context;

import androidx.appcompat.widget.AppCompatCheckBox;

public class ServingCheckBox extends AppCompatCheckBox {
    private ServingCheckBox scbNextServing;
    private ServingCheckBox scvPrevServing;

    public ServingCheckBox(Context context) {
        super(context);
    }

    public void setNextServing(ServingCheckBox nextServing) {
        this.scvPrevServing = nextServing;
        nextServing.scbNextServing = this;
    }

    public void onCheckChange(boolean isChecked) {
        if (isChecked) {
            continueCheck();
        } else {
            continueUncheck();
        }
    }

    private void continueCheck() {
        if (scbNextServing != null && !scbNextServing.isChecked()) {
            scbNextServing.setChecked(true);
        }
    }

    private void continueUncheck() {
        if (scvPrevServing != null && scvPrevServing.isChecked()) {
            scvPrevServing.setChecked(false);
        }
    }
}
