package us.xingkong.starwishingbottle.module.register;

import android.support.design.widget.Snackbar;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import us.xingkong.starwishingbottle.base.BasePresenterImpl;
import xyz.sealynn.bmobmodel.model.User;

/**
 * Created by SeaLynn0 on 2018/4/23 23:44
 * <p>
 * Email：sealynndev@gmail.com
 */
class RegisterPresenter extends BasePresenterImpl implements RegisterContract.Presenter {

    private RegisterContract.View mView;

    RegisterPresenter(RegisterContract.View mView) {
        this.mView = mView;
        this.mView.setPresenter(this);
    }

    @Override
    public void register(String username, String password, String passwordRe) {
        mView.freeze();

        try {
            if(username == null || password == null || passwordRe == null)
                throw new NullPointerException();
            User.signUp(username, password, passwordRe, new SaveListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if (e != null) {
                        e.printStackTrace();
                        Snackbar.make(mView.getActivity().findViewById(android.R.id.content), e.toString(),Snackbar.LENGTH_SHORT).show();
                    } else {
                        mView.getActivity().finish();
                    }
                    mView.unfreeze();
                }
            });
        }catch (IllegalArgumentException e){
            Snackbar.make(mView.getActivity().findViewById(android.R.id.content),"两次输入密码不一致！",Snackbar.LENGTH_SHORT).show();
            mView.unfreeze();
        }catch (NullPointerException e){
            Snackbar.make(mView.getActivity().findViewById(android.R.id.content),"请将账号密码输入完整！",Snackbar.LENGTH_SHORT).show();
            mView.unfreeze();
        }

    }
}
