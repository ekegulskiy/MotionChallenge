package com.example.ek.motionchallenge.motions;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.ek.motionchallenge.model.MotionBase;

/**
 * Created by ek on 11/15/17.
 */

public class ShakeMotion extends MotionBase
        implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private final String TAG="ShakeMotion";
    private final String NAME="shake";
    private final float SHAKE_MOTION_MIN_ACCELERATION = 2.0f;
    private final int SHAKE_MOTION_DURATION_SEC = 5;
    private ShakeDirection mDirection;

    private enum ShakeDirection {
        SHAKE_MOTION_FORWARD, SHAKE_MOTION_BACKWARD
    }

    private float mX = 0.0f;
    private float mY = 0.0f;
    private float mZ = 0.0f;

    public ShakeMotion(Context context){
        super(context);
        setMotionDuration(SHAKE_MOTION_DURATION_SEC);
        initSensors();
        mDirection = ShakeDirection.SHAKE_MOTION_BACKWARD;
    }

    protected void initSensors(){
        mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //       mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

    }

    public void Resume(){
        mSensorManager.registerListener(this, mAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void Pause(){
        mSensorManager.unregisterListener(this);
    }

    public void onMotionEnd()
    {
        Pause();
    }

    public void onMotionStart(){
        mSensorManager.registerListener(this, mAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public String getMotionName(){
        return NAME;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            if( x > SHAKE_MOTION_MIN_ACCELERATION && mDirection == ShakeDirection.SHAKE_MOTION_BACKWARD) {
                Log.d(TAG, "Shake direction switched for X");
                mDirection = ShakeDirection.SHAKE_MOTION_FORWARD;
                incrementMotionCount();
            }
            if( x < -SHAKE_MOTION_MIN_ACCELERATION && mDirection == ShakeDirection.SHAKE_MOTION_FORWARD) {
                Log.d(TAG, "Shake direction switched for X");
                mDirection = ShakeDirection.SHAKE_MOTION_BACKWARD;
                incrementMotionCount();
            }

            //     Log.d(TAG, "RAW ACCELERATION x=" + x + ",y=" + y + ",z=" +z);

            mX = x;
            mY = y;
            mZ = z;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
