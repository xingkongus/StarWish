package us.xingkong.starwishingbottle.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by SeaLynn0 on 2018/4/24 20:53
 * <p>
 * Email：sealynndev@gmail.com
 */
public class GlideImageLoader {

    /**
     * Glide圆角Builder
     *
     * @param builder
     * @return
     */
    public static RequestBuilder Circle(RequestBuilder builder) {
        RequestOptions rq = new RequestOptions();
        rq.transform(new CircleCrop());
        return builder.apply(rq);
    }

    public static SimpleTarget<Drawable> FitXY(final ImageView imageView, final int errorResourceID, final Context context) {

        return new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                try {
                    BitmapDrawable b = (BitmapDrawable) resource;

                    float s = (float) imageView.getWidth() / b.getBitmap().getWidth();
                    Matrix matrix = new Matrix();
                    if (s < 1)
                        matrix.setScale(s, s);

                    Bitmap bm = Bitmap.createBitmap(b.getBitmap(), 0, 0, b.getBitmap().getWidth(), b.getBitmap().getHeight(), matrix, true);
                    imageView.setImageBitmap(bm);

                    System.gc();

                } catch (Throwable e) {
                    e.printStackTrace();
                    Glide.with(context).load(resource).into(imageView);
                }
            }
        };
    }

    /**
     * 根据图片动态设置了imageView的高度，保持原比例不变
     * @param imageView
     */
    public static RequestListener<Drawable> setRequestListener(final ImageView imageView) {
        if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        return new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                int width = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                float scale = (float) width / (float) resource.getIntrinsicWidth();// 后面两个必须强转
                int height = Math.round(resource.getIntrinsicHeight() * scale);
                params.height = height + imageView.getPaddingTop() + imageView.getPaddingBottom();
                imageView.setLayoutParams(params);
                return false;
            }
        };
    }

    public static File saveImage(Bitmap bmp) throws IOException {
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        FileOutputStream fos = new FileOutputStream(file);
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
        return file;
    }

    /**
     * 简单获取文件格式的，本来想在做头像裁剪那判断格式
     * @param uri
     * @return
     */
    public static String getPictureFormat(String uri) {
        String[] strings = uri.split("\\.");
        String type = strings[strings.length - 1];
        Log.e("type", type);
        return type;
    }
}
