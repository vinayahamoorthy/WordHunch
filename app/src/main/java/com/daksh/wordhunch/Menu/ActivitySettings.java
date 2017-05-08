package com.daksh.wordhunch.Menu;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.daksh.wordhunch.R;
import com.daksh.wordhunch.Util.SettingsUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivitySettings extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {

    //ButterKnife views
    @BindView(R.id.SettingsPage_seekbar_volume) SeekBar volumeSeekBar;
    @BindView(R.id.SettingsPage_volume_image) ImageView imgSpeaker;
    @BindView(R.id.SettingsPage_switch_vibration) SwitchCompat vibrationSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        //Set initial position based on saved value
        volumeSeekBar.setProgress(SettingsUtil.getVolume());

        //Set saved value for Vibration
        vibrationSwitch.setOnCheckedChangeListener(ActivitySettings.this);
        vibrationSwitch.setChecked(SettingsUtil.isVibrationEnabled());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Apply a seekbar change listener to the volume control
        volumeSeekBar.setOnSeekBarChangeListener(ActivitySettings.this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.i(ActivitySettings.class.getSimpleName(), "onProgressChanged : " + progress + ":" + fromUser);

        if(progress == 0)
            imgSpeaker.setImageResource(R.drawable.ic_volume_mute);
        else if(progress > 50)
            imgSpeaker.setImageResource(R.drawable.ic_volume_loud);
        else
            imgSpeaker.setImageResource(R.drawable.ic_volume_low);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //Empty method stub
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //Save new volume in shared pref
        SettingsUtil.setVolume(seekBar.getProgress());
    }

    @OnClick(R.id.SettingsPage_heading_vibration_box)
    public void onVibrationClick(View view) {
        vibrationSwitch.performClick();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SettingsUtil.setVibration(isChecked);
    }

    //ActivitySettings is used in association with a builder pattern. This is done so as to ensure all
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
            return new Intent(context, ActivitySettings.class);
        }
    }
}