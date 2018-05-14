package us.xingkong.starwishingbottle.util;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by SeaLynn0 on 2018/4/24 20:53
 * <p>
 * Email：sealynndev@gmail.com
 */
public class GlideImageLoader extends ImageLoader {

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

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        /**
         注意：
         1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
         2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
         传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
         切记不要胡乱强转！
         */


        //Glide 加载图片简单用法
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Glide.with(context).load(context.getDrawable((int)path)).into(imageView);
        }else {
            Glide.with(context).load(context.getResources().getDrawable((int)path)).into(imageView);
        }

    }


}
