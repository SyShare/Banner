package banner.zyww.com.lib_banner;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.banner.Banner;
import com.banner.IndicatorView.CircleIndicatorView;
import com.banner.Transformer;
import com.banner.interfaces.ViewPagerHolder;
import com.banner.interfaces.ViewPagerHolderCreator;
import com.banner.listener.BannerPageChangeListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.banner)
    Banner banner;


    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        if(banner != null)
        banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }

    @Override
    protected void initView() {
        banner = findViewById(R.id.banner);
        banner.setData(App.images)
                .setBannerAnimation(Transformer.ScaleInTransformer)
                .setPageMargin(12)
                .setIndicatorFillMode(CircleIndicatorView.FillMode.NUMBER)
                .setOnPageChangeListener(new BannerPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        Log.d(TAG,"当前位置"+position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                })
                .setViewPagerHolder(new ViewPagerHolderCreator() {
                    @Override
                    public ViewPagerHolder createViewHolder() {
                        return new CustomViewPagerHolder();
                    }
                })
                .start();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    private static class CustomViewPagerHolder implements ViewPagerHolder<String> {

        private ImageView mImageView;
        private TextView mTextView;

        @Override
        public View onCreateView(Context context) {
            // 返回ViewPager 页面展示的布局
            View view = LayoutInflater.from(context).inflate(R.layout.item_view_pager, null);
            mImageView = (ImageView) view.findViewById(R.id.viewPager_item_image);
            mTextView = (TextView) view.findViewById(R.id.item_desc);

            return view;
        }

        @Override
        public void onBind(Context context, final int position, String data) {
            if (TextUtils.isEmpty(data)) {
                return;
            }
            if (mImageView != null) {
                Glide.with(context)
                        .load(Uri.parse(data))
                        .apply(new RequestOptions()
                        .transform(new GlideRoundTransform(context, 10, GlideRoundTransform.Gravity.ALL)))
                        .into(mImageView);
            }

            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "你点击了：" + position, Toast.LENGTH_SHORT).show();
                }
            });

            if (mTextView != null) {
                mTextView.setText(data);
            }
        }
    }
}
