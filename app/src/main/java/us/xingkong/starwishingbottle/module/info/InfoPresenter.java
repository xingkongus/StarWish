package us.xingkong.starwishingbottle.module.info;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import us.xingkong.starwishingbottle.base.BasePresenterImpl;
import us.xingkong.starwishingbottle.module.first.FirstActivity;
import us.xingkong.starwishingbottle.util.ActivityCollector;
import us.xingkong.starwishingbottle.util.BmobUtil;
import xyz.sealynn.bmobmodel.model.User;

/**
 * Created by SeaLynn0 on 2018/5/15 14:47
 * <p>
 * Email：sealynndev@gmail.com
 */
class InfoPresenter extends BasePresenterImpl implements InfoContarct.Presenter {

    InfoContarct.View mView;

    InfoPresenter(InfoContarct.View mView) {
        this.mView = mView;
        this.mView.setPresenter(this);
    }


    @Override
    public void changePassword(String oriPass, String newPass, String newPassRe, final View view) {

        if (newPass.equals(newPassRe)) {
            User.updateCurrentUserPassword(oriPass, newPass, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        new MaterialDialog.Builder(mView.getContext())
                                .title("修改成功")
                                .content("密码修改成功，可以用新密码进行登录啦")
                                .positiveText("ok")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                        new MaterialDialog.Builder(mView.getContext())
                                                .title("重新登陆")
                                                .content("修改密码后需要重新登陆")
                                                .positiveText("ok")
                                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                        BmobUtil.logout(mView.getActivity());
                                                    }
                                                }).cancelable(false).show();
                                    }
                                }).cancelable(false).show();

                    } else {
                        Snackbar.make(view, "修改失败\n" + BmobUtil.getStringFromErrorCode(e), Snackbar.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Snackbar.make(view, "两次密码不一致！", Snackbar.LENGTH_SHORT).show();
        }
    }


}
