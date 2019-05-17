package com.eb.geaiche.util;

import android.content.res.Resources;

public class DimensionUtil {
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
