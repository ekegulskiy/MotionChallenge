package com.example.ek.motionchallenge.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created by ek on 11/26/17.
 * Built on top of
 * snippet from https://stackoverflow.com/questions/18410984/android-displaying-text-in-center-of-progress-bar by Yogesh Rathi
 */

public class TextProgressBar extends ProgressBar {
    private Paint textPaint;
    private String mMinValueLabel;
    private String mMaxValueLabel;
    private String mCurValueLabel;
    private String mTitleLabel;
    private int mMinValue;

    private void init(){
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(35.0f);

        mMinValueLabel = "";
        mMaxValueLabel = "";
        mCurValueLabel = "";
        mTitleLabel = "";
    }

    public TextProgressBar(Context context) {
        super(context);
        init();
    }

    public TextProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        // First draw the regular progress bar, then custom draw our text
        super.onDraw(canvas);

        Rect bounds = new Rect();
        textPaint.getTextBounds(mCurValueLabel, 0, mCurValueLabel.length(), bounds);
        Integer curProgress = getProgress();
        Integer range = getMax() - mMinValue;
        double curProgressInRange = curProgress.doubleValue()/range.doubleValue();

        Integer width = getWidth()-getPaddingRight()-getPaddingLeft();

        Double curPos = width.doubleValue() * curProgressInRange;

        int x = getPaddingLeft()+curPos.intValue() - bounds.centerX();
        int y = 15 - bounds.centerY();
        canvas.drawText(mCurValueLabel, x, y, textPaint);

        textPaint.getTextBounds(mTitleLabel, 0, mTitleLabel.length(), bounds);
        x = 0;
        y = getHeight() / 2 - bounds.centerY();
        canvas.drawText(mTitleLabel, x, y, textPaint);

   //     textPaint.getTextBounds(mMinScores, 0, mMinScores.length(), bounds);
        x = bounds.width() + 80;
        y = getHeight() / 2 - bounds.centerY();
        canvas.drawText(mMinValueLabel, x, y, textPaint);

        textPaint.getTextBounds(mMaxValueLabel, 0, mMaxValueLabel.length(), bounds);
        x = getWidth()- 80;
        y = getHeight() / 2 - bounds.centerY();
        canvas.drawText(mMaxValueLabel, x, y, textPaint);

    }

    @Override
    public synchronized void setMin(int min) {
 //       super.setMin(min);
        mMinValueLabel = Integer.toString(min);
        mMinValue = min;
    }

    @Override
    public synchronized void setMax(int max) {
        super.setMax(max);
        mMaxValueLabel = Integer.toString(max);
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        mCurValueLabel = Integer.toString(progress);
    }

    public synchronized void setTitle(String title) {
        this.mTitleLabel = title;
        drawableStateChanged();
    }

    public void setTextColor(int color) {
        textPaint.setColor(color);
        drawableStateChanged();
    }
}
