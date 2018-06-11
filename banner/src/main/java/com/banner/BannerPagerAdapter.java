package com.banner;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.banner.interfaces.ViewPagerHolder;
import com.banner.interfaces.ViewPagerHolderCreator;
import com.banner.utils.BannerUtils;

import java.util.List;

/**
 * @author syb
 */

public class BannerPagerAdapter<T> extends PagerAdapter {

    private List<T> mDatas;
    private ViewPagerHolderCreator mCreator;
    private int count;


    public BannerPagerAdapter(@NonNull List<T> mDatas, ViewPagerHolderCreator creator) {
        this.mDatas = mDatas;
        this.count = mDatas.size();
        this.mCreator = creator;
    }

    @Override
    public int getCount() {
        if (mDatas == null) {
            return 0;
        }
        return mDatas.size() <= 1 ? mDatas.size() : Integer.MAX_VALUE;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //重点就在这儿了，不再是把布局写死，而是用接口提供的布局
        // 也不在这里绑定数据，数据绑定交给Api调用者。
        View view = getView(BannerUtils.getRealPosition(position, count), null, container);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


    /**
     * 获取viewPager 页面展示View
     *
     * @param position
     * @param view
     * @param container
     * @return
     */
    private View getView(int position, View view, ViewGroup container) {

        ViewPagerHolder holder = null;
        if (view == null) {
            //创建Holder
            holder = mCreator.createViewHolder();
            view = holder.onCreateView(container.getContext());
            view.setTag(holder);
        } else {
            holder = (ViewPagerHolder) view.getTag();
        }
        if (holder != null && mDatas != null && mDatas.size() > 0) {
            // 数据绑定
            holder.onBind(container.getContext(), position, mDatas.get(position));
        }

        return view;
    }

}
