package us.xingkong.starwishingbottle.module.editmsg;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import us.xingkong.starwishingbottle.R;
import us.xingkong.starwishingbottle.base.BaseActivity;
import us.xingkong.starwishingbottle.base.Constants;
import us.xingkong.visionlibrary.operator.Blur;
import us.xingkong.visionlibrary.operator.Light;
import vision.core.Core;
import vision.image.VisionImage;
import xyz.sealynn.bmobmodel.model.User;

/**
 * Created by SeaLynn0 on 2018/5/2 21:55
 * <p>
 * Email：sealynndev@gmail.com
 */
public class EditMsgActivity extends BaseActivity<EditMsgContract.Presenter>
        implements EditMsgContract.View, EasyPermissions.PermissionCallbacks {

    @BindView(R.id.et_content)
    AppCompatEditText content;
    @BindView(R.id.bt_edit)
    AppCompatButton edit;
    @BindView(R.id.picture)
    AppCompatImageView picture;
    @BindView(R.id.linear_layout)
    LinearLayout linearLayout;
    @BindView(R.id.head_image)
    AppCompatImageView headImgView;
    @BindView(R.id.bt_switch)
    Switch switchButton;

    private myDialog dialog;
    private ProgressDialog progressDialog;
    private File file;

    @Override
    protected void initEvent(Bundle savedInstanceState) {

    }

    @Override
    protected void initViews() {
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (content.getText().toString().trim().length() > 0) {
                    progressDialog.show();
                    mPresenter.publishMessage(BmobUser.getCurrentUser(User.class), file,
                            content.getText().toString().trim(), !switchButton.isChecked(), new UploadFileListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        finish();
                                    } else
                                        e.printStackTrace();
                                    progressDialog.dismiss();
                                }
                            });
                }
                else
                    Snackbar.make(v, "内容不能为空", Snackbar.LENGTH_SHORT).show();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("许愿");
        }

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!EasyPermissions.hasPermissions(EditMsgActivity.this, Constants.PERMISSIONS_EXTERNAL_STORAGE))
                        && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    EasyPermissions.requestPermissions(EditMsgActivity.this, getString(R.string.need_permission),
                            0, Constants.PERMISSIONS_EXTERNAL_STORAGE);
                } else
                    dialog.show();

            }
        });
    }

    @Override
    protected void prepareData() {
        dialog = new myDialog(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("正在许愿");
        progressDialog.setMessage("请稍等……");
        progressDialog.setCancelable(false);
    }

    @Override
    protected EditMsgContract.Presenter createPresenter() {
        return new EditMsgPresenter(this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_edit_msg;
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            if (data != null && data.getData() != null) {
                Glide.with(EditMsgActivity.this).load(data.getData()).into(picture);
//                Glide.with(EditMsgActivity.this).load(data.getData()).into(headImgView);
                try {
                    updateImg(data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                file = getFileByUri(data.getData());
            }
        } else if (requestCode == 3) {
            if (data != null && data.getExtras() != null && data.getExtras().get("data") != null) {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");

                file = new File(getExternalCacheDir(), "camera.jpg");
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();


                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("?", "???");
                }

                Blur blur = new Blur();
                Light light = new Light();
                try {
                    blur.r = 3;
                    blur.t = 2;
                    light.d = 6;
                    VisionImage img = new VisionImage(bmp.getWidth(), bmp.getHeight());
                    bmp.getPixels(img.getRGB(), 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

                    light.Operator(img, getContext());
                    blur.Operator(img, getContext());


                    bmp.setPixels(img.getRGB(), 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
                    img = null;
                    System.gc();


                } catch (Exception e) {
                    e.printStackTrace();
                }

                Glide.with(EditMsgActivity.this).load(bmp).into(picture);
                Glide.with(EditMsgActivity.this).load(bmp).into(headImgView);


            }
        }
    }

    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    class myDialog extends AppCompatDialog {

        public myDialog(Context context) {
            super(context);
        }

        public myDialog(Context context, int theme) {
            super(context, theme);
        }

        protected myDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_chose);

            //设置窗口大小
            Window dialogWindow = getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            DisplayMetrics d = this.getContext().getResources().getDisplayMetrics();
            lp.width = (int) (d.widthPixels * 0.95f);
            dialogWindow.setAttributes(lp);

            AppCompatButton library = findViewById(R.id.bt_library);
            AppCompatButton camera = findViewById(R.id.bt_camera);
            final AppCompatButton none = findViewById(R.id.bt_none);

            library.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 2);
                    dialog.dismiss();
                }
            });

            camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, 3);
                    }
                    dialog.dismiss();
                }
            });

            none.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    file = null;
                    picture.setImageResource(R.drawable.ic_action_add_dark);
                    headImgView.setImageBitmap(null);
                    dialog.dismiss();
                }
            });
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

    /**
     * 更新输入图
     *
     * @param uri
     * @throws IOException
     */
    protected void updateImg(Uri uri) throws IOException {

        Bitmap bitmap = null;

        Bitmap img_bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

        VisionImage img = new VisionImage(img_bitmap.getWidth(), img_bitmap.getHeight());
        img_bitmap.getPixels(img.getRGB(), 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        int w = 200;
        float scale = (float) w / img.getWidth();

        if (scale > 1)
            scale = 1;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        bitmap = Bitmap.createBitmap(img_bitmap, 0, 0, img_bitmap.getWidth(), img_bitmap.getHeight(), matrix, true);
        VisionImage tmp = new VisionImage(bitmap.getWidth(), bitmap.getHeight());
        bitmap.getPixels(tmp.getRGB(), 0, tmp.getWidth(), 0, 0, tmp.getWidth(), tmp.getHeight());

        Blur b = new Blur();
        b.r = 2;
        b.t = 2;
        b.Operator(tmp, this);
        Core.Light(tmp, 0.5f);
        headImgView.setImageBitmap(Bitmap.createBitmap(tmp.getRGB(), tmp.getWidth(), tmp.getHeight(), Bitmap.Config.RGB_565));
        tmp = null;
        b = null;
        //updateOutput();
        System.gc();
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
}
