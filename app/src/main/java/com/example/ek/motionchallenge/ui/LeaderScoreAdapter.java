package com.example.ek.motionchallenge.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ek.motionchallenge.model.LeaderScore;
import com.example.ek.motionchallenge.R;
import com.example.ek.motionchallenge.backend.FirebaseScoresDB;

/**
 * Created by ek on 12/5/17.
 * Based on https://stackoverflow.com/questions/32573800/android-how-to-add-icon-on-each-listview-list-item-and-change-the-text-color-bac
 * Author: Arsal Imam
 */

public class LeaderScoreAdapter extends BaseAdapter {
    private Context mContext;
    private LeaderScore[] mScores;
    protected FirebaseScoresDB mScoresDB;

    public LeaderScoreAdapter(Context context) {
        mContext = context;

        mScoresDB = FirebaseScoresDB.getInstance();

        String motionName = "shake";

        LeaderScore[] scores = new LeaderScore[4];
        scores[0] = new LeaderScore();
        scores[0].mUserName = "Eduard Kegulskiy";
        scores[0].mScores.put(motionName, mScoresDB.getMotionBestScore((motionName)));
        scores[0].mUserIcon = BaseScreen.mProfilePhoto;

        scores[1] = new LeaderScore();
        scores[1].mUserName = "Eduard Kegulskiy";
        scores[1].mScores.put(motionName, mScoresDB.getMotionBestScore((motionName)));

        scores[2] = new LeaderScore();
        scores[2].mUserName = "Eduard Kegulskiy";
        scores[2].mScores.put(motionName, mScoresDB.getMotionBestScore((motionName)));

        scores[3] = new LeaderScore();
        scores[3].mUserName = "Eduard Kegulskiy";
        scores[3].mScores.put(motionName, mScoresDB.getMotionBestScore((motionName)));

        mScores = scores;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return mScores.length;
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row;
        row = inflater.inflate(R.layout.leaderboard_row, parent, false);
        TextView title;
        TextView score;
        ImageView i1;
        i1 = (ImageView) row.findViewById(R.id.imgIcon);
        title = (TextView) row.findViewById(R.id.txtTitle);
        score = (TextView) row.findViewById(R.id.txtScore);

        String temp = new String("123");

        title.setText(mScores[position].mUserName);

        if(mScores[position].mUserIcon == null) {
            i1.setImageResource(R.drawable.ic_default_user);
        }else
            i1.setImageBitmap(mScores[position].mUserIcon);

        score.setText(mScores[position].mScores.get("shake").toString());
        return (row);
    }
}
