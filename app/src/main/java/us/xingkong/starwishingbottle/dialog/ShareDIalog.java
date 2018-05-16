package us.xingkong.starwishingbottle.dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.hentaiuncle.qrcodemaker.maker.QRcodeMaker;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import us.xingkong.starwishingbottle.R;
import xyz.sealynn.bmobmodel.model.Message;
import xyz.sealynn.bmobmodel.model.User;

public class ShareDIalog extends AppCompatDialog {

    private Message message;
    private User user;
    private QRcodeMaker maker;
    private Bitmap bitmap;

    @BindView(R.id.img_QRcode)
    AppCompatImageView qrcode;
    @BindView(R.id.bt_share)
    AppCompatButton share;
    @BindView(R.id.bt_save)
    AppCompatButton save;

    public static void share(Context context, Message message, User user){
        ShareDIalog shareDIalog = new ShareDIalog(context);
        shareDIalog.setMessage(message);
        shareDIalog.setUser(user);
        shareDIalog.show();
    }

    public ShareDIalog(Context context) {
        super(context);
    }

    public ShareDIalog(Context context, int theme) {
        super(context, theme);
    }

    protected ShareDIalog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);
        //设置窗口大小
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = this.getContext().getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels * 0.95f);
        dialogWindow.setAttributes(lp);

        ButterKnife.bind(this);
        maker = new QRcodeMaker("http://xingkongus.gqt.gcu.edu.cn/sw/?act=wish&id=" + message.getObjectId());
        maker.setWidth(500);
        maker.setHeight(500);
        maker.setLogo(BitmapFactory.decodeResource(getContext().getResources(),R.drawable.ic_action_like_pnik));
        maker.setTitle(user.getNicknameOrUsername() + "的愿望");
        maker.make(new QRcodeMaker.AsyncCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onDone(Bitmap bitmap, Exception e) {
                ShareDIalog.this.bitmap = bitmap;
                qrcode.setImageBitmap(bitmap);
                System.gc();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path = save();
                if(path != null){
                    Intent imageIntent = new Intent(Intent.ACTION_SEND);
                    imageIntent.setType("image/jpeg");
                    imageIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
                    ShareDIalog.this.getContext().startActivity(Intent.createChooser(imageIntent, "分享星愿"));
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = save();
                if(str != null){
                    Snackbar.make(findViewById(android.R.id.content),"保存成功！" ,Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean checkBitmapReady(){
        if(bitmap == null){
            Snackbar.make(findViewById(android.R.id.content),"二维码未准备好，请稍后再试",Snackbar.LENGTH_SHORT).show();
            return  false;
        }
        else
            return true;
    }

    public String save(){
        if(checkBitmapReady()){

            try {
                String str = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap, "星愿分享", user.getNicknameOrUsername() + "的愿望");
                if(str == null){
                    Snackbar.make(findViewById(android.R.id.content),"保存失败\n请检查储存权限" ,Snackbar.LENGTH_SHORT).show();
                return null;
                } else {
                    ShareDIalog.this.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(str)));
                    return str;
                }
            }catch (RuntimeException e){
                e.printStackTrace();
                Log.d("save QRcode",e.toString());
                Snackbar.make(findViewById(android.R.id.content),"保存出错\n" + e.toString(),Snackbar.LENGTH_SHORT).show();
                return null;
            }
        }else {
            return null;
        }
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Message getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}
