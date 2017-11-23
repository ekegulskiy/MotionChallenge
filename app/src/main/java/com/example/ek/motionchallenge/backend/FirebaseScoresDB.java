package com.example.ek.motionchallenge.backend;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Created by ek on 11/22/17.
 */

public class FirebaseScoresDB {
    private final String TAG = "FirebaseScoresDB";
    private final String DB_KEY_MOTIONS = "motions";
    private final String DB_KEY_USER_NAME = "name";
    private final String DB_KEY_USERS = "users";
    private final String DB_KEY_LAST_SCORE = "lastScore";
    private final String DB_KEY_BEST_SCORE = "bestScore";

    private String mUserName;
    private String mUserUUID;
    private DatabaseReference mFirebaseDB;
    private HashMap<String, Integer> mLastScores = new HashMap<String, Integer>();
    private HashMap<String, Integer> mBestScores = new HashMap<String, Integer>();

    private static FirebaseScoresDB mSingleton;

    private FirebaseScoresDB(){
           mFirebaseDB = FirebaseDatabase.getInstance().getReference();
    }

    public static FirebaseScoresDB getInstance(){
        if(mSingleton == null)
            mSingleton = new FirebaseScoresDB();

        return mSingleton;
    }

    private void setupListeners(){
        mFirebaseDB.child(DB_KEY_USERS).child(mUserUUID).child(DB_KEY_MOTIONS).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded");

                String key = dataSnapshot.getKey(); // motion name
                HashMap<String, Long> scores = (HashMap<String, Long>)dataSnapshot.getValue();

                mLastScores.put(key, scores.get(DB_KEY_LAST_SCORE).intValue());
                mBestScores.put(key, scores.get(DB_KEY_BEST_SCORE).intValue());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void setUserLoginInfo(String userUUID, String userName){
        mUserName = userName;
        mUserUUID = userUUID;
        setupListeners();

        mFirebaseDB.child(DB_KEY_USERS).child(userUUID).child(DB_KEY_USER_NAME).setValue(userName);
    }

    public void updateMotionScore(String motionName, int score){
        mLastScores.put(motionName, score);

        if(mBestScores.containsKey(motionName)) {
            Object x = mBestScores.get(motionName);

            if(mBestScores.get(motionName) < score) {
                mBestScores.put(motionName, score); // update with new best score
                mFirebaseDB.child(DB_KEY_USERS).child(mUserUUID).child(DB_KEY_MOTIONS).child(motionName).child(DB_KEY_BEST_SCORE).setValue(score);
            }
        }else{
            mBestScores.put(motionName,score); // update the best score for the first time
            mFirebaseDB.child(DB_KEY_USERS).child(mUserUUID).child(DB_KEY_MOTIONS).child(motionName).child(DB_KEY_BEST_SCORE).setValue(score);
        }

        mFirebaseDB.child(DB_KEY_USERS).child(mUserUUID).child(DB_KEY_MOTIONS).child(motionName).child(DB_KEY_LAST_SCORE).setValue(score);
    }

    public int getMotionLastScore(String motionName){
        return mLastScores.get(motionName);
    }

    public int getMotionBestScore(String motionName){
        return mBestScores.get(motionName);
    }
}
