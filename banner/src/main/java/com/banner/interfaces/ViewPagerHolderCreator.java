package com.banner.interfaces;

/**
 *
 * @author syb
 * @date 2018/3/5
 */

public interface ViewPagerHolderCreator<VH extends ViewPagerHolder>{

    /**
     * 创建一个ViewHolder
     * @return
     */
     VH createViewHolder();
}
