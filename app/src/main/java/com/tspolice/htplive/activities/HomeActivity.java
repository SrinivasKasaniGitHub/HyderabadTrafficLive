package com.tspolice.htplive.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.tspolice.htplive.R;
import com.tspolice.htplive.fragments.HomeFragment;
import com.tspolice.htplive.utils.UiHelper;

public class HomeActivity extends AppCompatActivity {

    public static FragmentManager mFragmentManager;
    private UiHelper mUiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar =  findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        mFragmentManager = getSupportFragmentManager();
        mUiHelper = new UiHelper(this);

        FrameLayout frameLayout =  findViewById(R.id.content_frame);
        overridePendingTransition(R.anim.fade_enter, R.anim.fade_leave);
        if (frameLayout != null) {
            if (savedInstanceState != null) {
                return;
            }
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            HomeFragment homeFragment = new HomeFragment();
            fragmentTransaction.add(R.id.content_frame, homeFragment, null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof HomeFragment) {
            mUiHelper.alertDialogOkCancel(getResources().getString(R.string.do_you_want_exit), false, "HOME");
        } else {
            super.onBackPressed();
        }
    }

    /*public void setTitle(int resource) {
        Objects.requireNonNull(((AppCompatActivity) getApplicationContext()).getSupportActionBar()).setTitle(resource);
    }

    public void setSubTitle(int resource) {
        Objects.requireNonNull(((AppCompatActivity) getApplicationContext()).getSupportActionBar()).setSubtitle(resource);
    }*/
}