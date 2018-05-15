package us.xingkong.starwishingbottle.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import us.xingkong.starwishingbottle.R;

/**
 * 获取图片对话框
 */
public class GetPictureDialog extends AppCompatDialog {

    private int flag_picture,flag_camera;
    private View.OnClickListener noneListener;
    private Activity activity;

    public GetPictureDialog(Context context) {
        super(context);
    }

    public GetPictureDialog(Context context, int theme) {
        super(context, theme);
    }

    protected GetPictureDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setFlag_picture(int flag_picture) {
        this.flag_picture = flag_picture;
    }

    public void setFlag_camera(int flag_camera) {
        this.flag_camera = flag_camera;
    }

    public int getFlag_camera() {
        return flag_camera;
    }

    public int getFlag_picture() {
        return flag_picture;
    }

    public void setNoneListener(View.OnClickListener noneListener) {
        this.noneListener = noneListener;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
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
        final AppCompatButton camera = findViewById(R.id.bt_camera);
        final AppCompatButton none = findViewById(R.id.bt_none);

        library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activity.startActivityForResult(intent, flag_picture);
                GetPictureDialog.this.dismiss();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                    activity.startActivityForResult(takePictureIntent, flag_camera);
                }
                GetPictureDialog.this.dismiss();
            }
        });

        none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noneListener != null)
                    noneListener.onClick(v);
                GetPictureDialog.this.dismiss();
            }
        });
    }

    public static void GetPicture(AppCompatActivity owner, int PictureFlag, int CameraFlag, View.OnClickListener noneListener){

        GetPictureDialog getPictureDialog = new GetPictureDialog(owner);
        getPictureDialog.setActivity(owner);
        getPictureDialog.setFlag_camera(CameraFlag);
        getPictureDialog.setFlag_picture(PictureFlag);
        getPictureDialog.setNoneListener(noneListener);
        getPictureDialog.show();
    }

}
