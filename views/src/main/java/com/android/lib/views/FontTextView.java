package com.android.lib.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.android.lib.tools.device.fonts.FontManager;

/**
 * Created by wb-liutiantian.h on 2016/3/25.
 */
public class FontTextView extends TextView {
    public FontTextView(Context context) {
        this(context, null);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Text);
        setFont(a.getString(R.styleable.Text_fontPath));
        a.recycle();
    }

    public void setFont(String fontPath) {
        Typeface font = FontManager.getInstance(getContext()).getFont(fontPath);
        if (font != null) {
            setTypeface(font);
        }
    }

    public void setFont(String fontPath, int style) {
        Typeface font = FontManager.getInstance(getContext()).getFont(fontPath);
        if (font != null) {
            setTypeface(font, style);
        }
    }
}
