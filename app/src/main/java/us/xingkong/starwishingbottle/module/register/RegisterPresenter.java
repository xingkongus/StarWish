package us.xingkong.starwishingbottle.module.register;

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
//        BottleUser user = new BottleUser();
//        user.setUsername(username);
//        user.setPassword(password);
//        user.signUp(new SaveListener<BmobUser>() {
//            @Override
//            public void done(BmobUser bmobUser, BmobException e) {
//                if (e!=null) {
//                    e.printStackTrace();
//                    Toast.makeText(mView.getContext(), e.toString(), Toast.LENGTH_LONG).show();
//                }else {
//                    mView.getActivity().finish();
//                }
//                mView.unfreeze();
//            }
//        });
        User.signUp(username, password, passwordRe, new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e != null) {
                    e.printStackTrace();
                    Toast.makeText(mView.getContext(), e.toString(), Toast.LENGTH_LONG).show();
                } else {
                    mView.getActivity().finish();
                }
            }
        });

    }
}
