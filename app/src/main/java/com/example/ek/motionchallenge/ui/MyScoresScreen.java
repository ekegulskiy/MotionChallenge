package com.example.ek.motionchallenge.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.example.ek.motionchallenge.R;

public class MyScoresScreen extends BaseScreen {
    private ListView mMyScoresList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_scores_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MotionScoreAdapter adapter = new MotionScoreAdapter(this);
        mMyScoresList = (ListView)findViewById(R.id.myScoresList);
        mMyScoresList.setAdapter(adapter);
    }

}
