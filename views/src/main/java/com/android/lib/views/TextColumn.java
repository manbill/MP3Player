package com.android.lib.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.android.lib.tools.device.fonts.FontManager;
import com.android.lib.tools.handler.MessageManager;
import com.android.lib.tools.log.MyLog;

/**
 * Created by liutiantian on 2016-04-03.
 */
public class TextColumn extends View {
    private static final String TAG = "TextColumn";

    private int textSize;
    private int textColor;
    private String text;
    private String fontPath;
    private int lineSpacingExtra;

    private TextPaint mPaint;
    private int scrollHeight;
    private LoopHandler mLoopHandler;
    private int measuredWidth;
    private int measuredHeight;
    private boolean shouldLoop;
    private Canvas mBackCanvas;
    private Bitmap mBackBitmap;

    public TextColumn(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Text, 0, 0);
        this.textSize = a.getDimensionPixelSize(R.styleable.Text_textSize, 24);
        this.textColor = a.getColor(R.styleable.Text_textColor, 0XFFFFFFFF);
        this.text = a.getString(R.styleable.Text_text);
        this.fontPath = a.getString(R.styleable.Text_fontPath);
        this.lineSpacingExtra = a.getDimensionPixelSize(R.styleable.Text_lineSpacingExtra, 0);
        a.recycle();

        mPaint = new TextPaint();
        mPaint.setTextSize(this.textSize);
        mPaint.setColor(this.textColor);
        if (this.fontPath != null && FontManager.getInstance(getContext()).getFont(this.fontPath) != null) {
            mPaint.setTypeface(FontManager.getInstance(getContext()).getFont(this.fontPath));
        }
        mLoopHandler = new LoopHandler(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        this.measuredWidth = measureWidth();
        this.measuredHeight = measureHeight();

        int finalWidth = 0;
        int finalHeight = 0;
        if (widthMode == MeasureSpec.EXACTLY) {
            finalWidth = width;
        }
        else if (widthMode == MeasureSpec.AT_MOST) {
            finalWidth = Math.min(width, this.measuredWidth);
        }
        else if (widthMode == MeasureSpec.UNSPECIFIED) {
            finalWidth = this.measuredWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            finalHeight = height;
        }
        else if (heightMode == MeasureSpec.AT_MOST) {
            finalHeight = Math.min(height, this.measuredHeight);
        }
        else if (heightMode == MeasureSpec.UNSPECIFIED) {
            finalHeight = this.measuredHeight;
        }

        setMeasuredDimension(finalWidth, finalHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        MyLog.i("TextColumn", "onDraw", "this.text != null && !this.text.isEmpty(): " + (this.text != null && !this.text.isEmpty()) + "; scrollHeight: " + this.scrollHeight);
        super.onDraw(canvas);
        if (this.text != null && !this.text.isEmpty()) {
            int width = getWidth();
            int height = getHeight();
            if (width < this.measuredWidth) {
                throw new IllegalStateException("view does not has enough space to draw its content.");
            }

            if (mBackBitmap == null || mBackBitmap.getWidth() != width || mBackBitmap.getHeight() != height) {
                mBackBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                mBackCanvas = new Canvas(mBackBitmap);
            }

            if (height < this.measuredHeight) {
                this.shouldLoop = true;
                if (!MessageManager.getInstance().containsHandler(LoopHandler.class)) {
                    MessageManager.getInstance().addHandler(LoopHandler.class, mLoopHandler);
                    MessageManager.getInstance().removeMessages(LoopHandler.class, LoopHandler.MARQUEE_TICK);
                    MessageManager.getInstance().sendMessageDelayed(LoopHandler.class, LoopHandler.MARQUEE_TICK, System.currentTimeMillis(), 100);
                }
            }

            mBackCanvas.save();
            if (getBackground() != null) {
                getBackground().draw(mBackCanvas);
            }
            else {
                mBackBitmap.eraseColor(Color.TRANSPARENT);
            }
            mBackCanvas.clipRect(getPaddingLeft(), getPaddingTop(), width - getPaddingRight(), height - getPaddingBottom());

            for (int  i = 0; i < this.text.length(); ++i) {
                float charWidth = mPaint.measureText(this.text, i, i + 1);
                if (getLayoutDirection() == LAYOUT_DIRECTION_RTL) {
                    mBackCanvas.drawText(this.text, i, i + 1, (width - getPaddingRight() - getPaddingLeft() - charWidth) / 2 + getPaddingRight(), getPaddingTop() + (this.shouldLoop ? 0 : (height - this.measuredHeight) / 2) + this.scrollHeight + this.textSize * (i + 1) + this.lineSpacingExtra * i, mPaint);
                }
                else {
                    mBackCanvas.drawText(this.text, i, i + 1, (width - getPaddingLeft() - getPaddingRight() - charWidth) / 2 + getPaddingLeft(), getPaddingTop() + (this.shouldLoop ? 0 : (height - this.measuredHeight) / 2) + this.scrollHeight + this.textSize * (i + 1) + this.lineSpacingExtra * i, mPaint);
                }
            }

            mBackCanvas.restore();

            canvas.drawBitmap(mBackBitmap, 0, 0, null);
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        MyLog.i(TAG, "onAttachedToWindow", "shouldLoop: " + this.shouldLoop);
        if (this.shouldLoop) {
            MessageManager.getInstance().addHandler(LoopHandler.class, mLoopHandler);
        }
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        MyLog.i(TAG, "onWindowFocusChanged", "hasWindowFocus: " + hasWindowFocus);
        if (hasWindowFocus) {
            if (this.shouldLoop) {
                MessageManager.getInstance().sendMessageDelayed(LoopHandler.class, LoopHandler.MARQUEE_TICK, System.currentTimeMillis(), 100);
            }
        }
        else {
            if (this.shouldLoop) {
                MessageManager.getInstance().removeMessages(LoopHandler.class, LoopHandler.MARQUEE_TICK);
                this.scrollHeight = 0;
                invalidate();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        MyLog.i(TAG, "onDetachedFromWindow", "shouldLoop: " + this.shouldLoop);
        if (this.shouldLoop) {
            this.shouldLoop = false;
            MessageManager.getInstance().removeHandler(LoopHandler.class);
        }
    }

    private int measureWidth() {
        if (this.text == null || this.text.isEmpty()) {
            return getPaddingStart() + getPaddingEnd();
        }
        else {
            float width = 0;
            for (int i = 0; i < this.text.length(); ++i) {
                float measuredWidth = mPaint.measureText(this.text, i, i + 1);
                if (width < measuredWidth) {
                    width = measuredWidth;
                }
            }
            return Math.round(width);
        }
    }

    private int measureHeight() {
        if (this.text == null || this.text.isEmpty()) {
            return getPaddingTop() + getPaddingBottom();
        }
        else {
            return getPaddingTop() + this.text.length() * this.textSize + (this.text.length() - 1) * this.lineSpacingExtra + getPaddingBottom();
        }
    }

    public int getTextSize() {
        return this.textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        mPaint.setTextSize(this.textSize);
        requestLayout();
    }

    public int getTextColor() {
        return this.textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        mPaint.setColor(this.textColor);
        requestLayout();
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
        requestLayout();
    }

    public String getFontPath() {
        return this.fontPath;
    }

    public void setFontPath(String fontPath) {
        this.fontPath = fontPath;
        if (FontManager.getInstance(getContext()).getFont(this.fontPath) != null) {
            mPaint.setTypeface(FontManager.getInstance(getContext()).getFont(this.fontPath));
            requestLayout();
        }
    }

    private static class LoopHandler extends MessageManager.BaseHandler<TextColumn> {
        protected static final int MARQUEE_TICK = 0;

        public LoopHandler(TextColumn host) {
            super(host);
        }

        @Override
        protected boolean validHost(TextColumn host) {
            return host != null && host.getWindowToken() != null;
        }

        @Override
        public void handleMessage(Message msg) {
            TextColumn column = getHost();
            if (column != null) {
                if (msg.what == MARQUEE_TICK) {
                    long lastTime = (long) msg.obj;
                    long curTime = System.currentTimeMillis();
                    column.scrollHeight -= (curTime - lastTime) / 50;
                    column.invalidate();
                    int desiredHeight = column.measuredHeight;
                    if (column.scrollHeight + desiredHeight < 0) {
                        column.scrollHeight = 0;
                    }
                    MessageManager.getInstance().sendMessageDelayed(LoopHandler.class, LoopHandler.MARQUEE_TICK, curTime, 100);
                }
            }
        }
    }
}
