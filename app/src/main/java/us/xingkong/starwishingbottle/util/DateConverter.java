package us.xingkong.starwishingbottle.util;

import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by SeaLynn0 on 2018/5/17 1:36
 * <p>
 * Email：sealynndev@gmail.com
 */
public class DateConverter {

    public static String getDateBefore(String date) {
        return String.valueOf(DateUtils.getRelativeTimeSpanString(getMillisFromBmobDate(date)));
    }

    public static long getMillisFromBmobDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
        long l = 0;
        try {
            Date date1 = format.parse(date);
            l = date1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return l;
    }
}
