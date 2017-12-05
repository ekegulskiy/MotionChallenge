/**
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// uses sound from   http://www.downloadfreesound.com/8-bit-sound-effects/


package com.example.ek.motionchallenge.ui;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ek.motionchallenge.backend.FirebaseScoresDB;
import com.example.ek.motionchallenge.R;
import com.example.ek.motionchallenge.motions.JumpUpMotion;
import com.example.ek.motionchallenge.motions.MotionBase;
import com.example.ek.motionchallenge.motions.ShakeMotion;
import com.example.ek.motionchallenge.motions.SpeedTapMotion;
import com.example.ek.motionchallenge.motions.Swing360Motion;

public class MotionScreen extends BaseScreen {
    private TextView mMotionDescView;
    private Button mStartBtn;
    private ProgressBar mMotionProgressBar;
    private MotionBase mMotion;
    private TextView mLastScoreView;
    private MediaPlayer mMediaPlayer;
    private TextView mFeedbackView;
    private FirebaseScoresDB mScoresDB;
    private int mMotionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        mMotionID = b.getInt("motionID");

        mMotionDescView = findViewById(R.id.MotionScreen_descView);
        mStartBtn = findViewById(R.id.MotionScreen_StartBtn);
        mMotionProgressBar = findViewById(R.id.MotionScreen_motionProgressBar);
        mLastScoreView = findViewById(R.id.MotoinScreen_lastScoreView);
        mFeedbackView = findViewById(R.id.MotionScreen_feedbackView);
        mFeedbackView.setText("");


        mScoresDB = FirebaseScoresDB.getInstance();

        switch(mMotionID){
            case R.id.shakeMotionIconView:
                mMotionDescView.setText(R.string.home_screen_shake_motion_desc);
                mMotion = new ShakeMotion(this);
                break;

            case R.id.swing360MotionIconView:
                mMotionDescView.setText(R.string.home_screen_swing360_motion_desc);
                mMotion = new Swing360Motion(this);
                break;

            case R.id.jumpUpMotionIconView:
                mMotionDescView.setText(R.string.home_screen_jumpup_motion_desc);
                mMotion = new JumpUpMotion(this);
                break;

            case R.id.speedTapMotionIconView:
                mMotionDescView.setText(R.string.home_screen_speedtap_motion_desc);
                mMotion = new SpeedTapMotion(this);

                mMotionProgressBar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mMotion.incrementMotionCount();
                    }
                });
                break;

            default:
                break;
        }

        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaPlayer.start();

                mMotionProgressBar.setVisibility(View.VISIBLE);
                mStartBtn.setVisibility(View.INVISIBLE);

                mMotion.start();
            }
        });

        mMotion.setMotionEventListener(new MotionBase.MotionEventListener() {

            public void onMotionStart(){

            }

            public void onMotionEnd() {
                mMediaPlayer.start();
                mMotionProgressBar.setVisibility(View.INVISIBLE);
                mStartBtn.setVisibility(View.VISIBLE);

                CharSequence curText = getString(R.string.motion_screen_last_score);
                curText = curText + Integer.toString(mMotion.getMotionCount());
                mLastScoreView.setText(curText);
                mFeedbackView.setText(R.string.motion_screen_feedback);

                mScoresDB.updateMotionScore(mMotion.getMotionName(), mMotion.getMotionCount());
            }
        });

        CharSequence curText = getString(R.string.motion_screen_last_score);
        curText = curText + Integer.toString(mScoresDB.getMotionLastScore(mMotion.getMotionName()));
        mLastScoreView.setText(curText);

        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.beep1);
        mMotionProgressBar.getIndeterminateDrawable().setColorFilter(0xFF33b5E5, android.graphics.PorterDuff.Mode.MULTIPLY);
    }

}
