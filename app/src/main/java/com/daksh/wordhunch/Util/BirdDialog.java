package com.daksh.wordhunch.Util;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.view.Window;

import com.daksh.wordhunch.R;


public class BirdDialog extends Dialog {

    /**
     * Hosting activity reference
     */
    private Activity ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.progress_dialog_layout);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    cancel();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 30000);
    }

    public BirdDialog(Activity ctx) {
        super(ctx);
        this.ctx = ctx;
    }
}