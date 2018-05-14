package us.xingkong.starwishingbottle.module.main;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import us.xingkong.starwishingbottle.base.BasePresenterImpl;
import xyz.sealynn.bmobmodel.model.Message;
import xyz.sealynn.bmobmodel.model.User;

/**
 * Created by SeaLynn0 on 2018/4/23 19:55
 * <p>
 * Email：sealynndev@gmail.com
 */
class MainPresenter extends BasePresenterImpl implements MainContract.Presenter {

    private MainContract.View mView;

    MainPresenter(MainContract.View mView) {
        this.mView = mView;
        this.mView.setPresenter(this);
    }

    @Override
    public void getBottle(FindListener<Message> listener) {
        BmobQuery<Message> query = new BmobQuery<>();

        query.addWhereEqualTo("published", true);
        query.order("-updatedAt");
        query.findObjects(listener);
    }

    @Override
    public void loadMyBottles(FindListener<Message> listener) {
        BmobQuery<Message> query = new BmobQuery<>();
        query.addWhereEqualTo("user", User.getCurrentUser());
        query.order("-updatedAt");
        query.findObjects(listener);
    }
}
