package xyz.sealynn.bmobmodel.model;

import android.util.Log;

import java.io.File;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import static android.content.ContentValues.TAG;

/**
 * Created by SeaLynn0 on 2018/5/12 23:25
 * <p>
 * Email：sealynndev@gmail.com
 */
public class Reversion extends BmobObject {

    private User user;
    private String content;
    private Message message;
    private BmobFile picture;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public BmobFile getPicture() {
        return picture;
    }

    public void setPicture(BmobFile picture) {
        this.picture = picture;
    }

    public static void publish(User user, Message message, File file, String content, final UploadFileListener listener) {
        final Reversion reversion = new Reversion();
        reversion.setContent(content);
        reversion.setUser(user);
        reversion.setMessage(message);
        if (file != null) {
            final BmobFile bmobFile = new BmobFile();
            bmobFile.upload(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        reversion.setPicture(bmobFile);
                        reversion.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                Log.i(TAG, s);
                                listener.done(e);
                            }
                        });
                    } else
                        listener.done(e);
                }
            });
        } else {
            reversion.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    Log.i(TAG, s);
                    listener.done(e);
                }
            });
        }
    }

    public static void shutDown(Reversion reversion, UpdateListener listener) {
        reversion.getMessage().setFinished(reversion);
        reversion.update(listener);
    }
}
