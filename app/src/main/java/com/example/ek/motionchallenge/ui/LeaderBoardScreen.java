package com.example.ek.motionchallenge.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.example.ek.motionchallenge.R;

public class LeaderBoardScreen extends BaseScreen {
    private ListView mLeadersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LeaderScoreAdapter adapter = new LeaderScoreAdapter(this);

        mLeadersList = (ListView)findViewById(R.id.leadersList);
        mLeadersList.setAdapter(adapter);
    }

}
