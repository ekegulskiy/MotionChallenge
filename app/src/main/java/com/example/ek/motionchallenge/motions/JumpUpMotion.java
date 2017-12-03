package com.example.ek.motionchallenge.motions;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.sql.Time;

/**
 * Created by ek on 12/3/17.
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

    private long mStartTime;
    private long mEndTime;
    private float mStartAcceleration;
    private float mEndAcceleration;

    private int mAxisDirectionChanged;

    private MotionAcceleration mZMotionAcceleration;

    public JumpUpMotion(Context context){
        super(context);
        setMotionDuration(MOTION_DURATION_SEC);
        initSensors();
        mAxisDirectionChanged = 0;
        mEndAcceleration = 0.0f;
        mStartAcceleration = 0.0f;

        mZMotionAcceleration = new MotionAcceleration();
    }

    protected void initSensors(){
        mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void Resume(){
        mSensorManager.registerListener(this, mAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void Pause(){
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onMotionStart(){
        mSensorManager.registerListener(this, mAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        mStartTime = System.currentTimeMillis();
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

            float curAcceleration_X = event.values[0];
            float curAcceleration_Y = event.values[1];
            float curAcceleration_Z = event.values[2];

            if(Math.abs(curAcceleration_Z - GRAVITY_ACCELERATION) > MOTION_MIN_ACCELERATION)
            {
                mZMotionAcceleration.update(curAcceleration_Z);

                if(mStartAcceleration == 0.0f)
                    mStartAcceleration = curAcceleration_Z;
            }

            if(mZMotionAcceleration.isIncreasing())
            {
                mEndTime = System.currentTimeMillis();
                mEndAcceleration = mZMotionAcceleration.getLastStateValue(MotionAcceleration.ValueState.DECREASING);

                float distance = calculateDistance(mStartTime, mEndTime, mStartAcceleration, mEndAcceleration);

                setMotionScore((int)distance);
                mZMotionAcceleration.reset();
                mAxisDirectionChanged = 0;
            }

            //Log.d(TAG, " TYPE_ROTATION_VECTOR ACCELERATION x=" + curAcceleration_X + ",y=" + curAcceleration_Y + ",z=" + curAcceleration_Z);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public float calculateDistance(long startTime, long endTime, float startAcceleration, float endAcceleration){

        long timeDur = endTime - startTime;

        float aveAcceleration = (endAcceleration + startAcceleration)/2.0f;

        float timeDurSec = (float)timeDur/1000.0f;

        float aveVelocity =  timeDurSec*aveAcceleration;

        return aveVelocity*timeDurSec;
    }

}
