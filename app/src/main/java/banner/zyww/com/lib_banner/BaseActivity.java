package banner.zyww.com.lib_banner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * @author syb
 * @date 2018/4/24
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        ButterKnife.bind(this);

        initView();
    }

    protected abstract void initView();

    /**
     * 布局id
     *
     * @return
     */
    public abstract int getLayoutResId();


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
