package com.android.lib.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Locale;

/**
 * Created by wb-liutiantian.h on 2016/3/11.
 */
public class TimeStatusView extends View {
    private int status;
    private int blockNum;
    private String startTime;
    private String endTime;
    private int period;
    private float textSize;
    private int blockColor;
    private int textColor;
    private int lineColor;
    private Paint mPaint;

    public TimeStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Text, 0, 0);
        this.textSize = a.getDimensionPixelSize(R.styleable.Text_textSize, 24);
        this.textColor = a.getColor(R.styleable.Text_textColor, 0xff666666);
        a.recycle();

        TypedArray b = context.obtainStyledAttributes(attrs, R.styleable.TimeStatusView, 0, 0);
        this.status = b.getInt(R.styleable.TimeStatusView_status, 0);
        this.startTime = b.getString(R.styleable.TimeStatusView_start_time);
        this.endTime = b.getString(R.styleable.TimeStatusView_end_time);
        this.period = b.getInt(R.styleable.TimeStatusView_period, 30);
        this.blockColor = b.getColor(R.styleable.TimeStatusView_block_color, 0xffc9c9c9);
        this.lineColor = b.getColor(R.styleable.TimeStatusView_line_color, 0xffa0a0a0);
        b.recycle();

        validateData();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(this.textSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureHeight(int measureSpec){
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int designedHeight = Math.round(64 + getTextSize());

        switch (specMode) {
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                result = getPaddingTop() + designedHeight + getPaddingBottom();
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(getPaddingTop() + designedHeight + getPaddingBottom(), specSize);
                break;
        }
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (validateData()) {
            if (getBackground() != null) {
                getBackground().draw(canvas);
            }

            mPaint.setStrokeWidth(1.0f);

            int verticalLineHeight = Math.round(getHeight() - getPaddingTop() - getTextSize() - getPaddingBottom());
            float perItemWidth = (getWidth() - this.blockNum - getPaddingStart() - getPaddingEnd() - 1f) / this.blockNum;

            for(int i = 0; i < this.blockNum; ++i) {
                float startX =  getPaddingStart() + (perItemWidth * (i)) + i;
                float startNextX = getPaddingStart() + (perItemWidth * (i + 1)) + i;

                if((this.status & (1 << i)) != 0){
                    mPaint.setColor(this.blockColor);
                    canvas.drawRect(startX, getPaddingTop(), startNextX + 1f, getPaddingTop() + verticalLineHeight, mPaint);
                }

                mPaint.setColor(this.lineColor);
                canvas.drawLine(startX, getPaddingTop(), startX, getPaddingTop() + verticalLineHeight - 1f, mPaint);

                if ((i * this.period + getMinutes(this.startTime)) % 60 == 0) {
                    mPaint.setColor(this.textColor);
                    mPaint.setTextSize(this.textSize);
                    canvas.drawText(String.format(Locale.CHINA, "%d", (i * this.period + getMinutes(this.startTime)) / 60), startX, getPaddingTop() + verticalLineHeight + getTextSize(), mPaint);
                }
            }
            mPaint.setColor(this.lineColor);
            canvas.drawLine(getWidth() - getPaddingEnd() - 1f, getPaddingTop(), getWidth() - getPaddingEnd() - 1f, getPaddingTop() + verticalLineHeight - 1f, mPaint);

            mPaint.setColor(this.lineColor);
            canvas.drawLine(getPaddingStart(), getPaddingTop(), getWidth() - getPaddingEnd(), getPaddingTop(), mPaint);
            canvas.drawLine(getPaddingStart(), getPaddingTop() + verticalLineHeight, getWidth() - getPaddingEnd(), getPaddingTop() + verticalLineHeight, mPaint);
        }
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
        if (validateData()) {
            requestLayout();
        }
    }

    public int getBlockNum() {
        return this.blockNum;
    }

    public float getTextSize() {
        return this.textSize;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
        if (validateData()) {
            requestLayout();
        }
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
        if (validateData()) {
            requestLayout();
        }
    }

    public int getMinutes(String time) {
        if (time == null || time.isEmpty()) {
            return 0;
        }
        if (time.matches("\\d{2}:\\d{2}")) {
            String[] times = time.split(":");
            return Integer.parseInt(times[0]) * 60 + Integer.parseInt(times[1]);
        }
        return 0;
    }

    public String toTime(int minutes) {
        return String.format(Locale.CHINA, "%02d:%02d", minutes / 60, minutes % 60);
    }

    private boolean validateData() {
        if (this.startTime != null && this.endTime != null) {
            if (!this.startTime.matches("\\d{2}:\\d{2}")) {
                return false;
            }

            if (!this.endTime.matches("\\d{2}:\\d{2}")) {
                return false;
            }

            int startTime = getMinutes(this.startTime);
            int endTime = getMinutes(this.endTime);
            if (startTime > endTime) {
                return false;
            }

            if ((endTime - startTime) % this.period != 0) {
                return false;
            }

            this.blockNum = (endTime - startTime) / this.period;

            return true;
        }
        return false;
    }
}
