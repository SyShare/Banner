package com.banner.interfaces;

import android.content.Context;
import android.view.View;

/**
 * @author syb
 * @date 2018/3/5
 */

public interface ViewPagerHolder<T> {

    /**
     * 创建
     *
     * @param context
     * @return
     */
    View onCreateView(Context context);

    /**
     * 绑定数据
     * @param context
     * @param position
     * @param data
     */
    void onBind(Context context, int position, T data);
}
