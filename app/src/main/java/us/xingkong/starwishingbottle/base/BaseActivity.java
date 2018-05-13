package us.xingkong.starwishingbottle.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import us.xingkong.starwishingbottle.util.ActivityCollector;

/**
 * Created by SeaLynn0 on 2018/4/23 19:31
 * <p>
 * Email：sealynndev@gmail.com
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView<P> {

    Unbinder bind;

    /**
     * 泛型确定Presenter
     */
    protected P mPresenter;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(bindLayout());
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        // 设置为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // ButterKnife绑定布局
        bind = ButterKnife.bind(this);

        mPresenter = createPresenter();
        if (mPresenter != null) {
            // 调用Presenter初始化方法
            mPresenter.onStart();
        }

        // 准备数据
        prepareData();
        // 初始化视图
        initViews();
        // 初始化数据
        initEvent(savedInstanceState);
    }

    protected abstract void initEvent(Bundle savedInstanceState);

    /**
     * 初始化视图，findViewById等等
     */
    protected abstract void initViews();

    /**
     * 准备数据（从Intent获取上一个界面传过来的数据或其他需要初始化的数据）
     */
    protected abstract void prepareData();

    protected abstract P createPresenter();

    /**
     * 绑定布局
     *
     * @return 布局文件的资源ID
     */
    protected abstract int bindLayout();

    /**
     * Activity销毁时清理资源
     */
    @Override
    protected void onDestroy() {
        // ButterKnife解除绑定
        bind.unbind();
        // 销毁Presenter
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setPresenter(P presenter) {

    }
}
