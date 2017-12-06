package com.example.ek.motionchallenge.motions;

import android.content.Context;
import android.os.Handler;

/**
 * Created by ek on 11/15/17.
 */

public abstract class MotionBase {
    private final Context mContext;
    private MotionEventListener mEventListener;

    private int mMotionDuration;
    private int mMotionCount;
    private float mMotionScore;

    protected void setMotionDuration(int motionDurationSec){
        mMotionDuration = motionDurationSec;
    }
    public int getMotionDuration(){
        return mMotionDuration;
    }

    public MotionBase(Context context) {
        mContext = context;
        this.mEventListener = null;
    }

    public interface MotionEventListener {
        public void onMotionEnd();
        public void onMotionStart();
    }

    public void setMotionEventListener(MotionEventListener listener) {
        this.mEventListener = listener;
    }

    public abstract void onMotionEnd();
    public abstract void onMotionStart();
    public abstract String getMotionName();

    public void incrementMotionCount(){
        mMotionCount++;
    }

    public int getMotionCount(){
        return mMotionCount;
    }
    public float getMotionScore(){
        return mMotionScore;
    }

    public void setMotionScore(int score){
        mMotionCount = score;
    }

    public void setMotionScore(float score){
        mMotionScore = score;
    }

    public void start(){
        onMotionStart(); // notify derived class

        if(mEventListener != null)
            mEventListener.onMotionStart(); // notify listeners

        mMotionCount = 0; // reset
        Handler handler = new Handler();
        Runnable runnable = new Runnable(){
            public void run() {

                onMotionEnd(); // notify derived class

                if(mEventListener != null)
                    mEventListener.onMotionEnd(); // notify listeners
            }
        };
        handler.postDelayed(runnable, getMotionDuration()*1000);
    }

    public void cancel(){

    }

    public Context getContext(){
        return mContext;
    }
}
