package com.example.ek.motionchallenge.model;

import android.content.Context;
import android.os.Handler;
import android.view.View;

/**
 * Created by ek on 11/15/17.
 */

public abstract class MotionBase {
    private final Context mContext;
    private MotionEventListener mEventListener;

    private int mMotionDuration;
    private int mMotionCount;

    protected void setMotionDuration(int motionDurationSec){
        mMotionDuration = motionDurationSec;
    }
    public int getmMotionDuration(){
        return mMotionDuration;
    }

    public MotionBase(Context context) {
        mContext = context;
        this.mEventListener = null;
    }

    public interface MotionEventListener {
        public void onMotionEnd();
    }

    public void setMotionEventListener(MotionEventListener listener) {
        this.mEventListener = listener;
    }

    public abstract void onMotionEnd();

    public void incrementMotionCount(){
        mMotionCount++;
    }

    public int getMotionCount(){
        return mMotionCount;
    }

    public void start(){
        mMotionCount = 0; // reset
        Handler handler = new Handler();
        Runnable runnable = new Runnable(){
            public void run() {
                if(mEventListener != null)
                    mEventListener.onMotionEnd();
            }
        };
        handler.postDelayed(runnable, getmMotionDuration()*1000);
    }

    public void cancel(){

    }

    public Context getContext(){
        return mContext;
    }
}
