package com.example.ek.motionchallenge.ui;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ek.motionchallenge.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeScreen extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private static final String TAG = "HomeScreen";
    public static final String GUEST = "Guest";
    private String mUsername;

    private GoogleApiClient mGoogleApiClient;

    // Firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    // Views
    private ImageView mShakeMotionIconView;
    private ImageView mSwing360MotionIconView;
    private ImageView mJumpUpMotionIconView;
    private ImageView mSpeedTapMotionIconView;
    private TextView mMotionDescView;

    @Override // Implement the OnClickListener callback
    public void onClick(View v) {
        mShakeMotionIconView.clearColorFilter();
        mSwing360MotionIconView.clearColorFilter();
        mJumpUpMotionIconView.clearColorFilter();
        mSpeedTapMotionIconView.clearColorFilter();

        ImageView imageView = (ImageView)v;
        imageView.setColorFilter(0xFF33b5E5, PorterDuff.Mode.SCREEN);

        switch(v.getId()){
            case R.id.shakeMotionIconView:
                mMotionDescView.setText(R.string.home_screen_shake_motion_desc);
                break;

            case R.id.swing360MotionIconView:
                mMotionDescView.setText(R.string.home_screen_swing360_motion_desc);
                break;

            case R.id.jumpUpMotionIconView:
                mMotionDescView.setText(R.string.home_screen_jumpup_motion_desc);
                break;

            case R.id.speedTapMotionIconView:
                mMotionDescView.setText(R.string.home_screen_speedtap_motion_desc);
                break;

            default:
                break;
        }
    }

    private void initViews(){
        mShakeMotionIconView = findViewById(R.id.shakeMotionIconView);
        mSwing360MotionIconView = findViewById(R.id.swing360MotionIconView);
        mJumpUpMotionIconView = findViewById(R.id.jumpUpMotionIconView);
        mSpeedTapMotionIconView = findViewById(R.id.speedTapMotionIconView);

        mShakeMotionIconView.setOnClickListener(this);
        mSwing360MotionIconView.setOnClickListener(this);
        mJumpUpMotionIconView.setOnClickListener(this);
        mSpeedTapMotionIconView.setOnClickListener(this);

        mMotionDescView = findViewById(R.id.motionDescView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Motion Challenge is starting...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set default username is anonymous.
        mUsername = GUEST;
        //Initialize Auth
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser == null) {
            startActivity(new Intent(this, SignInScreen.class));
            finish();
            return;
        } else {
            mUsername = mUser.getDisplayName();
            if (mUser.getPhotoUrl() != null) {
                // TODO implement user profile picture
                //  mPhotoUrl = mUser.getPhotoUrl().toString();
            }
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
}
