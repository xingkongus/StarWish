package us.xingkong.starwishingbottle.module.info;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import jp.wasabeef.glide.transformations.BlurTransformation;
import us.xingkong.starwishingbottle.R;
import us.xingkong.starwishingbottle.dialog.EditTextDialog;
import us.xingkong.starwishingbottle.dialog.GetPictureDialog;
import us.xingkong.starwishingbottle.module.editmsg.EditMsgActivity;
import us.xingkong.starwishingbottle.module.main.MainActivity;
import us.xingkong.starwishingbottle.module.setting.SettingActivity;
import us.xingkong.starwishingbottle.util.GlideImageLoader;
import xyz.sealynn.bmobmodel.model.User;

/**
 * 用户信息
 */
public class InfoActivity extends AppCompatActivity {


    //--------------------------------------------------------------------
    /**
     * 静态变量以及方法
     */
    public static final String IntentKey_UserID = "userID";
    private File file;//头像文件
    private ProgressDialog progressDialog;

    public static void showUserInfo(Context packageContext,User user){
        Intent intent = new Intent(packageContext,InfoActivity.class);
        intent.putExtra(IntentKey_UserID,user.getObjectId());
        packageContext.startActivity(intent);
    }

    public static void showUserInfo(Context packageContext,String userID){
        Intent intent = new Intent(packageContext,InfoActivity.class);
        intent.putExtra(IntentKey_UserID,userID);
        packageContext.startActivity(intent);
    }
    //--------------------------------------------------------------------


    private Toolbar toolbar;
    private AppCompatImageView headImg,headPic;
    private CollapsingToolbarLayout toolbarLayout;
    private TextView username,nickname,phone,email,intor;
    private AppCompatButton changePssword,changeAvatar;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);


        initView();

        String id = getIntent().getStringExtra(IntentKey_UserID);

        if(id == null){
            init(null,new IllegalArgumentException("用户ID为空！"));
        }else {
            BmobQuery<User> query = new BmobQuery<User>();
            query.getObject(id, new QueryListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    init(user,e);
                }
            });
        }
    }

    protected void initView(){
        toolbar = findViewById(R.id.toolbar);
        headImg = findViewById(R.id.head_image);
        headPic = findViewById(R.id.headPic);
        toolbarLayout = findViewById(R.id.collapsing_toolbar);
        username = findViewById(R.id.tv_username);
        nickname = findViewById(R.id.tv_nicknam);
        phone = findViewById(R.id.tv_phone);
        email = findViewById(R.id.tv_email);
        intor = findViewById(R.id.tv_intor);
        changePssword = findViewById(R.id.changePassword);
        changeAvatar = findViewById(R.id.changeAvatar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void init(final User user, Exception e){
        this.user = user;
        if(e != null){
            e.printStackTrace();
            Log.d(this.toString(),e.toString());
            return;
        }

        if(user == null)
            return;

        toolbarLayout.setTitle(user.getUsername());

        if(user.getAvatar() != null) {
            Glide.with(this)
                    .load(user.getAvatar().getUrl())
                    .transition(new DrawableTransitionOptions().crossFade())
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25)))
                    .into(headImg);
            GlideImageLoader.Circle(Glide.with(this).load(user.getAvatar().getUrl()))
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(headPic);
        }else{

        }

        headPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //changeAvatar(user);
            }
        });


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("更新头像中");
        progressDialog.setMessage("请稍等……");
        progressDialog.setCancelable(false);
        if(User.getCurrentUser(User.class) == null || !user.getObjectId().equals(User.getCurrentUser(User.class).getObjectId())){
            changePssword.setVisibility(View.GONE);
            changeAvatar.setVisibility(View.GONE);
        }else{
            changeAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeAvatar(user);
                }
            });
            setListener("昵称", nickname, new EditTextDialog.EditResult() {
                @Override
                public void onOK(final String value) {
                    User us = new User();
                    us.setNickname(value);
                    us.update(user.getObjectId(),new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                nickname.setText(value);
                                Snackbar.make(nickname,"修改成功",Snackbar.LENGTH_SHORT).show();
                            }else{
                                Snackbar.make(nickname,"修改失败\n" + e,Snackbar.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });
                }

                @Override
                public void onCancel() {

                }
            });
            setListener("电话号码", phone, new EditTextDialog.EditResult() {
                @Override
                public void onOK(final String value) {
                    User us = new User();
                    us.setMobilePhoneNumber(value);
                    us.update(user.getObjectId(),new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                phone.setText(value);
                                Snackbar.make(nickname,"修改成功",Snackbar.LENGTH_SHORT).show();
                            }else{
                                Snackbar.make(nickname,"修改失败\n" + e,Snackbar.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });
                }

                @Override
                public void onCancel() {

                }
            });
            setListener("简介", intor, new EditTextDialog.EditResult() {
                @Override
                public void onOK(final String value) {
                    User us = new User();
                    us.setIntor(value);
                    us.update(user.getObjectId(),new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                intor.setText(value);
                                Snackbar.make(nickname,"修改成功",Snackbar.LENGTH_SHORT).show();
                            }else{
                                Snackbar.make(nickname,"修改失败\n" + e,Snackbar.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });
                }

                @Override
                public void onCancel() {

                }
            });
            setListener("邮箱", email, new EditTextDialog.EditResult() {
                @Override
                public void onOK(final String value) {
                    User us = new User();
                    us.setEmail(value);
                    us.update(user.getObjectId(),new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                email.setText(value);
                                Snackbar.make(nickname,"修改成功",Snackbar.LENGTH_SHORT).show();
                            }else{
                                Snackbar.make(nickname,"修改失败\n" + e,Snackbar.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });
                }

                @Override
                public void onCancel() {

                }
            });
        }
        setText(user.getUsername(),username);
        setText(user.getNickname(),nickname);
        setText(user.getEmail(),email);
        setText(user.getMobilePhoneNumber(),phone);
        setText(user.getIntor(),intor);

        //setListener("用户名",username);
    }

    protected void setListener(final String key, final TextView value, final EditTextDialog.EditResult editResult){
        value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditTextDialog.Edit(InfoActivity.this,
                        "编辑", key,
                        value.getText().toString(),editResult );
        }
        });
    }

    protected void setText(String text,TextView textView){
        if(text != null && text.length() > 0)
            textView.setText(text);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    protected void changeAvatar(User user){
        if(user == null) {
            Log.d("changeAvatar","User is invaild!");
            return;
        }
        if (!user.getObjectId().equals(User.getCurrentUser(User.class).getObjectId()))
            return; // 该用户不是已登陆的而用户，不能更换头像

        GetPictureDialog.GetPicture(this, 1, 2, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public static void uploadAvatar(final User user, File file, final UploadFileListener listener){
        final BmobFile bf = new BmobFile(file);
        bf.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e != null)
                    listener.done(e);
                else {

                    if(user.getAvatar() != null)
                        user.getAvatar().delete();//先删除旧头像
                    user.setAvatar(bf);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            listener.done(e);
                        }
                    });

                }
            }
        });
    }

    protected void showMessage(String message){
        Snackbar.make(InfoActivity.this.findViewById(android.R.id.content),message,Snackbar.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1){

            if (data != null && data.getData() != null) {
                Glide.with(this)
                        .load(data.getData())
                        .transition(new DrawableTransitionOptions().crossFade())
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(25)))
                        .into(headImg);
                GlideImageLoader.Circle(Glide.with(this).load(data.getData()))
                        .transition(new DrawableTransitionOptions().crossFade())
                        .into(headPic);

                file = getFileByUri(data.getData());
                progressDialog.show();
                uploadAvatar(this.user, file, new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e == null){
                            showMessage("头像更新成功");
                        }else{
                            showMessage("头像更新失败\n" + e.toString());
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        }else if(requestCode == 2){
            if (data != null && data.getExtras() != null && data.getExtras().get("data") != null) {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");

                Glide.with(this)
                        .load(bmp)
                        .transition(new DrawableTransitionOptions().crossFade())
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(25)))
                        .into(headImg);
                GlideImageLoader.Circle(Glide.with(this).load(bmp))
                        .transition(new DrawableTransitionOptions().crossFade())
                        .into(headPic);

                file = new File(getExternalCacheDir(), "camera.jpg");
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 80, out);
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("?", "???");
                }
                progressDialog.show();
                uploadAvatar(this.user, file, new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e == null){
                            showMessage("头像更新成功");
                        }else{
                            showMessage("头像更新失败\n" + e.toString());
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        }
    }

    /**
     * 通过Uri返回File文件
     * 注意：通过相机的是类似content://media/external/images/media/97596
     * 通过相册选择的：file:///storage/sdcard0/DCIM/Camera/IMG_20150423_161955.jpg
     * 通过查询获取实际的地址
     *
     * @param uri
     * @return
     */
    public File getFileByUri(Uri uri) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = this.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA}, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    System.out.println("temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = this.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();

            return new File(path);
        } else {
            Log.i("", "Uri Scheme:" + uri.getScheme());
        }
        return null;
    }
}
