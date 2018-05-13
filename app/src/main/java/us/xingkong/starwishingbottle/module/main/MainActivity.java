package us.xingkong.starwishingbottle.module.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobUser;
import us.xingkong.starwishingbottle.R;
import us.xingkong.starwishingbottle.base.BaseActivity;
import us.xingkong.starwishingbottle.module.editmsg.EditMsgActivity;
import us.xingkong.starwishingbottle.module.myinfo.MyInfoActivity;

public class MainActivity extends BaseActivity<MainContract.Presenter> implements MainContract.View {

    @BindView(R.id.head_image)
    AppCompatImageView imageView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    private static final String[] TITLES = {"星愿", "我的愿望", "实现的愿望"};
    private List<String> mTitles = new ArrayList<>();

    @Override
    protected void initEvent(Bundle savedInstanceState) {

        mPresenter.loadMyBottle(BmobUser.getCurrentUser().getUsername());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MyInfoActivity.class));
            }
        });

    }

    @Override
    protected void initViews() {
        Glide.with(this).load(R.drawable.blowball_dandelion_dandelion_seed_54300).into(imageView);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        for (int i = 0; i < mTitles.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitles.get(i)));
        }
    }

    @Override
    protected void prepareData() {
        mTitles.addAll(Arrays.asList(TITLES));
    }

    @Override
    protected MainContract.Presenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_main;
    }


    @Override
    public void setText(String content) {
//        myContent.setText(content);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.send:
//                startActivity(new Intent(MainActivity.this, EditMsgActivity.class));
//                break;
            case R.id.add:
                startActivity(new Intent(MainActivity.this, EditMsgActivity.class));
                break;
        }
        return true;
    }


}
