package com.example.ek.motionchallenge.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.ek.motionchallenge.backend.FirebaseScoresDB;
import com.example.ek.motionchallenge.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * HomeScreen is the Main Activity of the application.
 * Allows user to select a motion
 */
public class HomeScreen extends BaseScreen
        implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private static final String TAG = "HomeScreen";
    private int mSelectedMotionIconID;
    private FirebaseScoresDB mScoresDB;

    private GoogleApiClient mGoogleApiClient;

    // Firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    // Motion Views
    private ImageView mShakeMotionIconView;
    private ImageView mSwing360MotionIconView;
    private ImageView mJumpUpMotionIconView;
    private ImageView mSpeedTapMotionIconView;
    private TextView mMotionDescView;
    private Button mStartBtn;

    /**
     * onClick() used to activate the selected motion
     * @param v motion view that was clicked by the user
     */
    @Override
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

    /**
     * Activates the icon for the selected motion and display the text in the Motion description view
     * @param motionIcon
     * @param motionDescStrID
     */
    private void activateMotionIcon(View motionIcon, int motionDescStrID){
        mShakeMotionIconView.clearColorFilter();
        mSwing360MotionIconView.clearColorFilter();
        mJumpUpMotionIconView.clearColorFilter();
        mSpeedTapMotionIconView.clearColorFilter();

        ImageView iconToActivate = (ImageView)motionIcon;

        mSelectedMotionIconID = motionIcon.getId();
        mMotionDescView.setText(motionDescStrID);
        iconToActivate.setColorFilter(0xFF33b5E5, PorterDuff.Mode.SCREEN);

        mStartBtn.setEnabled(true);
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
        mStartBtn.setEnabled(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Motion Challenge is starting...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //Initialize Auth
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser == null) {
            startActivity(new Intent(this, SignInScreen.class));
            finish();
            return;
        } else {
            if (mUser.getPhotoUrl() != null) {
                Log.d(TAG, "Photo URL=" + mUser.getPhotoUrl() );

                SimpleTarget target = new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        setProfilePhoto(bitmap);
                    }
                };

                Glide.with(this)
                        .load(mUser.getPhotoUrl())
                        .asBitmap()
                        .into(target);
            }
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        initViews();
        mScoresDB = FirebaseScoresDB.getInstance();
        mScoresDB.setUserLoginInfo(mUser.getUid(),mUser.getDisplayName());
    }

    /**
     * callback to create options menu
     * @param menu options menu for this screen
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    /**
     * options menu actions
     * @param item selection menu item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_sign_out) {
            mAuth.signOut();
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            startActivity(new Intent(this, SignInScreen.class));
            return true;
        }else if(id == R.id.menu_my_scores) {
            Intent myScoresScreenIntent = new Intent(this, MyScoresScreen.class);
            startActivity(myScoresScreenIntent);
        }else if(id == R.id.menu_leaderboard) {
            Intent leaderboardScreenIntent = new Intent(this, LeaderBoardScreen.class);
            startActivity(leaderboardScreenIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * callback when GoogleApiClient call fails. Displays a toast to notify the user that the error has occurred
     * @param connectionResult connection error description
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
}
