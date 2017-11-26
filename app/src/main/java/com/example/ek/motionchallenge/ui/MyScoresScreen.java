package com.example.ek.motionchallenge.ui;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.example.ek.motionchallenge.R;
import com.example.ek.motionchallenge.backend.FirebaseScoresDB;

public class MyScoresScreen extends AppCompatActivity {
    private FirebaseScoresDB mScoresDB;


    private void initMotionScore(@IdRes int i){
        TextProgressBar motionScore = (TextProgressBar)findViewById(i);

        String motionName = "";
        switch(i){
            case R.id.shakeMotionScore:
                motionName = "shake";
                break;
            case R.id.swing360MotionScore:
                motionName = "";
                break;
            case R.id.jumpUpMotionIconScore:
                motionName = "";
                break;
            case R.id.speedTapMotionScore:
                motionName = "";
                break;
            default:
                assert false;
        }

        if(!motionName.equals("")) {
            motionScore.setTitle(motionName);
            motionScore.setMin(mScoresDB.getOverallMinScore(motionName));
            motionScore.setMax(mScoresDB.getOverallMaxScore((motionName)));
            motionScore.setProgress(mScoresDB.getMotionBestScore((motionName)));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_scores_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mScoresDB = FirebaseScoresDB.getInstance();

        initMotionScore(R.id.shakeMotionScore);
        initMotionScore(R.id.swing360MotionScore);
        initMotionScore(R.id.jumpUpMotionIconScore);
        initMotionScore(R.id.speedTapMotionScore);

    }

}
