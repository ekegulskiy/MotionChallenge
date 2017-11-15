/**
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import android.widget.Button;
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
    private int mSelectedMotionIconID;
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
    private Button mStartBtn;

    @Override // Implement the OnClickListener callback
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.shakeMotionIconView:
                activateMotionIcon(v, R.string.home_screen_shake_motion_desc);
                break;

            case R.id.swing360MotionIconView:
                activateMotionIcon(v, R.string.home_screen_swing360_motion_desc);
                break;

            case R.id.jumpUpMotionIconView:
                activateMotionIcon(v, R.string.home_screen_jumpup_motion_desc);
                break;

            case R.id.speedTapMotionIconView:
                activateMotionIcon(v, R.string.home_screen_speedtap_motion_desc);
                break;

            case R.id.startMotionBtn:
                Intent motionScreenIntent = new Intent(this, MotionScreen.class);
                motionScreenIntent.putExtra("motionID", mSelectedMotionIconID);
                startActivity(motionScreenIntent);
                break;
            default:
                break;
        }
    }

    /** Activates the icon for the selected motion and display the text in the Motion
     * description view
     * */
    private void activateMotionIcon(View motionIcon, int motionDescStrID){
        mShakeMotionIconView.clearColorFilter();
        mSwing360MotionIconView.clearColorFilter();
        mJumpUpMotionIconView.clearColorFilter();
        mSpeedTapMotionIconView.clearColorFilter();

        ImageView iconToActivate = (ImageView)motionIcon;

        mSelectedMotionIconID = motionIcon.getId();
        mMotionDescView.setText(motionDescStrID);
        iconToActivate.setColorFilter(0xFF33b5E5, PorterDuff.Mode.SCREEN);
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

        mStartBtn = findViewById(R.id.startMotionBtn);
        mStartBtn.setOnClickListener(this);
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
