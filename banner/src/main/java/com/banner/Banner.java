package com.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.banner.IndicatorView.CircleIndicatorView;
import com.banner.interfaces.ViewPagerHolderCreator;
import com.banner.listener.BannerPageChangeListener;
import com.banner.listener.OnBannerListener;
import com.banner.utils.BannerConfig;
import com.banner.utils.BannerUtils;
import com.banner.view.BannerViewPager;

import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * @author syb
 */
public class Banner extends FrameLayout implements ViewPager.OnPageChangeListener {
    public static final String TAG = Banner.class.getSimpleName();

    /**
     * 数据为1时，隐藏
     */
    private boolean isIndicatorViewLimit = false;
    /**
     * ViewPager是否滑动禁止
     */
    private boolean scrollable = BannerConfig.IS_SCROLL;

    /**
     * 延迟时间
     */
    private int delayTime = BannerConfig.DELAY_TIME;
    /**
     * 滚动事件
     */
    private int scrollTime = BannerConfig.DURATION;
    /**
     * 自动轮播标志
     */
    private boolean isAutoPlay = BannerConfig.IS_AUTO_PLAY;
    /**
     * 默认布局id
     */
    private int mLayoutResId = R.layout.banner;
    /**
     * 指示器是否显示
     */
    private boolean indicatorViewVisible;
    /**
     * 当前位置
     */
    private int currentItem;
    /**
     * 数据集合
     */
    private List imageUrls;
    /**
     * viewPager
     */
    private BannerViewPager viewPager;
    /**
     * 指示器
     */
    private CircleIndicatorView circleIndicatorView;
    /**
     * 数据适配
     */
    private BannerPagerAdapter adapter;
    /**
     * 滚动Scroller
     */
    private BannerScroller mScroller;
    /**
     * 点击监听
     */
    private OnBannerListener listener;

    /**
     * ViewPager监听事件
     */
    private BannerPageChangeListener pageChangeListener;

    /**
     * 数据集合长度
     */
    private int count;
    /**
     * item构建
     */
    private ViewPagerHolderCreator creator;

    /**
     * item是否构建完成
     */
    private boolean isInitCreator;

    private ImageHandler handler = null;


    public Banner(Context context) {
        this(context, null);
    }

    public Banner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        imageUrls = new ArrayList<>();
        initView(context, attrs);
    }

    /**
     * 初始化view
     *
     * @param context
     * @param attrs
     */
    private void initView(Context context, AttributeSet attrs) {
        initTypedArray(context, attrs);
        View view = LayoutInflater.from(context).inflate(mLayoutResId, this, true);
        viewPager = (BannerViewPager) view.findViewById(R.id.bannerViewPager);
        circleIndicatorView = (CircleIndicatorView) view.findViewById(R.id.circleIndicatorView);
        initViewPagerScroll();
        initBindViewPager();
    }

    /**
     * 关联ViewPager
     */
    private void initBindViewPager() {
        handler = new ImageHandler(new SoftReference<>(this));
        handler.setMsgDelayTime(delayTime);
        viewPager.setScrollable(scrollable);
        if (circleIndicatorView != null) {
            circleIndicatorView.setVisibility(indicatorViewVisible ? VISIBLE : GONE);
            if (!indicatorViewVisible) return;
            circleIndicatorView.setUpWithViewPager(viewPager);
        }
    }

    /**
     * 解析 TypedArray
     *
     * @param context
     * @param attrs
     */
    private void initTypedArray(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Banner);
        isIndicatorViewLimit = typedArray.getBoolean(R.styleable.Banner_indicator_limit, false);
        delayTime = typedArray.getInt(R.styleable.Banner_delay_time, BannerConfig.DELAY_TIME);
        scrollTime = typedArray.getInt(R.styleable.Banner_scroll_time, BannerConfig.DURATION);
        isAutoPlay = typedArray.getBoolean(R.styleable.Banner_is_auto_play, BannerConfig.IS_AUTO_PLAY);
        mLayoutResId = typedArray.getResourceId(R.styleable.Banner_banner_layout, mLayoutResId);
        scrollable = typedArray.getBoolean(R.styleable.Banner_scrollable, BannerConfig.IS_SCROLL);
        indicatorViewVisible = typedArray.getBoolean(R.styleable.Banner_indicator_view_visible, BannerConfig.IS_DEFAULT_INDICATOR_VIEW_VISIBLE);
        typedArray.recycle();
    }

    /**
     * 配置 mScroller
     */
    private void initViewPagerScroll() {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new BannerScroller(viewPager.getContext());
            mScroller.setDuration(scrollTime);
            mField.set(viewPager, mScroller);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return imageUrls == null || imageUrls.isEmpty();
    }

    /**
     * 是否创建creator
     *
     * @return
     */
    public boolean isInitCreator() {
        return isInitCreator;
    }

    /**
     * 获取当前ViewPager引用
     *
     * @return
     */
    public BannerViewPager getViewPager() {
        return viewPager;
    }

    /**
     * 是否自动开始
     *
     * @param isAutoPlay
     * @return
     */
    public Banner isAutoPlay(boolean isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
        return this;
    }

    /**
     * 设置时间
     *
     * @param delayTime
     * @return
     */
    public Banner setDelayTime(int delayTime) {
        this.delayTime = delayTime;
        return this;
    }

    /**
     * 设置是否允许触发事件
     *
     * @param scrollable
     * @return
     */
    public Banner setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
        return this;
    }

    /**
     * 指示器显示模式
     *
     * @param fillMode
     * @return
     */
    public Banner setIndicatorFillMode(CircleIndicatorView.FillMode fillMode) {
        if (!indicatorViewVisible) {
            return this;
        }
        if (circleIndicatorView == null) {
            throw new IllegalStateException("you must init CirCleIndicatorView before using this method");
        }
        circleIndicatorView.setFillMode(fillMode);
        return this;
    }

    /**
     * 设置 PageTransformer
     *
     * @param transformer
     * @return
     */
    public Banner setBannerAnimation(Class<? extends ViewPager.PageTransformer> transformer) {
        try {
            setPageTransformer(true, transformer.newInstance());
        } catch (Exception e) {
            Log.e(TAG, "Please set the PageTransformer class");
        }
        return this;
    }

    /**
     * Set the number of pages that should be retained to either side of the
     * current page in the view hierarchy in an idle state. Pages beyond this
     * limit will be recreated from the adapter when needed.
     *
     * @param limit How many pages will be kept offscreen in an idle state.
     * @return Banner
     */
    public Banner setOffscreenPageLimit(int limit) {
        if (viewPager != null) {
            viewPager.setOffscreenPageLimit(limit);
        }
        return this;
    }

    /**
     * Set a ViewPager.PageTransformer that will be called for each attached page whenever
     * the scroll position is changed. This allows the application to apply custom property
     * transformations to each page, overriding the default sliding look and feel.
     *
     * @param reverseDrawingOrder true if the supplied PageTransformer requires page views
     *                            to be drawn from last to first instead of first to last.
     * @param transformer         PageTransformer that will modify each page's animation properties
     * @return Banner
     */
    public Banner setPageTransformer(boolean reverseDrawingOrder, ViewPager.PageTransformer transformer) {
        viewPager.setPageTransformer(reverseDrawingOrder, transformer);
        return this;
    }

    /**
     * Set the margin between pages.
     *
     * @param marginPixels Distance between adjacent pages in pixels
     */
    public Banner setPageMargin(int marginPixels) {
        viewPager.setPageMargin(marginPixels);
        return this;
    }

    /**
     * 设置数据源
     *
     * @param imageUrls
     * @return
     */
    public Banner setData(List<?> imageUrls) {
        this.imageUrls.clear();
        this.imageUrls.addAll(imageUrls);
        this.count = imageUrls.size();
        return this;
    }

    /**
     * 设置item构造器
     *
     * @param creator
     * @return
     */
    public Banner setViewPagerHolder(ViewPagerHolderCreator creator) {
        isInitCreator = true;
        this.creator = creator;
        return this;
    }

    /**
     * 开始start
     *
     * @return
     */
    public Banner start() {
        setData();
        return this;
    }

    /**
     * 更新数据
     *
     * @param imageUrls
     */
    public void update(List<?> imageUrls) {
        this.imageUrls.clear();
        this.imageUrls.addAll(imageUrls);
        this.count = this.imageUrls.size();
        start();
    }

    /**
     * 设置数据并开始
     */
    private void setData() {
        if (adapter == null) {
            adapter = new BannerPagerAdapter(imageUrls, creator);
            viewPager.addOnPageChangeListener(this);
        }
        viewPager.setAdapter(adapter);
        viewPager.setFocusable(true);
        setCircleIndicatorCount(count);
        if (count > 1) {
            viewPager.setScrollable(true);
        } else {
            viewPager.setScrollable(false);
        }
        setCurrentItem();
    }

    /**
     * 解决ANR问题
     */
    private void setCurrentItem() {
        try {
            Field mFirstLayout = ViewPager.class.getDeclaredField("mFirstLayout");
            mFirstLayout.setAccessible(true);
            mFirstLayout.set(viewPager, true);
            adapter.notifyDataSetChanged();
            ///阻塞主线程。
            currentItem = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % imageUrls.size();
            viewPager.setCurrentItem(currentItem);
            startAutoPlay();
        } catch (Exception ignore) {

        }
    }

    /**
     * 设置指示器点数
     *
     * @param count
     */
    private void setCircleIndicatorCount(int count) {
        if (isIndicatorViewLimit && count == 1) {
            return;
        }
        if (circleIndicatorView != null && indicatorViewVisible) {
            circleIndicatorView.setCount(count);
        }
    }

    /**
     * onstart 开始
     */
    public void startAutoPlay() {
        startAutoPlay(ImageHandler.MSG_DELAY_SHORT);
    }

    /**
     * 开始播放
     */
    private void startAutoPlay(long delayTime) {
        if (!isAutoPlay || count <= 1) {
            return;
        }
        stopAutoPlay();
        if (handler != null) {
            handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, delayTime);
        }
    }

    /**
     * 停止自动轮播
     */
    public void stopAutoPlay() {
        if (!isAutoPlay || count <= 1) {
            return;
        }
        if (handler != null && handler.hasMessages(ImageHandler.MSG_UPDATE_IMAGE)) {
            handler.removeMessages(ImageHandler.MSG_UPDATE_IMAGE);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopAutoPlay();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                startAutoPlay(delayTime);
                break;
            default:
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE:
                startAutoPlay(delayTime);
                break;
            default:
                break;
        }
        if (pageChangeListener != null) {
            pageChangeListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (pageChangeListener != null) {
            pageChangeListener.onPageScrolled(BannerUtils.getRealPosition(position, count), positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        currentItem = position;
        updateCurrentPosition(position);
        if (pageChangeListener != null) {
            pageChangeListener.onPageSelected(BannerUtils.getRealPosition(position, count));
        }
    }

    /**
     * 更新当前位置信息
     */
    private void updateCurrentPosition(int position) {
        if (!isAutoPlay) {
            return;
        }
        if (handler != null) {
            handler.removeMessages(ImageHandler.MSG_PAGE_CHANGED);
            handler.sendMessage(Message.obtain(handler, ImageHandler.MSG_PAGE_CHANGED, position, 0));
        }
    }

    /**
     * @param listener
     * @return
     */
    public Banner setOnBannerListener(OnBannerListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * @param listener
     * @return
     */
    public Banner setOnPageChangeListener(BannerPageChangeListener listener) {
        this.pageChangeListener = listener;
        return this;
    }

    @Override
    protected void onDetachedFromWindow() {
        releaseBanner();
        super.onDetachedFromWindow();
    }

    public void releaseBanner() {
        handler.removeCallbacksAndMessages(null);
    }
}
