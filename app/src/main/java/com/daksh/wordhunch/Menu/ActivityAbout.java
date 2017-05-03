package com.daksh.wordhunch.Menu;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.daksh.wordhunch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityAbout extends AppCompatActivity {

    //Bind views using butter knife
    @BindView(R.id.aboutPage_text_version) TextView txVersion;
    @BindView(R.id.aboutPage_introBox_email) TextView txEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //Bind the views
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            //populate the version code for the app
            txVersion.setText(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //Parse Email from the entire text to make it clickable
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

            }
        };

        String strEmail = txEmail.getText().toString();
        SpannableString spannableString = new SpannableString(strEmail);
        spannableString.setSpan(clickableSpan, strEmail.indexOf("daksh"), strEmail.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        txEmail.setText(spannableString);
    }
}