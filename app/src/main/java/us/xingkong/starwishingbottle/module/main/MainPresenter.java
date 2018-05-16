package us.xingkong.starwishingbottle.module.main;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import us.xingkong.starwishingbottle.base.BasePresenterImpl;
import xyz.sealynn.bmobmodel.model.Message;
import xyz.sealynn.bmobmodel.model.Reversion;
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
        query.setCachePolicy(policy);
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

    @Override
    public void getStarBottles(BmobQuery.CachePolicy policy, final FindListener<Message> listener) {


        BmobQuery<Reversion> query = new BmobQuery<>();
        query.addWhereEqualTo("user",User.getCurrentUser());
        query.findObjects(new FindListener<Reversion>() {
            @Override
            public void done(List<Reversion> list, BmobException e) {
                if(e != null){
                    e.printStackTrace();
                    Log.d("getStarBottles",e.toString());
                    listener.done(null,e);
                }else if(list == null){
                    BmobException e2 = new BmobException("列表为null");
                    e2.printStackTrace();
                    Log.d("getStarBottles",e2.toString());
                    listener.done(null,e2);
                } else {
                    ArrayList<String> messagesId = new ArrayList<>();
                    for(int i = 0;i < list.size();i++) {
                        messagesId.add(list.get(i).getMessage().getObjectId());
                    }

                    BmobQuery<Message> query1 = new BmobQuery<>();
                    query1.addWhereContainedIn("objectId",messagesId);
                    query1.order("-updatedAt");
                    query1.findObjects(new FindListener<Message>() {
                        @Override
                        public void done(List<Message> list, BmobException e) {
                            listener.done(list,e);
                        }
                    });
                }
            }
        });

    }
}
