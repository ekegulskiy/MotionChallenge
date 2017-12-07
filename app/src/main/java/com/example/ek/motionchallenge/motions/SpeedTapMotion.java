package com.example.ek.motionchallenge.motions;

import android.content.Context;

import com.example.ek.motionchallenge.model.MotionBase;

/**
 * Created by ek on 12/3/17.
 */

public class SpeedTapMotion extends MotionBase {
    private final String TAG="speedTapMotion";
    private final String NAME="speedTap";
    private final int MOTION_DURATION_SEC = 5;

    public SpeedTapMotion(Context context){
        super(context);
        setMotionDuration(MOTION_DURATION_SEC);
    }

    @Override
    public void onMotionEnd() {

    }

    @Override
    public void onMotionStart(){

    }

    @Override
    public String getMotionName() {
        return NAME;
    }
}
