package com.example.ek.motionchallenge.motions;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import static com.example.ek.motionchallenge.motions.MotionAcceleration.numOfAxisDirectionChange;

/**
 * Created by ek on 12/2/17.
 */

public class Swing360Motion extends MotionBase
        implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private final String TAG="Swing360Motion";
    private final String NAME="360swing";
    private final float SHAKE_MOTION_MIN_ACCELERATION = 1.0f;
    private final int MOTION_DURATION_SEC = 5;

    private int mAxisDirectionChanged;

    private MotionAcceleration mXMotionAcceleration;
    private MotionAcceleration mYMotionAcceleration;
    private MotionAcceleration mZMotionAcceleration;

    public Swing360Motion(Context context){
        super(context);
        setMotionDuration(MOTION_DURATION_SEC);
        initSensors();
        mAxisDirectionChanged = 0;

        mXMotionAcceleration = new MotionAcceleration();
        mYMotionAcceleration = new MotionAcceleration();
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

            if(Math.abs(curAcceleration_X) > SHAKE_MOTION_MIN_ACCELERATION)
                mXMotionAcceleration.update(curAcceleration_X);
            if(Math.abs(curAcceleration_Y) > SHAKE_MOTION_MIN_ACCELERATION)
                mYMotionAcceleration.update(curAcceleration_Y);
            if(Math.abs(curAcceleration_Z) > SHAKE_MOTION_MIN_ACCELERATION)
                mZMotionAcceleration.update(curAcceleration_Z);

            if(mXMotionAcceleration.numOfDirChanged() > 2)
                mAxisDirectionChanged = mAxisDirectionChanged | MotionAcceleration.X_AXIS_MASK;

            if(mYMotionAcceleration.numOfDirChanged() > 2)
                mAxisDirectionChanged = mAxisDirectionChanged | MotionAcceleration.Y_AXIS_MASK;

            if(mZMotionAcceleration.numOfDirChanged() > 2)
                mAxisDirectionChanged = mAxisDirectionChanged | MotionAcceleration.Z_AXIS_MASK;

            if(MotionAcceleration.numOfAxisDirectionChange(mAxisDirectionChanged) >= 2) // at least 2
            {
                incrementMotionCount();
                mXMotionAcceleration.reset();
                mYMotionAcceleration.reset();
                mZMotionAcceleration.reset();
                mAxisDirectionChanged = 0;
            }

//            Log.d(TAG, " TYPE_ROTATION_VECTOR ACCELERATION x=" + x + ",y=" + y + ",z=" +z);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
