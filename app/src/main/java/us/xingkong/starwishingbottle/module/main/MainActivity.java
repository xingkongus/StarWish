package us.xingkong.starwishingbottle.module.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.update.BmobUpdateAgent;
import jp.wasabeef.glide.transformations.BlurTransformation;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import us.xingkong.starwishingbottle.R;
import us.xingkong.starwishingbottle.adapter.ViewPagerAdapter;
import us.xingkong.starwishingbottle.base.BaseActivity;
import us.xingkong.starwishingbottle.base.Constants;
import us.xingkong.starwishingbottle.module.editmsg.EditMsgActivity;
import us.xingkong.starwishingbottle.module.setting.SettingActivity;

public class MainActivity extends BaseActivity<MainContract.Presenter>
        implements MainContract.View {

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
        Glide.with(this).load(R.drawable.blowball_dandelion_dandelion_seed_54300).
                apply(RequestOptions.bitmapTransform(new BlurTransformation(13))).into(imageView);
//        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式

        for (String TITLE : TITLES) {
            mTabLayout.addTab(mTabLayout.newTab().setText(TITLE));
        }

        fragments = new ArrayList<>();

        fragments.add( WishListFrament.class);
        fragments.add( MyWishFragment.class);
        fragments.add( WishingFragment.class);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments,TITLES);
        viewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void prepareData() {
        BmobUpdateAgent.initAppVersion();
        BmobUpdateAgent.setUpdateOnlyWifi(false);
        BmobUpdateAgent.update(this);
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
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
        }
        return true;
    }
}
