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

    /* 缓存策略
     建议的做法： 第一次进入应用的时候，设置其查询的缓存策略为CACHE_ELSE_NETWORK,
     当用户执行上拉或者下拉刷新操作时，设置查询的缓存策略为NETWORK_ELSE_CACHE。
     */
    @Override
    public void getBottle(BmobQuery.CachePolicy policy, FindListener<Message> listener) {
        BmobQuery<Message> query = new BmobQuery<>();

        query.addWhereEqualTo("published", true);
        query.order("-updatedAt");
        query.findObjects(listener);
    }

    @Override
    public void loadMyBottles(BmobQuery.CachePolicy policy, FindListener<Message> listener) {
        BmobQuery<Message> query = new BmobQuery<>();
        query.addWhereEqualTo("user", User.getCurrentUser());
        query.order("-updatedAt");
        query.setCachePolicy(policy);
        query.findObjects(listener);
    }
}
