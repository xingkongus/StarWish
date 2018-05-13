package us.xingkong.starwishingbottle.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SeaLynn0 on 2018/4/26 1:52
 * <p>
 * Email：sealynndev@gmail.com
 */
public class ActivityCollector {

    private static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing())
                activity.finish();
        }
        activities.clear();
    }
}
