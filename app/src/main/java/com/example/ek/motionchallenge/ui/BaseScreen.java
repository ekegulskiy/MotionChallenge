package com.example.ek.motionchallenge.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.ek.motionchallenge.R;

/**
 * Created by ek on 12/5/17.
 */

public class BaseScreen extends AppCompatActivity {

    static Bitmap mProfilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart (){
        super.onStart();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        displayProfilePhoto();
    }

    public void setProfilePhoto(Bitmap profilePhoto){
        mProfilePhoto = profilePhoto;
        displayProfilePhoto();
    }

    protected void displayProfilePhoto(){
        if(mProfilePhoto != null){
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowHomeEnabled(true);
            Resources res = getResources();
            BitmapDrawable icon = new BitmapDrawable(res,mProfilePhoto);
            actionBar.setIcon(icon);
        }
    }
}
