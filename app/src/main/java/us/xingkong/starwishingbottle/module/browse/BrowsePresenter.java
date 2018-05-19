package us.xingkong.starwishingbottle.module.browse;

import us.xingkong.starwishingbottle.base.BasePresenterImpl;

/**
 * @作者: Xuer
 * @创建时间: 2018/5/19 0:52
 * @描述:
 * @更新日志:
 */
public class BrowsePresenter extends BasePresenterImpl implements BrowseContract.Presenter {

    private BrowseContract.View mView;

    BrowsePresenter(BrowseContract.View view) {
        this.mView = view;
        this.mView.setPresenter(this);
    }
}
