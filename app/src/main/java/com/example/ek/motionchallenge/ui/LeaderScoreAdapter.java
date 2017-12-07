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
 * Created by Eduard Kegulskiy on 12/5/17.
 * Based on https://stackoverflow.com/questions/32573800/android-how-to-add-icon-on-each-listview-list-item-and-change-the-text-color-bac
 * Author: Arsal Imam
 *
 * Data adapter used by LeaderBoardScreen
 */
public class LeaderScoreAdapter extends BaseAdapter {
    private Context mContext;
    private LeaderScore[] mScores;
    protected FirebaseScoresDB mScoresDB;
    private final int NUMBER_OF_LEADERS = 5;

    public LeaderScoreAdapter(Context context) {
        mContext = context;

        mScoresDB = FirebaseScoresDB.getInstance();

        // TODO: this is work-in-progress, needs to get real data from FireBaseDB object
        // TODO: need to implmement this for each motion. For now hardcodeds "shake" motion only
        String motionName = "shake";

        LeaderScore[] scores = new LeaderScore[NUMBER_OF_LEADERS];
        scores[0] = new LeaderScore();
        scores[0].mUserName = "Test User 1";
        scores[0].mScores.put(motionName, 100);

        scores[1] = new LeaderScore();
        scores[1].mUserName = "Test User 2";
        scores[1].mScores.put(motionName, 90);

        scores[2] = new LeaderScore();
        scores[2].mUserName = "Eduard Kegulskiy";
        scores[2].mScores.put(motionName, mScoresDB.getMotionBestScore((motionName)));
        scores[2].mUserIcon = BaseScreen.sProfilePhoto;

        scores[3] = new LeaderScore();
        scores[3].mUserName = "Test User 3";
        scores[3].mScores.put(motionName, 10);

        scores[4] = new LeaderScore();
        scores[4].mUserName = "Test User 4";
        scores[4].mScores.put(motionName, 2);

        mScores = scores;
    }

    /**
     * gets number of rows in the data set
     * @return
     */
    public int getCount() {
        return mScores.length;
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * gets the view for each data row
     * @param position data row index
     * @param convertView view
     * @param parent parent
     * @return new row created for this data
     */
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

        title.setText(mScores[position].mUserName);

        if(mScores[position].mUserIcon == null) {
            i1.setImageResource(R.drawable.ic_default_user);
        }else
            i1.setImageBitmap(mScores[position].mUserIcon);

        score.setText(mScores[position].mScores.get("shake").toString());
        return (row);
    }
}