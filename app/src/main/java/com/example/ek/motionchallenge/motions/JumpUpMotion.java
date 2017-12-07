package com.example.ek.motionchallenge.motions;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.ek.motionchallenge.model.MotionBase;

/**
 * Created by ek on 12/3/17.
 * Implements measuring of JumpUp motion
 */
public class JumpUpMotion extends MotionBase
        implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private final String TAG="jumUpMotion";
    private final String NAME="jumpUp";
    private final float MOTION_MIN_ACCELERATION = 1.0f;
    private final int MOTION_DURATION_SEC = 5;
    private final float GRAVITY_ACCELERATION = 9.8f;
    private final float INCHES_IN_METER = 39.37f;

    private long mStartTime;
    private long mEndTime;

    private MotionAcceleration mZMotionAcceleration;

    public JumpUpMotion(Context context){
        super(context);
        setMotionDuration(MOTION_DURATION_SEC);
        initSensors();
        mStartTime = 0;
        mEndTime = 0;
        mZMotionAcceleration = new MotionAcceleration();
    }

    protected void initSensors(){
        mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void Resume(){
        mSensorManager.registerListener(this, mAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void Pause(){ }

    @Override
    public void onMotionStart(){
        mSensorManager.registerListener(this, mAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onMotionEnd()
    {
        Pause();
    }

    @Override
    public String getMotionName(){
        return NAME;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            float curAcceleration_Z = event.values[2];
            mZMotionAcceleration.update(curAcceleration_Z);

            if(mStartTime == 0 && mZMotionAcceleration.isDecreasing()){
               mStartTime = event.timestamp/1000000; // set start time when acceleration starts to decrease
               Log.d(TAG,"Setting mStartTime = " + mStartTime);
            }
            else if(mStartTime != 0 && mZMotionAcceleration.isIncreasing()) {

                float distance = calculateDistance(mStartTime, mEndTime);

                setMotionScore((int)distance);
                setMotionScore(distance);
                mZMotionAcceleration.reset();

                mEndTime = 0;
                mStartTime = 0;
                mSensorManager.unregisterListener(this); // we are done, unregister listener
            }

            mEndTime = event.timestamp/1000000; // keep track of latest timestamp
            Log.d(TAG,"Updating mEndTime = " + mEndTime);

            //Log.d(TAG, " TYPE_ROTATION_VECTOR ACCELERATION x=" + curAcceleration_X + ",y=" + curAcceleration_Y + ",z=" + curAcceleration_Z);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected float calculateDistance(long startTime, long endTime){

        Log.d(TAG, "calculateDistance()");
        Log.d(TAG, "    startTime         = " + startTime);
        Log.d(TAG, "    endTime           = " + endTime);

        // Follow the formula from https://sciencing.com/calculate-height-velocity-8115675.html

        // Final Velocity = Initial Velocity + (Acceleration Due To Gravity * Time)
        // VF = V0 + a*t
        // 1. Calculate initial velocity V0
        float V0, VF = 0.0f;
        long timeDur = endTime - startTime;
        float t = (float)timeDur/1000.0f;
        float a = -GRAVITY_ACCELERATION;
        V0 = VF - (a*t);

        // Height = (Initial velocity * Time) + (Acceleration Due to Gravity * Time Squared Over 2)
        // h = (v0 * t) + (a * (t*t)/2)
        // 2. Calculate distance
        float h = (V0*t) + (a*t*t/2);

        Log.d(TAG, "    V0=" + V0);
        Log.d(TAG, "    t=" + t);
        Log.d(TAG, "    a=" + a);
        Log.d(TAG, "    h=" + h);

        // Convert meters to inches
        // 1 m = 39.37 inches

        return (h*INCHES_IN_METER);
    }
}
