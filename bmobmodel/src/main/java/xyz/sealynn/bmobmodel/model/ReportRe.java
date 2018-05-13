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
 * Created by SeaLynn0 on 2018/5/12 23:30
 * <p>
 * Email：sealynndev@gmail.com
 */
public class ReportRe extends BmobObject {

    private User user;
    private Reversion reversion;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Reversion getReversion() {
        return reversion;
    }

    public void setReversion(Reversion reversion) {
        this.reversion = reversion;
    }

    public static void report(final User user, final Reversion reversion, final UpdateListener listener) {
        BmobQuery<ReportRe> query = new BmobQuery<>();
        query.addWhereEqualTo("user", user).addWhereEqualTo("reversion", reversion);
        query.setLimit(1);
        query.findObjects(new FindListener<ReportRe>() {
            @Override
            public void done(List<ReportRe> list, BmobException e) {
                if (e != null) {
                    listener.done(e);
                } else {
                    if (!(list != null && list.size() > 0)) {
                        ReportRe report = new ReportRe();
                        report.setUser(user);
                        report.setReversion(reversion);
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

    public static void cancel(final User user, final Reversion reversion, final UpdateListener listener) {
        BmobQuery<ReportRe> query = new BmobQuery<>();
        query.addWhereEqualTo("user", user).addWhereEqualTo("reversion", reversion);
        query.setLimit(1);
        query.findObjects(new FindListener<ReportRe>() {
            @Override
            public void done(List<ReportRe> list, BmobException e) {
                if (e != null) {
                    listener.done(e);
                } else {
                    if (list != null && list.size() > 0) {
                        ReportRe report = list.get(0);
                        report.delete(listener);
                    }
                }
            }
        });
    }
}
