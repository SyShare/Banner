*********** Banner工具类**************

[compile 'com.banner:banner:1.0']

1、直接在布局引用
       <com.banner.Banner
           android:id="@+id/banner"
           android:layout_width="match_parent"
           android:layout_height="200dp"
           android:background="@color/colorPrimary" />


 2、也可以直接自定义自己所需要的布局样式[组件id必须按照下图所示]

    ![](./screenshot/img_1.1.png)


 3、开始使用

     banner.setData(App.images)  //设置数据
                     .setBannerAnimation(Transformer.CubeOut) //设置展示样式
                     .setIndicatorFillMode(CircleIndicatorView.FillMode.NUMBER) //设置指引器方式
                     .setViewPagerHolder(new ViewPagerHolderCreator() {         //设置viewHolder item 布局,可以设置自定义item布局
                         @Override
                         public ViewPagerHolder createViewHolder() {
                             return new CustomViewPagerHolder();
                         }
                     })
                     .start();

    对应activity生命周期

    @Override
        protected void onStart() {
            super.onStart();
            //开始轮播
            banner.startAutoPlay();
        }

        @Override
        protected void onStop() {
            super.onStop();
            //结束轮播
            banner.stopAutoPlay();
        }
gradlew clean build bintrayUpload -PbintrayUser=zhengliang1995 -PbintrayKey=xxxxxxxxxxxx -PdryRun=false