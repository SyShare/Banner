package com.banner.interfaces;

/**
 *
 * @author syb
 */

public interface ViewPagerHolderCreator<VH extends ViewPagerHolder>{

    /**
     * 创建一个ViewHolder
     * @return
     */
     VH createViewHolder();
}
