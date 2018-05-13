package us.xingkong.starwishingbottle.module.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import java.util.Objects;

import butterknife.BindView;
import cn.bmob.v3.BmobUser;
import us.xingkong.starwishingbottle.R;
import us.xingkong.starwishingbottle.adapter.ViewPagerAdapter;
import us.xingkong.starwishingbottle.base.BaseActivity;
import us.xingkong.starwishingbottle.module.editmsg.EditMsgActivity;
import us.xingkong.starwishingbottle.module.myinfo.MyInfoActivity;
import us.xingkong.starwishingbottle.util.FragmentUtil;

public class MainActivity extends BaseActivity<MainContract.Presenter> implements MainContract.View {

    @BindView(R.id.head_image)
    AppCompatImageView imageView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private static final String[] TITLES = {"星愿", "我的", "实现"};

    private List<Class> fragments;

    @Override
    protected void initEvent(Bundle savedInstanceState) {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EditMsgActivity.class));
            }
        });

    }

    @Override
    protected void initViews() {
        Glide.with(this).load(R.drawable.blowball_dandelion_dandelion_seed_54300).into(imageView);
//        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式

        for (String TITLE : TITLES) {
            mTabLayout.addTab(mTabLayout.newTab().setText(TITLE));
        }

        fragments = new ArrayList<>();

        fragments.add(WishListFrament.class);
        fragments.add(MyWishFragment.class);
        fragments.add(WishingFragment.class);

//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments,TITLES);
//        viewPager.setAdapter(adapter);
//        mTabLayout.setupWithViewPager(viewPager);
        WishListFrament f1 = new WishListFrament();
        MyWishFragment f2 = new MyWishFragment();
        WishingFragment f3 = new WishingFragment();

//        FragmentUtil.addFragmentToContainer(getSupportFragmentManager(),f1,R.id.view_pager);
//        FragmentUtil.addFragmentToContainer(getSupportFragmentManager(),f2,R.id.view_pager);
//        FragmentUtil.addFragmentToContainer(getSupportFragmentManager(),f3,R.id.view_pager);
         final View v = getLayoutInflater().inflate(R.layout.fragment_my_wish, null);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(v);
                return v;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(v);
            }
        });




    }

    @Override
    protected void prepareData() {
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
            case R.id.person:
                startActivity(new Intent(MainActivity.this, MyInfoActivity.class));
                break;
        }
        return true;
    }


}
