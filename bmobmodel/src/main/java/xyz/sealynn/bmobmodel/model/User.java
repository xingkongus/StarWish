package xyz.sealynn.bmobmodel.model;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by SeaLynn0 on 2018/5/12 23:20
 * <p>
 * Email：sealynndev@gmail.com
 */
public class User extends BmobUser {

    private BmobFile avatar;
    private String nickname;
    private int sex;
    private String intor;

    public BmobFile getAvatar() {
        return avatar;
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar = avatar;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIntor() {
        return intor;
    }

    public void setIntor(String intor) {
        this.intor = intor;
    }


    public static void login(String username, String password, SaveListener<User> listener) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        login(user, listener);

    }

    public static void login(User user, SaveListener<User> listener) {
        user.login(listener);
    }

    public static void signUp(String username,String nickname ,String password, String passwordRe, SaveListener<User> listener) {
        if (!password.equals(passwordRe))
            throw new IllegalArgumentException("两次密码不一样");
        User user = new User();
        user.setUsername(username);
        if (nickname==null||nickname.length()==0)
            nickname = username;
        user.setNickname(nickname);
        user.setPassword(password);
        signUp(user, listener);

    }

    public static void signUp(User user, SaveListener<User> listener) {
        user.signUp(listener);
    }

    public static void uploadPicture(String picPath, final User user, final UploadFileListener listener) {
        final BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
//                    toast("上传文件成功:" + bmobFile.getFileUrl());
                    user.setAvatar(bmobFile);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            listener.done(e);
                        }
                    });
                } else
                    listener.done(e);
            }

            @Override
            public void onProgress(Integer value) {
                listener.onProgress(value);
            }
        });
    }
}
