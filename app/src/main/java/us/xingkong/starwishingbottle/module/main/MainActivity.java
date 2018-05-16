package us.xingkong.starwishingbottle.module.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import jp.wasabeef.glide.transformations.BlurTransformation;
import pub.devrel.easypermissions.EasyPermissions;
import us.xingkong.starwishingbottle.R;
import us.xingkong.starwishingbottle.adapter.ViewPagerAdapter;
import us.xingkong.starwishingbottle.base.BaseActivity;
import us.xingkong.starwishingbottle.base.Constants;
import us.xingkong.starwishingbottle.module.editmsg.EditMsgActivity;
import us.xingkong.starwishingbottle.module.setting.SettingActivity;
import us.xingkong.starwishingbottle.module.wish.WishingActivity;
import xyz.sealynn.bmobmodel.model.Message;

public class MainActivity extends BaseActivity<MainContract.Presenter>
        implements MainContract.View, EasyPermissions.PermissionCallbacks {

    @BindView(R.id.head_image)
    AppCompatImageView imageView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private static final String[] TITLES = {"星愿", "我的", "关注"};

    private List<Class> fragments;

    @Override
    protected void initEvent(Bundle savedInstanceState) {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EditMsgActivity.class));
            }
        });

        Uri data = getIntent().getData();
        if(data != null && data.getPath() != null && data.getPath().length() > 0) {
            String id = data.getPath().substring(1);
            DoUri(data.getHost(),id);
        }
    }

    protected void DoUri(String action,String id) {
        if(action.toLowerCase().equals("wish")){
            BmobQuery<Message> bmobQuery = new BmobQuery<>();
            bmobQuery.getObject(id, new QueryListener<Message>() {
                @Override
                public void done(Message message, BmobException e) {
                    if(e != null){
                        e.printStackTrace();
                        Log.d("DoUri",e.toString());
                        Toast.makeText(MainActivity.this,"Uri错误\n" + e.toString(),Toast.LENGTH_SHORT).show();
                    }else if(message ==null){
                        Log.d("DoUri","message is null!");
                        Toast.makeText(MainActivity.this,"载入愿望出错啦",Toast.LENGTH_SHORT).show();
                    }else{
                        WishingActivity.showWishing(MainActivity.this,message,(message.isFinished()?message.getFinished().getObjectId():null));
                    }
                }
            });
        }
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

        fragments.add(WishListFrament.class);
        fragments.add(MyWishFragment.class);
        fragments.add(WishingFragment.class);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, TITLES);
        viewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void prepareData() {
        if ((!EasyPermissions.hasPermissions(MainActivity.this, Constants.PERMISSION_READ_PHONE_STATE))
                && Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            EasyPermissions.requestPermissions(MainActivity.this, getString(R.string.need_phone_permission),
                    0, Constants.PERMISSION_READ_PHONE_STATE);
        }
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /**
     * 以下是关于EasyPermissions对权限的操作
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}
