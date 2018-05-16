package xyz.sealynn.bmobmodel.model;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
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
    private Boolean finished;
    private Boolean read;

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Boolean getRead() {
        return read;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

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

    public static void submit(final User user, final Message message, final String content, final File picture, final UpdateListener listener) {

        BmobQuery<Reversion> query = new BmobQuery<>();
        query.addWhereEqualTo("user", user);
        query.addWhereEqualTo("message", message);
        query.setLimit(1);
        query.findObjects(new FindListener<Reversion>() {
            @Override
            public void done(final List<Reversion> list, BmobException e) {
                if (e != null) {
                    e.printStackTrace();
                    Log.d("Reversion.submit", e.toString());
                    listener.done(e);
                }
                final Reversion reversion;
                if (list != null && list.size() > 0) {
                    reversion = list.get(0);
                    reversion.setContent(content);
                    reversion.setFinished(true);
                    if (picture != null) {
                        if (picture.length() > 1024 * 1024 * 8) {
                            listener.done(new BmobException("文件大小超过限制，请上传小于8M的文件"));
                            return;
                        }
                        if (reversion.getPicture() != null) {
                            reversion.getPicture().delete(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e != null) {
                                        e.printStackTrace();
                                        Log.d("Reversion.submit", e.toString());
                                    }
                                    final BmobFile bmobFile = new BmobFile(picture);
                                    bmobFile.upload(new UploadFileListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e != null) {
                                                e.printStackTrace();
                                                Log.d("Reversion.submit", e.toString());
                                                listener.done(e);
                                            } else {
                                                reversion.setPicture(bmobFile);
                                                reversion.update(new UpdateListener() {
                                                    @Override
                                                    public void done(BmobException e) {
                                                        listener.done(e);
                                                        if (e != null) {
                                                            e.printStackTrace();
                                                            Log.d("Reversion.submit", e.toString());

                                                        }

                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    } else {
                        reversion.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                listener.done(e);
                                if (e != null) {
                                    e.printStackTrace();
                                    Log.d("Reversion.submit", e.toString());
                                }

                            }
                        });
                    }

                } else {
                    reversion = new Reversion();
                    reversion.setContent(content);
                    reversion.setMessage(message);
                    reversion.setUser(user);
                    reversion.setFinished(true);
                    if (picture == null) {
                        reversion.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                listener.done(e);
                                if (e != null) {
                                    e.printStackTrace();
                                    Log.d("Reversion.submit", "s:" + s + "; " + e.toString());
                                }

                            }
                        });
                    } else {
                        final BmobFile bmobFile = new BmobFile(picture);
                        bmobFile.upload(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e != null) {
                                    e.printStackTrace();
                                    Log.d("Reversion.submit", e.toString());
                                    listener.done(e);
                                } else {
                                    reversion.setPicture(bmobFile);
                                    reversion.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            listener.done(e);
                                            if (e != null) {
                                                e.printStackTrace();
                                                Log.d("Reversion.submit", "s:" + s + "; " + e.toString());
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }


            }
        });
    }

    public static void setAllRead(List<Reversion> reversions, QueryListListener listListener){
        if(reversions == null || reversions.size() == 0)
            return;
        List<BmobObject> rs = new ArrayList<>();
        for(int i = 0;i < reversions.size();i++) {
            if(reversions.get(i).getRead())
                continue;
            Reversion reversion = new Reversion();
            reversion.setObjectId(reversions.get(i).getObjectId());
            reversion.setRead(true);
            rs.add(reversion);
        }
        new BmobBatch().updateBatch(rs).doBatch(listListener);
    }
}
