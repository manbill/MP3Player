package com.android.lib.tools.device.fonts;

import android.content.Context;
import android.graphics.Typeface;

import com.android.lib.tools.log.MyLog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wb-liutiantian.h on 2016/3/25.
 */
public class FontManager {
    private static FontManager mInstance;

    public static FontManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (FontManager.class) {
                if (mInstance == null) {
                    mInstance = new FontManager(context);
                }
            }
        }
        return mInstance;
    }

    private Context context;
    private Map<String, Typeface> typefaces;

    private FontManager(Context context) {
        this.context = context;
        this.typefaces = new HashMap<>();
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Typeface getFont(String assetPath) {
        MyLog.i("FontManager", "getFont.1", "assetPath: " + assetPath);
        if (assetPath == null || assetPath.isEmpty()) {
            return null;
        }
        if ((!assetPath.endsWith(".ttf") && !assetPath.endsWith(".ttc"))) {
            assetPath = assetPath + ".ttf";
        }
        MyLog.i("FontManager", "getFont.2", "assetPath: " + assetPath);
        if (this.typefaces.containsKey(assetPath)) {
            return this.typefaces.get(assetPath);
        }
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), assetPath);
        if (font != null) {
            this.typefaces.put(assetPath, font);
            return font;
        }
        return null;
    }
}
