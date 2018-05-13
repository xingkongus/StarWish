package us.xingkong.starwishingbottle.module.main;

import android.os.Bundle;
import android.util.Log;

import us.xingkong.starwishingbottle.R;
import us.xingkong.starwishingbottle.base.BaseFragment;

/**
 * Created by SeaLynn0 on 2018/5/13 17:43
 * <p>
 * Email：sealynndev@gmail.com
 */
public class WishingFragment extends BaseFragment<MainContract.Presenter> implements MainContract.View {
    @Override
    protected void init(Bundle savedInstanceState) {
        Log.d("start", "init:3 ");
    }

    @Override
    protected MainContract.Presenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_wishing;
    }

}
