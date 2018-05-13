package us.xingkong.starwishingbottle.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import us.xingkong.starwishingbottle.WishingBottleApplication;

/**
 * Created by SeaLynn0 on 2018/4/24 17:58
 * <p>
 * Email：sealynndev@gmail.com
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment
        implements BaseView<P> {

    private Unbinder mUnbinder;
    protected P mPresenter;

    protected View root;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.onStart();
        }
        root = inflater.inflate(bindLayout(), container, false);
        mUnbinder = ButterKnife.bind(this, root);

        init(savedInstanceState);
        return root;
    }

    protected abstract void init(Bundle savedInstanceState);


    protected abstract P createPresenter();

    @Override
    public void setPresenter(P presenter) {
        mPresenter = presenter;
    }

    /**
     * 绑定fragment的布局文件
     *
     * @return
     */
    protected abstract int bindLayout();

    @Override
    public void onDetach() {
        mUnbinder.unbind();
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        super.onDestroyView();
    }


    public Context getFragmentContext() {
        return WishingBottleApplication.getAppContext();
    }
}
