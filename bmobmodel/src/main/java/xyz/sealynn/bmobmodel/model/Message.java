package xyz.sealynn.bmobmodel.model;

import android.util.Log;

import java.io.File;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import static android.content.ContentValues.TAG;

/**
 * Created by SeaLynn0 on 2018/5/12 23:23
 * <p>
 * Email：sealynndev@gmail.com
 */
public class Message extends BmobObject {

    private User user;
    private Boolean published;
    private String content;
    private Reversion finished;
    private BmobFile picture;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Reversion getFinished() {
        return finished;
    }

    public void setFinished(Reversion finished) {
        this.finished = finished;
    }

    public BmobFile getPicture() {
        return picture;
    }

    public void setPicture(BmobFile picture) {
        this.picture = picture;
    }

    public static void publish(User user, File file, String content, Boolean published, final UploadFileListener listener) {
        final Message message = new Message();
        message.setContent(content);
        message.setUser(user);

        message.setPublished(published);
        if (file != null) {
            final BmobFile bmobFile = new BmobFile(file);
            bmobFile.upload(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        message.setPicture(bmobFile);
                        message.save(new SaveListener<String>() {
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
            message.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    Log.i(TAG, s);
                    listener.done(e);
                }
            });
        }
    }
}
