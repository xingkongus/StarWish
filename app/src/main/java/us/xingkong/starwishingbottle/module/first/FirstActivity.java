package us.xingkong.starwishingbottle.module.first;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

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
    @Override
    protected void initEvent(Bundle savedInstanceState) {
        if (BmobUser.getCurrentUser() != null)
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                startActivity(new Intent(FirstActivity.this, MainActivity.class));
                finish();
            }
        else {
            FragmentUtil.addFragmentToContainer(getSupportFragmentManager(), new FirstFragment(), R.id.content);
        }
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void prepareData() {

    }

    @Override
    protected FirstContract.Presenter createPresenter() {
        return new FirstPresenter(this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_first;
    }
}
