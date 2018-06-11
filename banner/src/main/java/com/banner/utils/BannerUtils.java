package com.banner.utils;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * @author syb
 */

public class BannerUtils {
    public static int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public static int pxToDp(float px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, Resources.getSystem().getDisplayMetrics());
    }

    /**
     * 获取真实position
     * @param position
     * @param realCount
     * @return
     */
    public static int getRealPosition(int position, int realCount) {
        realCount = realCount == 0 ? 1 : realCount;
        return position % realCount;
    }
}
