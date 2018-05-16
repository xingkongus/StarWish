package us.xingkong.starwishingbottle.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import us.xingkong.starwishingbottle.module.first.FirstActivity;
import us.xingkong.starwishingbottle.module.setting.SettingActivity;

/**
 * Created by SeaLynn0 on 2018/5/17 0:23
 * <p>
 * Email：sealynndev@gmail.com
 */
public class BmobUtil {

    public static void logout(Activity activity) {
        BmobUser.logOut();
        BmobQuery.clearAllCachedResults();
        activity.startActivity(new Intent(activity, FirstActivity.class));
        ActivityCollector.finishAll();
    }

    public static String getStringFromErrorCode(BmobException e) {
        Log.i("ErrorCode", "getStringFromErrorCode: " + e.getErrorCode());
        switch (e.getErrorCode()) {
            case 9003:
                return "上传文件出错";
            case 9002:
                return "解析返回数据出错";
            case 9004:
                return "文件上传失败";
            case 9007:
                return "文件大小超过10M";
            case 9010:
                return "网络超时";
            case 9016:
                return "无网络连接，请检查您的手机网络.";
            case 9018:
                return "所填信息不能为空";
            case 9022:
                return "文件上传失败，请重新上传";
            case 108:
                return "用户名和密码是必需的";
            case 205:
                return "没有找到此用户";
            case 210:
                return "旧密码不正确";
            case 202:
                return "用户名已存在";
            case 109:
                return "登录信息是必需的";
            case 101:
                return "用户名或密码不正确";
            case 304:
                return "用户名或密码为空";
            default:
                return e.getMessage();
        }
    }
}
