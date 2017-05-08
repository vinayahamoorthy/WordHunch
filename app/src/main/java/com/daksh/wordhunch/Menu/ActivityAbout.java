package com.daksh.wordhunch.Menu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsService;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.daksh.wordhunch.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityAbout extends AppCompatActivity {

    //Bind views using butter knife
    @BindView(R.id.aboutPage_text_version) TextView txVersion;
    @BindView(R.id.aboutPage_introBox_email) TextView txEmail;

    //A custom tabs client to warm up the custom tab implementation before the URL is actually loaded
    private CustomTabsClient client;
    private CustomTabsIntent customTabsIntent;
    private CustomTabsSession session;

    //A custom tab service connection associated with the custom tab used to open github links
    private CustomTabsServiceConnection serviceConnection = new CustomTabsServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            client = null;
        }

        @Override
        public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
            ActivityAbout.this.client = client;

            //warm up the custom tab
            client.warmup(1);
            //Create a new session
            session = client.newSession(new CustomTabsCallback());
            //Create a new bundle and add the libraries URL as a parcelable
            Bundle bundle = new Bundle();
            bundle.putParcelable(CustomTabsService.KEY_URL, Uri.parse("https://github.com/dakshsrivastava/WordHunch/wiki/Open-Source-Libraries-used"));

            //Create a new bundle and add the libraries url bundle to it to pass it as an argument
            //to mayLaunchUrl method
            List<Bundle> lsBundle = new ArrayList<>();
            lsBundle.add(bundle);

            //Build a custom tabs to view github project
            String strUrl = "https://github.com/dakshsrivastava/WordHunch";
            session.mayLaunchUrl(Uri.parse(strUrl), null, lsBundle);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //Bind the views
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Custom tabs primary color
        CustomTabsClient.bindCustomTabsService(ActivityAbout.this, getPackageName(), serviceConnection);

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

        //Build a custom tabs intent using a builder
        customTabsIntent = new CustomTabsIntent.Builder(session)
                .setToolbarColor(intColor)
                .setSecondaryToolbarColor(intSecondaryColor)
                .setInstantAppsEnabled(false)
                .setShowTitle(true)
                .build();
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

    @OnClick(R.id.aboutPage_heading_libraries_box)
    public void onLibraries(View view) {
        //Build a custom tabs to view github project
        String strUrl = "https://github.com/dakshsrivastava/WordHunch/wiki/Open-Source-Libraries-used";

        //Launch the tabs
        customTabsIntent.launchUrl(ActivityAbout.this, Uri.parse(strUrl));
    }

    @OnClick(R.id.aboutPage_heading_github_box)
    public void onGithub(View view) {

        //Build a custom tabs to view github project
        String strUrl = "https://github.com/dakshsrivastava/WordHunch";

        //Launch the tabs
        customTabsIntent.launchUrl(ActivityAbout.this, Uri.parse(strUrl));
    }

    //About Activity is used in association with a builder pattern. This is done so as to ensure all
    //prerequisite parameters are sent to the activity that are required to ensure its proper
    //functioning. This ensures even when someone else than the primary developer works on this activity.
    //s/he will not have to go through layers of code to find which params are required
    //to instantiate this module.
    public static class Builder {

        //A context to the instantiating activity
        private Context context;

        private Builder() {
            //Empty private constructor to disable instantiation
        }

        /**
         * The constructor to be called to instantiate the class.
         * @param context The context of the calling activity is required to manufacture the intent
         */
        public Builder(Context context) {
            this.context = context;
        }

        /**
         * The build method executes and generates an intent. Returns the intent that may be used with
         * startActivity or startActivityForResult.
         *
         * @return Intent that may be used to start the RingActivity.
         */
        public Intent build() {
            Intent intent = new Intent(context, ActivityAbout.class);
            return intent;
        }
    }
}