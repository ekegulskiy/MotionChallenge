package com.example.ek.motionchallenge.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ek.motionchallenge.R;
import com.example.ek.motionchallenge.backend.FirebaseScoresDB;

/**
 * Created by ek on 12/6/17.
 * Motion scores data adapter used by the MyScores Screen
 */
public class MotionScoreAdapter extends BaseAdapter {
    private Context mContext;
    private final int MOTION_COUNT = 4;
    protected FirebaseScoresDB mScoresDB;

    /**
     * Constructor
     * @param context context of the activity instating this class
     */
    public MotionScoreAdapter(Context context) {
        mContext = context;
        mScoresDB = FirebaseScoresDB.getInstance();
    }

    /**
     * number of rows for the data view
     * @return
     */
    public int getCount() {
        return MOTION_COUNT + 1 /* 1 for the header */;
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
        row = inflater.inflate(R.layout.myscores_row, parent, false);

        if(position == 0)
            setupHeaderRow(row);
        else
            setupMotionRow(position, row);

        return (row);
    }

    protected void setupHeaderRow(View row){
        TextView motionName = (TextView) row.findViewById(R.id.motionName);
        motionName.setText("Motion");
        motionName.setTypeface(null, Typeface.BOLD);

        TextView myScoreTitle = (TextView) row.findViewById(R.id.myScore);
        myScoreTitle.setText("My Score");
        myScoreTitle.setTypeface(null, Typeface.BOLD);


        TextView bestScore = (TextView) row.findViewById(R.id.bestScore);
        bestScore.setText("Best Score");
        bestScore.setTypeface(null, Typeface.BOLD);
    }

    private void setupMotionRow(int position, View row){
        TextView motionNameView = (TextView) row.findViewById(R.id.motionName);
        TextView myScoreTitleView = (TextView) row.findViewById(R.id.myScore);
        TextView bestScoreView = (TextView) row.findViewById(R.id.bestScore);

        String motionName = "";
        switch(position){
            case 1:
                motionName = "shake";
                break;
            case 2:
                motionName = "360swing";
                break;
            case 3:
                motionName = "jumpUp";
                break;
            case 4:
                motionName = "speedTap";
                break;
            default:
                assert false;
        }

        if(!motionName.equals("")) {
            motionNameView.setText(motionName);

            Integer userBestScore = mScoresDB.getMotionBestScore(motionName);
            myScoreTitleView.setText(userBestScore.toString());

            Integer overallBestScore = mScoresDB.getOverallMaxScore(motionName);
            bestScoreView.setText(overallBestScore.toString());
       }
    }
}
