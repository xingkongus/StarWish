package us.xingkong.starwishingbottle.module.myinfo;

import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import us.xingkong.starwishingbottle.base.BasePresenterImpl;

/**
 * Created by SeaLynn0 on 2018/5/7 20:55
 * <p>
 * Email：sealynndev@gmail.com
 */
public class MyInfoPresenter extends BasePresenterImpl implements MyInfoContract.Presenter {

    private MyInfoContract.View mView;

    MyInfoPresenter(MyInfoContract.View mView) {
        this.mView = mView;
        this.mView.setPresenter(this);
    }

}
