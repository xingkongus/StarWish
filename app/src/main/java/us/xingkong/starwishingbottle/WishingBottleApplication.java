package us.xingkong.starwishingbottle;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import cn.bmob.v3.Bmob;
import us.xingkong.starwishingbottle.base.Constants;

public class WishingBottleApplication extends Application {

    private static Context appContext;

    private List<Class<? extends View>> problemViewClassList;

    @Override
    public void onCreate() {
        super.onCreate();
        //默认初始化
        Bmob.initialize(this, Constants.ApplicationID);
//        CrashReport.initCrashReport(getApplicationContext(), Constants.APP_ID, false);
        Bugly.init(getApplicationContext(), Constants.APP_ID, false);

        appContext = getApplicationContext();

        Beta.enableNotification = false;
        /**
         * 必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回
         * 第一个参数：应用程序上下文
         * 第二个参数：如果发现滑动返回后立即触摸界面时应用崩溃，请把该界面里比较特殊的 View 的 class 添加到该集合中，目前在库中已经添加了 WebView 和 SurfaceView
         */
        problemViewClassList = new ArrayList<>();
        BGASwipeBackHelper.init(this, problemViewClassList);

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
