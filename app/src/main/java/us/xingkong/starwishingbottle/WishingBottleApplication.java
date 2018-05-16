package us.xingkong.starwishingbottle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

import cn.bmob.v3.Bmob;
import us.xingkong.starwishingbottle.base.Constants;

public class WishingBottleApplication extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        //默认初始化
        Bmob.initialize(this, Constants.ApplicationID);
//        CrashReport.initCrashReport(getApplicationContext(), Constants.APP_ID, false);
        Bugly.init(getApplicationContext(), Constants.APP_ID, false);

//        BiliShareConfiguration configuration = new BiliShareConfiguration.Builder(context)
//                .sina(appKey, redirectUrl, scope) //配置新浪
//                .qq(appId) //配置qq
//                .weixin(appId) //配置微信
//                .imageDownloader(new ShareFrescoImageDownloader()) //图片下载器
//                .build();
//
//        //global client全局共用，也可以用BiliShare.get(name)获取一个特定的client，以便业务隔离。
//        BiliShare shareClient = BiliShare.global();
//        shareClient.config(configuration); //config只需要配置一次

        appContext = getApplicationContext();

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (activity.findViewById(R.id.toolbar) != null) {
                    Log.d("APP", "onActivityCreated: isnotnull");
                    if (activity instanceof AppCompatActivity) {
                        ((AppCompatActivity) activity).setSupportActionBar((Toolbar) activity.findViewById(R.id.toolbar));
                    }
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    /**
     * 获取Application的Context
     *
     * @return 全局Context
     */
    public static Context getAppContext() {
        return appContext;
    }
}
