package com.example.ek.motionchallenge.motions;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by ek on 12/2/17.
 */

public class MotionAcceleration {
    private float mAcceleration;
    private int mDirectionChanged;
    private Direction mDirection;
    private ValueState m_ValueState;
    private final String TAG="MotionAcceleration";
    private final float MIN_VALUE_THREASHOLD = 0.1f;

    public final static int X_AXIS_MASK = 0x1;
    public final static int Y_AXIS_MASK = 0x10;
    public final static int Z_AXIS_MASK = 0x100;

    private HashMap<ValueState, Float> mLastStateValue = new HashMap<ValueState, Float>();

    MotionAcceleration(){
        reset();
    }
    public enum Direction {
        POSITIVE, NEGATIVE
    }

    public enum ValueState{
        INCREASING, DECREASING, STATIC
    }

    public void reset(){
        mAcceleration = 0.0f;
        mDirectionChanged = 0;
        m_ValueState = ValueState.STATIC;

        mLastStateValue.put(ValueState.STATIC,0.0f);
        mLastStateValue.put(ValueState.INCREASING,0.0f);
        mLastStateValue.put(ValueState.DECREASING,0.0f);
    }

    /**
     * Updates acceleration value and keeps track of the state (increasing or decreasing) and
     * the sign change (positive->negative and vice versa)
     * @param acceleration
     */
    public void update(float acceleration) {

        if( Math.abs(mAcceleration - acceleration) < MIN_VALUE_THREASHOLD)
        {
            Log.d(TAG, "not significant change from previous value of " + mAcceleration +", ignoring:" + acceleration);

            m_ValueState = ValueState.STATIC;
            mLastStateValue.put(m_ValueState,acceleration);
            mAcceleration = acceleration;
            return;
        }

        Log.d(TAG, "update: " + acceleration);

        if(mAcceleration == 0.0f) {
            // initial init
            if(acceleration > 0.0f)
                mDirection = Direction.POSITIVE;
            else if(acceleration < 0.0f)
                mDirection = Direction.NEGATIVE;
        }
        else{
            if(mDirection == Direction.NEGATIVE && acceleration > 0.0f){
                mDirection = Direction.POSITIVE;
                mDirectionChanged++;
            } else if(mDirection == Direction.POSITIVE && acceleration < 0.0f){
                mDirection = Direction.NEGATIVE;
                mDirectionChanged++;
            }
        }

        if(acceleration > 0.0f){
            if(acceleration > mAcceleration)
                m_ValueState = ValueState.INCREASING;
            else if (acceleration < mAcceleration)
                m_ValueState = ValueState.DECREASING;
            else
                m_ValueState = ValueState.STATIC;
        }
        else if(acceleration < 0.0f){
            if(acceleration < mAcceleration)
                m_ValueState = ValueState.INCREASING;
            else if (acceleration > mAcceleration)
                m_ValueState = ValueState.DECREASING;
            else
                m_ValueState = ValueState.STATIC;
        }
        else
            m_ValueState = ValueState.STATIC;

        mLastStateValue.put(m_ValueState,acceleration);
        mAcceleration = acceleration;
    }

    /**
     * checks if the acceleration is increasing
     * @return true if increasing, false otherwise
     */
    public boolean isIncreasing(){
        return m_ValueState == ValueState.INCREASING;
    }
    public boolean isDecreasing(){
        return m_ValueState == ValueState.DECREASING;
    }

    /**
     * checks how many times the direction of acceleration has changed
     * @return number of times
     */
    public int numOfDirChanged(){
        return mDirectionChanged;
    }

    /**
     * checks how many axis has changed direction at least one time
     * @param dirChangeMask
     * @return number of axis
     */
    static public int numOfAxisDirectionChange(int dirChangeMask){
        int count = 0;
        if( (dirChangeMask & X_AXIS_MASK) == X_AXIS_MASK)
            count++;
        if( (dirChangeMask & Y_AXIS_MASK) == Y_AXIS_MASK)
            count++;
        if( (dirChangeMask & Z_AXIS_MASK) == Z_AXIS_MASK)
            count++;

        return count;
    }
}

