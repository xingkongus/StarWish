package us.xingkong.starwishingbottle.module.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import us.xingkong.starwishingbottle.R;
import us.xingkong.starwishingbottle.adapter.RecyclerAdapter;
import us.xingkong.starwishingbottle.base.BaseFragment;
import xyz.sealynn.bmobmodel.model.Message;

/**
 * Created by SeaLynn0 on 2018/5/13 17:37
 * <p>
 * Email：sealynndev@gmail.com
 */
public class MyWishFragment extends BaseFragment<MainContract.Presenter> implements MainContract.View {

    @BindView(R.id.my_wish_list)
    RecyclerView mWishList;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private List<Message> messages;

    private RecyclerAdapter adapter;

    @Override
    protected void init(Bundle savedInstanceState) {
        messages = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mWishList.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(messages,this.getContext());
        mWishList.setAdapter(adapter);

        Log.d("start", "init:1 ");

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadMyBottles(new FindListener<Message>() {
                    @Override
                    public void done(List<Message> list, BmobException e) {
                        if (e != null) {
                            e.printStackTrace();
                        } else {
                            messages = list;
                            adapter.setMessages(list);
                            for(Message msg : messages)
                                System.out.println(msg.getContent());
                            adapter.notifyDataSetChanged();
                        }
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    @Override
    protected MainContract.Presenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_my_wish;
    }


}
