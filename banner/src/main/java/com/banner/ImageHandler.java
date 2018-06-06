package com.banner;

import android.os.Handler;
import android.os.Message;

import com.banner.utils.BannerConfig;

import java.lang.ref.SoftReference;

/**
 * @author syb
 * @date 2018/4/23
 */

final class ImageHandler extends Handler {

    /**
     * 请求更新显示的View。
     */
    static final int MSG_UPDATE_IMAGE = 1;
    /**
     * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。
     * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
     * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
     */
    static final int MSG_PAGE_CHANGED = 4;

    /**
     * 轮播间隔时间
     */
    private long msgDelayTime = BannerConfig.DELAY_TIME;

    static final long MSG_DELAY_SHORT = 200;
    /**
     * 使用弱引用避免Handler泄露.这里的泛型参数可以不是Activity，也可以是Fragment等
     */
    private SoftReference<Banner> softReference;
    private int currentItem = 0;

    ImageHandler(SoftReference<Banner> wk) {
        softReference = wk;
    }

    /**
     * 设置延迟时间
     *
     * @param msgDelayTime
     */
    void setMsgDelayTime(long msgDelayTime) {
        this.msgDelayTime = msgDelayTime;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Banner banner = softReference.get();
        if (banner == null) {
            //Activity已经回收，无需再处理UI了
            return;
        }
        switch (msg.what) {
            case MSG_UPDATE_IMAGE:
                currentItem++;
                banner.getViewPager().setCurrentItem(currentItem);
                //准备下次播放
                sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, msgDelayTime);
                break;
            case MSG_PAGE_CHANGED:
                //记录当前的页号，避免播放的时候页面显示不正确。
                currentItem = msg.arg1;
                break;
            default:
                break;
        }
    }
}
