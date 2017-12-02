package com.example.ek.motionchallenge.motions;

/**
 * Created by ek on 12/2/17.
 */

public class MotionAcceleration {
    private float mAcceleration;
    private int mDirectionChanged;
    Direction mDirection;

    public final static int X_AXIS_MASK = 0x1;
    public final static int Y_AXIS_MASK = 0x10;
    public final static int Z_AXIS_MASK = 0x100;

    MotionAcceleration(){
        reset();
    }
    private enum Direction {
        POSITIVE, NEGATIVE
    }

    public void reset(){
        mAcceleration = 0.0f;
        mDirectionChanged = 0;
    }
    public void update(float acceleration) {
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
        mAcceleration = acceleration;
    }

    public int numOfDirChanged(){
        return mDirectionChanged;
    }

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

