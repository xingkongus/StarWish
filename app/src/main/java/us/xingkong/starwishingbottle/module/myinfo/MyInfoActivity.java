package us.xingkong.starwishingbottle.module.myinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.suke.widget.SwitchButton;

import butterknife.BindView;
import cn.bmob.v3.BmobUser;
import us.xingkong.starwishingbottle.util.ActivityCollector;
import us.xingkong.starwishingbottle.R;
import us.xingkong.starwishingbottle.base.BaseActivity;
import us.xingkong.starwishingbottle.module.first.FirstActivity;

/**
 * Created by SeaLynn0 on 2018/5/7 20:57
 * <p>
 * Email：sealynndev@gmail.com
 */
public class MyInfoActivity extends BaseActivity<MyInfoContract.Presenter> implements MyInfoContract.View {
    @BindView(R.id.bt_switch)
    SwitchButton switch_isGraduate;
    @BindView(R.id.tv_username)
    AppCompatTextView username;
    @BindView(R.id.bt_change_pass)
    AppCompatButton change_password;
    @BindView(R.id.bt_logout)
    AppCompatButton logout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void initEvent(Bundle savedInstanceState) {

    }

    @Override
    protected void initViews() {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MyInfoActivity.this)
                        .title(R.string.islogout)
                        .positiveText(R.string.confirm)
                        .negativeText(R.string.disagree)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                BmobUser.logOut();
                                startActivity(new Intent(MyInfoActivity.this, FirstActivity.class));
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
        switch_isGraduate.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, final boolean isChecked) {
                new MaterialDialog.Builder(MyInfoActivity.this)
                        .title(R.string.warming)
                        .content(R.string.warning_message)
                        .positiveText(R.string.confirm)
                        .negativeText(R.string.disagree)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

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
    protected MyInfoContract.Presenter createPresenter() {
        return new MyInfoPresenter(this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_myinfo;
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
