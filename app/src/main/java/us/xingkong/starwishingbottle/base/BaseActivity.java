package us.xingkong.starwishingbottle.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import us.xingkong.starwishingbottle.util.ActivityCollector;

/**
 * Created by SeaLynn0 on 2018/4/23 19:31
 * <p>
 * Email：sealynndev@gmail.com
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity
        implements BaseView<P>, BGASwipeBackHelper.Delegate {

    Unbinder bind;

    /**
     * 泛型确定Presenter
     */
    protected P mPresenter;

    protected BGASwipeBackHelper mSwipeBackHelper;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 在 super.onCreate(savedInstanceState) 之前调用该方法
        initSwipeBackFinish();
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

    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);

        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

//        // 设置滑动返回是否可用。默认值为 true
//        mSwipeBackHelper.setSwipeBackEnable(false);
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

    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {

    }

    @Override
    public void onSwipeBackLayoutCancel() {

    }

    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }

    @Override
    public void onBackPressed() {
        // 正在滑动返回的时候取消返回按钮事件
        if (mSwipeBackHelper.isSliding()) {
            return;
        }
        mSwipeBackHelper.backward();
    }
}
