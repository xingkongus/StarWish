package us.xingkong.starwishingbottle.module.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import cn.bmob.v3.BmobUser;
import jp.wasabeef.glide.transformations.BlurTransformation;
import us.xingkong.starwishingbottle.module.info.InfoActivity;
import us.xingkong.starwishingbottle.util.ActivityCollector;
import us.xingkong.starwishingbottle.R;
import us.xingkong.starwishingbottle.base.BaseActivity;
import us.xingkong.starwishingbottle.module.first.FirstActivity;
import xyz.sealynn.bmobmodel.model.User;

/**
 * Created by SeaLynn0 on 2018/5/7 20:57
 * <p>
 * Email：sealynndev@gmail.com
 */
public class SettingActivity extends BaseActivity<SettingContract.Presenter> implements SettingContract.View {


    @BindView(R.id.bt_change_userinfo)
    AppCompatButton change_userinfo;
    @BindView(R.id.head_image)
    AppCompatImageView headImg;
    @BindView(R.id.bt_logout)
    AppCompatButton logout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void initEvent(Bundle savedInstanceState) {

    }

    @Override
    protected void initViews() {
        change_userinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoActivity.showUserInfo(SettingActivity.this,User.getCurrentUser(User.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(SettingActivity.this)
                        .title(R.string.islogout)
                        .positiveText(R.string.confirm)
                        .negativeText(R.string.disagree)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                BmobUser.logOut();
                                startActivity(new Intent(SettingActivity.this, FirstActivity.class));
                                ActivityCollector.finishAll();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });

        User user = User.getCurrentUser(User.class);
        if(user != null) {
            collapsingToolbarLayout.setTitle(user.getUsername());
            if(user.getAvatar() != null)
                Glide.with(this).load(user.getAvatar().getUrl())
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(13)))
                        .transition(new DrawableTransitionOptions().crossFade())
                        .into(headImg);

        }


    }

    @Override
    protected void prepareData() {
//        username.setText(BottleUser.getCurrentUser().getUsername());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    protected SettingContract.Presenter createPresenter() {
        return new SettingPresenter(this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
