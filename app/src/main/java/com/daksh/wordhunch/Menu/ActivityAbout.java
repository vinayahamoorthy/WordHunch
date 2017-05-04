package com.daksh.wordhunch.Menu;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.daksh.wordhunch.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

        //Set spans on the email portion of the header
        //get string value of txEmail
        String strEmail = txEmail.getText().toString();

        //Create a spannable string that hosts all spans
        SpannableString spannableString = new SpannableString(strEmail);
        //Set a color span to apply on email
        ForegroundColorSpan colorSpan;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            colorSpan = new ForegroundColorSpan(getColor(R.color.WhiteTextColorOne));
        else
            colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.WhiteTextColorOne));
        //apply the color span
        spannableString.setSpan(colorSpan, strEmail.indexOf("daksh"), strEmail.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        //apply it on the textView
        txEmail.setText(spannableString);
//        txEmail.setMovementMethod(new LinkMovementMethod());
    }

    @OnClick(R.id.aboutPage_introBox)
    public void onEmailClick(View view) {
        //Create an intent to send an email when clicked
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, "dakshsrivastava@gmail.com");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "WordHunch Feedback");
        emailIntent.setData(Uri.parse("mailto:"));

        if(getPackageManager().resolveActivity(emailIntent, PackageManager.MATCH_DEFAULT_ONLY) != null)
            startActivity(emailIntent);
    }
}