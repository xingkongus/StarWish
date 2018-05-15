package us.xingkong.starwishingbottle.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

/**
 * Created by SeaLynn0 on 2018/4/24 20:53
 * <p>
 * Email：sealynndev@gmail.com
 */
public class GlideImageLoader {

    /**
     * Glide圆角Builder
     * @param builder
     * @return
     */
    public static RequestBuilder Circle(RequestBuilder builder){
        RequestOptions rq = new RequestOptions();
        rq.transform(new CircleCrop());
        return builder.apply(rq);
    }

    public static SimpleTarget<Drawable> FitXY(final ImageView imageView,final int errorResourceID){

        return new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                try {
                    BitmapDrawable b = (BitmapDrawable) resource;

                    float s = (float)imageView.getWidth() / b.getBitmap().getWidth();
                    Matrix matrix = new Matrix();
                    if(s < 1)
                        matrix.setScale(s,s);

                    Bitmap bm = Bitmap.createBitmap(b.getBitmap(),0,0,b.getBitmap().getWidth(),b.getBitmap().getHeight(),matrix,true);
                    imageView.setImageBitmap(bm);

                    System.gc();

                }catch (Throwable e){
                    e.printStackTrace();
                    imageView.setImageResource(errorResourceID);
                }

            }
        };

    };


}
