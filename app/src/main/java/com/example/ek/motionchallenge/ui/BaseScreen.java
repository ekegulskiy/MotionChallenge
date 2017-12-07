package com.example.ek.motionchallenge.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.ek.motionchallenge.R;
import com.example.ek.motionchallenge.backend.FirebaseScoresDB;

/**
 * Created by ek on 12/5/17.
 * Base class for all screens of the application
 */

public class BaseScreen extends AppCompatActivity {
    protected FirebaseScoresDB mScoresDB;
    public static Bitmap sProfilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mScoresDB = FirebaseScoresDB.getInstance();
    }

    /**
     * When activity is started, displays user profile photo (if exists) in the action bar
     */
    @Override
    protected void onStart (){
        super.onStart();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        displayProfilePhoto();
    }

    /**
     * stores user profile photo and displays it in the action bar
     * @param profilePhoto user profile photo
     */
    public void setProfilePhoto(Bitmap profilePhoto){
        sProfilePhoto = profilePhoto;
        displayProfilePhoto();
    }

    /**
     * displays user profile photo in the action bar
     */
    protected void displayProfilePhoto(){
        if(sProfilePhoto != null){
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowHomeEnabled(true);
            Resources res = getResources();
            BitmapDrawable icon = new BitmapDrawable(res,sProfilePhoto);
            actionBar.setIcon(icon);
        }
    }
}
