package com.muzzlyworld.middleandroid.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import androidx.annotation.AttrRes;

public final class ThemeUtils {

    private ThemeUtils() {}

    public static int getThemeColor(Context context, @AttrRes int themeAttrId) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(themeAttrId, typedValue, true);
        return typedValue.data;
    }

}
