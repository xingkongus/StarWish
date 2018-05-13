package us.xingkong.starwishingbottle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import cn.bmob.v3.Bmob;
import us.xingkong.starwishingbottle.base.Constants;

public class WishingBottleApplication extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        //默认初始化
        Bmob.initialize(this, Constants.ApplicationID);

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
