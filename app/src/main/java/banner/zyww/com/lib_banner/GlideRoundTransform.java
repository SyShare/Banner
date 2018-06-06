/*
 * Copyright (c) 2016  athou（cai353974361@163.com）.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package banner.zyww.com.lib_banner;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

/**
 * glide加载圆角图片的实现
 * Created by cai on 2016/9/11.
 */
public class GlideRoundTransform extends BitmapTransformation {
    private static final String ID = "com.zywawa.base.glide.GlideRoundTransform";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    static int mRadius = 0;
    static int mGravity = Gravity.NONE;

    private Paint paint = null;
    private Path path = null;

    public GlideRoundTransform(Context context) {
        this(context, 4);
    }

    public GlideRoundTransform(Context context, int dp) {
        this(context, dp, Gravity.NONE);
    }

    public GlideRoundTransform(Context context, int dp, int gravity) {
        super(context);
        this.mRadius = dip2px(dp);
        this.mGravity = gravity;

        paint = new Paint();
        paint.setAntiAlias(true);

        path = new Path();
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;
        int width = source.getWidth();
        int height = source.getHeight();

        Bitmap result = pool.get(width, height, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));

        path.reset();
        path.moveTo(width / 2, 0);
        if (hasGravity(Gravity.Right_Top)) {
            path.lineTo(width - mRadius, 0);
            path.arcTo(new RectF(width - mRadius * 2, 0, width, mRadius * 2), 270, 90);
//            path.cubicTo(width - mRadius, 0, width, 0, width, mRadius);
            path.lineTo(width, mRadius);
        } else {
            path.lineTo(width, 0);
        }
        if (hasGravity(Gravity.Right_Bottom)) {
            path.lineTo(width, height - mRadius);
            path.cubicTo(width, height - mRadius, width, height, width - mRadius, height);
            path.lineTo(width - mRadius, height);
        } else {
            path.lineTo(width, height);
        }
        if (hasGravity(Gravity.Left_Bottom)) {
            path.lineTo(mRadius, height);
            path.cubicTo(mRadius, height, 0, height, 0, height - mRadius);
            path.lineTo(0, height - mRadius);
        } else {
            path.lineTo(0, height);
        }
        if (hasGravity(Gravity.Left_Top)) {
            path.lineTo(0, mRadius);
            path.cubicTo(0, mRadius, 0, 0, mRadius, 0);
        } else {
            path.lineTo(0, 0);
        }
        path.lineTo(mRadius, 0);

        canvas.drawPath(path, paint);
        return result;
    }

    public static int dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private boolean hasGravity(int gravity) {
        return (mGravity & gravity) == gravity;
    }

    public static class Gravity {
        public static int NONE = 0b0000;
        public static int Left_Top = 0b0001;
        public static int Right_Top = 0b0010;
        public static int Right_Bottom = 0b0100;
        public static int Left_Bottom = 0b1000;
        public static int Left = 0b1001;
        public static int Top = 0b0011;
        public static int Right = 0b0110;
        public static int Bottom = 0b1100;
        public static int ALL = 0b1111;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof GlideRoundTransform) && ((GlideRoundTransform) o).mRadius == mRadius;
    }

    @Override
    public int hashCode() {
        return (ID.hashCode() + mRadius);
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);

        byte[] radiusData = ByteBuffer.allocate(4).putInt(mRadius).array();
        messageDigest.update(radiusData);
    }
}