package us.xingkong.starwishingbottle.dialog;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import us.xingkong.starwishingbottle.R;
import us.xingkong.starwishingbottle.module.editmsg.EditMsgActivity;

public class DoItDialog extends AppCompatDialog {

    private File imageFile;

    private ResultListener resultListener;

    @BindView(R.id.message)
    AppCompatEditText message;
    @BindView(R.id.ok)
    AppCompatButton ok;
    @BindView(R.id.cancel)
    AppCompatButton cancel;
    @BindView(R.id.picture)
    AppCompatImageView picture;

    public DoItDialog(Context context) {
        super(context);
    }

    public DoItDialog(Context context, int theme) {
        super(context, theme);
    }

    protected DoItDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setImageUri(Uri imageUri) {
        if (imageUri != null) {
            Glide.with(this.getContext()).load(imageUri).into(picture);
            imageFile = getFileByUri(imageUri);
        } else {
            Glide.with(this.getContext()).load(R.drawable.ic_action_add_dark).into(picture);
            imageFile = null;
        }
    }

    public void setImageFile(File file) {
        imageFile = file;
        if (file != null) {
            Glide.with(this.getContext()).load(file).into(picture);
        } else {
            Glide.with(this.getContext()).load(R.drawable.ic_action_add_dark).into(picture);
        }
    }

    public void setResultListener(ResultListener resultListener) {
        this.resultListener = resultListener;
    }

    public File getImageFile() {
        return imageFile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_do_it);
        //设置窗口大小
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = this.getContext().getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels * 0.85f);
        lp.height = (int) (d.heightPixels * 0.65f);
        dialogWindow.setAttributes(lp);
        dialogWindow.setBackgroundDrawableResource(R.drawable.dialog_do_it_background);

        ButterKnife.bind(this);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoItDialog.this.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DoItDialog.this.message.getText().toString().length() > 0) {
                    if (resultListener != null)
                        resultListener.onOK(DoItDialog.this.getImageFile(), DoItDialog.this.message.getText().toString());
                    DoItDialog.this.dismiss();
                } else {
                    Snackbar.make(view, "内容不能为空!", Snackbar.LENGTH_SHORT).show();
                }

            }
        });

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resultListener != null)
                    resultListener.onSelectPicture();
            }
        });
    }

    public void clear() {
        this.setImageFile(null);
        this.message.setText("");
    }

    @Override
    public void show() {
        if (picture != null)
            clear();
        super.show();
    }

    public interface ResultListener {
        void onOK(File imageFile, String message);

        void onSelectPicture();
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
                ContentResolver cr = this.getContext().getContentResolver();
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
            Cursor cursor = this.getContext().getContentResolver().query(uri, proj, null, null, null);
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
