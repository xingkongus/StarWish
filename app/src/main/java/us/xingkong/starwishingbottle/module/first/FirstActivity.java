package us.xingkong.starwishingbottle.module.first;

import android.app.ActivityManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.hentaiuncle.qrcodemaker.ScrollingActivity;

import cn.bmob.v3.BmobUser;
import us.xingkong.starwishingbottle.R;
import us.xingkong.starwishingbottle.base.BaseActivity;
import us.xingkong.starwishingbottle.module.main.MainActivity;
import us.xingkong.starwishingbottle.util.FragmentUtil;

/**
 * Created by SeaLynn0 on 2018/4/24 17:26
 * <p>
 * Email：sealynndev@gmail.com
 */
public class FirstActivity extends BaseActivity<FirstContract.Presenter> implements FirstContract.View {

    private FirstFragment firstFragment;

    @Override
    protected void initEvent(Bundle savedInstanceState) {
//        ActivityManager activityManager = (ActivityManager) getSystemService(this.ACTIVITY_SERVICE);
//        activityManager.moveTaskToFront(getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME);

//        startActivity(new Intent(this, ScrollingActivity.class));


        if (BmobUser.getCurrentUser() != null) {
            Intent intent = new Intent(FirstActivity.this, MainActivity.class);
            if(getIntent().getData() != null)
                intent.setData(getIntent().getData());
            startActivity(intent);
            finish();
        } else {
            firstFragment = new FirstFragment();
            System.out.println("!!!????");
            firstFragment.setData(getIntent().getData());
            FragmentUtil.addFragmentToContainer(getSupportFragmentManager(),firstFragment , R.id.content);
        }
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void prepareData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(firstFragment != null) {
            firstFragment.setData(getIntent().getData());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(firstFragment != null) {
            firstFragment.setData(getIntent().getData());
        }
    }

    @Override
    protected FirstContract.Presenter createPresenter() {
        return new FirstPresenter(this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_first;
    }

    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }
}
