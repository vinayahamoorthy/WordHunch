package com.daksh.wordhunch.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.daksh.wordhunch.R;
import com.daksh.wordhunch.Rink.RingActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ActivityMainMenu extends AppCompatActivity {

    //An unbinder object to unbind the views when the activity finishes
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        //Bind Views
        unbinder = ButterKnife.bind(this);
    }

    /**
     * The menu button starts the game | Navigates to RinkActivity
     */
    @OnClick(R.id.menu_start)
    public void onMenuStart() {
        Intent intent = new Intent(this, RingActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.menu_about)
    public void onMenuAbout() {
        Intent intent = new Intent(this, ActivityAbout.class);
        startActivity(intent);
    }

//    @OnClick(R.id.menu_start)
//    public void onMenuStart(View view) {
//        Intent intent = new Intent(this, RingActivity.class);
//        startActivity(intent);
//    }

    @Override
    protected void onStop() {
        super.onStop();
        if(unbinder != null)
            unbinder.unbind();
    }
}