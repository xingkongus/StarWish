package xyz.sealynn.bmobmodel.model;

import android.util.Log;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static android.content.ContentValues.TAG;

/**
 * Created by SeaLynn0 on 2018/5/12 23:29
 * <p>
 * Email：sealynndev@gmail.com
 */
public class Report extends BmobObject {

    private User user;
    private Message message;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public static void report(final User user, final Message message, final UpdateListener listener) {
        BmobQuery<Report> query = new BmobQuery<>();
        query.addWhereEqualTo("user", user).addWhereEqualTo("message", message);
        query.setLimit(1);
        query.findObjects(new FindListener<Report>() {
            @Override
            public void done(List<Report> list, BmobException e) {
                if (e != null) {
                    listener.done(e);
                } else {
                    if (!(list != null && list.size() > 0)) {
                        Report report = new Report();
                        report.setUser(user);
                        report.setMessage(message);
                        report.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                Log.i(TAG, s);
                                listener.done(e);
                            }
                        });
                    }
                }
            }
        });

    }

    public static void cancel(final User user, final Message message, final UpdateListener listener) {
        BmobQuery<Report> query = new BmobQuery<>();
        query.addWhereEqualTo("user", user).addWhereEqualTo("message", message);
        query.setLimit(1);
        query.findObjects(new FindListener<Report>() {
            @Override
            public void done(List<Report> list, BmobException e) {
                if (e != null) {
                    listener.done(e);
                } else {
                    if (list != null && list.size() > 0) {
                        Report report = list.get(0);
                        report.delete(listener);
                    }
                }
            }
        });
    }
}
