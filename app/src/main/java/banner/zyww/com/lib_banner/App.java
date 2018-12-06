package banner.zyww.com.lib_banner;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class App extends Application {
    public static List<?> images = new ArrayList<>();
    public static List<?> images2 = new ArrayList<>();
    public static List<String> titles = new ArrayList<>();
    public static int H, W;
    public static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        getScreen(this);
//        Recovery.getInstance()
//                .debug(true)
//                .recoverInBackground(false)
//                .recoverStack(true)
//                .mainPage(MainActivity.class)
//                .init(this);
        String[] urls = getResources().getStringArray(R.array.url);
        List list = Arrays.asList(urls);
        images = new ArrayList(list);


        String[] urls2 = getResources().getStringArray(R.array.url1);
        List list2 = Arrays.asList(urls2);
        images2 = new ArrayList(list2);
    }

    public void getScreen(Context aty) {
        DisplayMetrics dm = aty.getResources().getDisplayMetrics();
        H = dm.heightPixels;
        W = dm.widthPixels;
    }
}
