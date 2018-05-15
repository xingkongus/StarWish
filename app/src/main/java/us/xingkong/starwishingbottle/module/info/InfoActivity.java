package us.xingkong.starwishingbottle.module.info;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import jp.wasabeef.glide.transformations.BlurTransformation;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import us.xingkong.starwishingbottle.R;
import us.xingkong.starwishingbottle.base.BaseActivity;
import us.xingkong.starwishingbottle.base.Constants;
import us.xingkong.starwishingbottle.dialog.EditTextDialog;
import us.xingkong.starwishingbottle.dialog.GetPictureDialog;
import us.xingkong.starwishingbottle.module.editmsg.EditMsgActivity;
import us.xingkong.starwishingbottle.module.first.FirstActivity;
import us.xingkong.starwishingbottle.module.main.MainActivity;
import us.xingkong.starwishingbottle.module.setting.SettingActivity;
import us.xingkong.starwishingbottle.util.ActivityCollector;
import us.xingkong.starwishingbottle.util.GlideImageLoader;
import xyz.sealynn.bmobmodel.model.User;

/**
 * 用户信息
 */
public class InfoActivity extends BaseActivity<InfoContarct.Presenter>
        implements InfoContarct.View,EasyPermissions.PermissionCallbacks {


    //--------------------------------------------------------------------
    /**
     * 静态变量以及方法
     */
    public static final String IntentKey_UserID = "userID";
    private File file;//头像文件
    private ProgressDialog progressDialog;

    public static void showUserInfo(Context packageContext, User user) {
        Intent intent = new Intent(packageContext, InfoActivity.class);
        intent.putExtra(IntentKey_UserID, user.getObjectId());
        packageContext.startActivity(intent);
    }

    public static void showUserInfo(Context packageContext, String userID) {
        Intent intent = new Intent(packageContext, InfoActivity.class);
        intent.putExtra(IntentKey_UserID, userID);
        packageContext.startActivity(intent);
    }
    //--------------------------------------------------------------------


    @BindView(R.id.head_image)
    AppCompatImageView headImg;
    @BindView(R.id.headPic)
    AppCompatImageView headPic;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.tv_username)
    TextView username;
    @BindView(R.id.tv_nicknam)
    TextView nickname;
    @BindView(R.id.tv_phone)
    TextView phone;
    @BindView(R.id.tv_email)
    TextView email;
    @BindView(R.id.tv_intor)
    TextView intor;

    private User user;
    private String id;
    private Boolean isCurrentUser;

    @Override
    protected void initEvent(Bundle savedInstanceState) {
    }

    @Override
    protected void initViews() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (id == null) {
            init(null, new IllegalArgumentException("用户ID为空！"));
        } else {
            BmobQuery<User> query = new BmobQuery<>();
            query.getObject(id, new QueryListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    init(user, e);
                }
            });
        }
    }

    @Override
    protected void prepareData() {
        id = getIntent().getStringExtra(IntentKey_UserID);
    }

    @Override
    protected InfoContarct.Presenter createPresenter() {
        return new InfoPresenter(this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_info;
    }

    protected void init(final User user, Exception e) {
        this.user = user;
        if (e != null) {
            e.printStackTrace();
            Log.d(this.toString(), e.toString());
            return;
        }

        if (user == null)
            return;

        toolbarLayout.setTitle(user.getNicknameOrUsername());

        if (user.getAvatar() != null) {
            Glide.with(this)
                    .load(user.getAvatar().getUrl())
                    .transition(new DrawableTransitionOptions().crossFade())
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25)))
                    .into(headImg);
            GlideImageLoader.Circle(Glide.with(this).load(user.getAvatar().getUrl()))
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(headPic);
        } else {

        }

        headPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAvatar(user);
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("更新头像中");
        progressDialog.setMessage("请稍等……");
        progressDialog.setCancelable(false);
        if (User.getCurrentUser(User.class) == null || !user.getObjectId().equals(User.getCurrentUser(User.class).getObjectId())) {
            isCurrentUser = false;
            //打开时判断是不是当前用户
        } else {
            isCurrentUser = true;
            invalidateOptionsMenu();    //重新加载Menu
            setListener("昵称", nickname, true, new EditTextDialog.EditResult() {
                @Override
                public void onOK(final String value) {
                    User us = new User();
                    us.setNickname(value);
                    us.update(user.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                nickname.setText(value);
                                Snackbar.make(nickname, "修改成功", Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(nickname, "修改失败\n" + e, Snackbar.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });
                }

                @Override
                public void onCancel() {

                }
            });
            setListener("电话号码", phone, true, new EditTextDialog.EditResult() {
                @Override
                public void onOK(final String value) {
                    User us = new User();
                    us.setMobilePhoneNumber(value);
                    us.update(user.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                phone.setText(value);
                                Snackbar.make(nickname, "修改成功", Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(nickname, "修改失败\n" + e, Snackbar.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });
                }

                @Override
                public void onCancel() {

                }
            });
            setListener("简介", intor, false, new EditTextDialog.EditResult() {
                @Override
                public void onOK(final String value) {
                    User us = new User();
                    us.setIntor(value);
                    us.update(user.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                intor.setText(value);
                                Snackbar.make(nickname, "修改成功", Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(nickname, "修改失败\n" + e, Snackbar.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });
                }

                @Override
                public void onCancel() {

                }
            });
            setListener("邮箱", email, true, new EditTextDialog.EditResult() {
                @Override
                public void onOK(final String value) {
                    User us = new User();
                    us.setEmail(value);
                    us.update(user.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                email.setText(value);
                                Snackbar.make(nickname, "修改成功", Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(nickname, "修改失败\n" + e, Snackbar.LENGTH_SHORT).show();
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
        setText(user.getUsername(), username);
        setText(user.getNickname(), nickname);
        setText(user.getEmail(), email);
        setText(user.getMobilePhoneNumber(), phone);
        setText(user.getIntor(), intor);
    }

    @Override
    public void setListener(final String key, final TextView value, final Boolean isSingle
            , final EditTextDialog.EditResult editResult) {
        value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditTextDialog.Edit(InfoActivity.this,
                        "编辑", key,
                        value.getText().toString(), editResult, isSingle);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isCurrentUser != null)
            if (isCurrentUser)
                getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.change_password:
                new ChangePassDialog(InfoActivity.this).show();
                break;
        }
        return true;
    }

    protected void changeAvatar(User user) {
        if (!EasyPermissions.hasPermissions(InfoActivity.this, Constants.PERMISSIONS_EXTERNAL_STORAGE)
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            EasyPermissions.requestPermissions(InfoActivity.this, getString(R.string.need_permission),
                    0, Constants.PERMISSIONS_EXTERNAL_STORAGE);
        }

        if (user == null) {
            Log.d("changeAvatar", "User is invaild!");
            return;
        }
        if (!user.getObjectId().equals(User.getCurrentUser(User.class).getObjectId()))
            return; // 该用户不是已登陆的而用户，不能更换头像

        GetPictureDialog.GetPicture(this, 1, 2, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ChangePassDialog(InfoActivity.this).show();
            }
        });
    }

    public static void uploadAvatar(final User user, File file, final UploadFileListener listener) {
        final BmobFile bf = new BmobFile(file);
        bf.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e != null)
                    listener.done(e);
                else {

                    if (user.getAvatar() != null)
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

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
                        if (e == null) {
                            showMessage("头像更新成功");
                        } else {
                            showMessage("头像更新失败\n" + e.toString());
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        } else if (requestCode == 2) {
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
                        if (e == null) {
                            showMessage("头像更新成功");
                        } else {
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

    @Override
    public void showMessage(String message) {
        Snackbar.make(InfoActivity.this.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setText(String text, TextView textView) {
        if (text != null && text.length() > 0)
            textView.setText(text);
    }

    /**
     * 以下是关于EasyPermissions对权限的操作
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    class ChangePassDialog extends AppCompatDialog {

        @BindView(R.id.ok)
        AppCompatButton ok;
        @BindView(R.id.cancel)
        AppCompatButton cancel;
        @BindView(R.id.et_ori_code)
        AppCompatEditText originalCode;
        @BindView(R.id.et_new_code)
        AppCompatEditText newCode;
        @BindView(R.id.et_conf_new_code)
        AppCompatEditText confNewCode;

        public ChangePassDialog(Context context) {
            super(context);
        }

        public ChangePassDialog(Context context, int theme) {
            super(context, theme);
        }

        protected ChangePassDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_change_pass);
            ButterKnife.bind(this);

            Log.d("", "onCreate: ==========");

            //设置窗口大小
            Window dialogWindow = getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            DisplayMetrics d = this.getContext().getResources().getDisplayMetrics();
            lp.width = (int) (d.widthPixels * 0.95f);
            dialogWindow.setAttributes(lp);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChangePassDialog.this.dismiss();
                }
            });

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.changePassword(originalCode.getText().toString().trim()
                            , newCode.getText().toString().trim()
                            , confNewCode.getText().toString().trim(), toolbarLayout);
                    ChangePassDialog.this.dismiss();
                }
            });

        }
    }
}
