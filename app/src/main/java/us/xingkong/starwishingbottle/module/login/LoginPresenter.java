package us.xingkong.starwishingbottle.module.login;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import us.xingkong.starwishingbottle.util.ActivityCollector;
import us.xingkong.starwishingbottle.base.BasePresenterImpl;
import us.xingkong.starwishingbottle.module.main.MainActivity;
import xyz.sealynn.bmobmodel.model.User;

import static android.support.constraint.Constraints.TAG;

/**
 * Created by SeaLynn0 on 2018/4/23 23:30
 * <p>
 * Email：sealynndev@gmail.com
 */
class LoginPresenter extends BasePresenterImpl implements LoginContract.Presenter {

    private LoginContract.View mView;

    LoginPresenter(LoginContract.View mView) {
        this.mView = mView;
        this.mView.setPresenter(this);
    }

    @Override
    public void login(String username, String password) {
        mView.freeze();
        User.login(username, password, new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e != null) {
                    e.printStackTrace();
                    Snackbar.make(mView.getActivity().findViewById(android.R.id.content), e.toString(),Snackbar.LENGTH_SHORT).show();
                } else {
                    mView.getActivity().startActivity(new Intent(mView.getActivity(), MainActivity.class));
                    ActivityCollector.finishAll();
                }
                mView.unfreeze();
            }
        });
    }
}
