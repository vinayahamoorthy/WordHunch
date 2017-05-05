package com.daksh.wordhunch.Menu;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
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

    //A custom tabs client to warm up the custom tab implementation before the URL is actually loaded
    private CustomTabsClient client;

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

        CustomTabsClient.bindCustomTabsService(ActivityAbout.this, getPackageName(), new CustomTabsServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                client = null;
            }

            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                ActivityAbout.this.client = client;
            }
        });

        if(client != null) {
            client.warmup(1);
            CustomTabsSession session = client.newSession(new CustomTabsCallback());
            //Build a custom tabs to view github project
            String strUrl = "https://github.com/dakshsrivastava/WordHunch";
            session.mayLaunchUrl(Uri.parse(strUrl), null, null);
        }
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

    @OnClick(R.id.aboutPage_heading_github_box)
    public void onGithub(View view) {

        //Custom tabs primary color
        int intColor = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            intColor = getColor(R.color.orangeLight);
        else
            intColor = getResources().getColor(R.color.orangeLight);

        //Custom tabs secondary color
        int intSecondaryColor = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            intSecondaryColor = getColor(R.color.orangeDark);
        else
            intSecondaryColor = getResources().getColor(R.color.orangeDark);

        //Build a custom tabs to view github project
        String strUrl = "https://github.com/dakshsrivastava/WordHunch";
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .setToolbarColor(intColor)
                .setSecondaryToolbarColor(intSecondaryColor)
                .setInstantAppsEnabled(false)
                .setShowTitle(true)
                .build();

        //Launch the tabs
        customTabsIntent.launchUrl(ActivityAbout.this, Uri.parse(strUrl));
    }
}