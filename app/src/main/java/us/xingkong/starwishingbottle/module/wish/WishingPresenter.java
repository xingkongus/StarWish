package us.xingkong.starwishingbottle.module.wish;

import us.xingkong.starwishingbottle.base.BasePresenterImpl;

/**
 * Created by SeaLynn0 on 2018/5/17 1:10
 * <p>
 * Email：sealynndev@gmail.com
 */
class WishingPresenter extends BasePresenterImpl implements WishingContract.Presenter {

    private WishingContract.View mView;

    WishingPresenter(WishingContract.View mView) {
        this.mView = mView;
        this.mView.setPresenter(this);
    }


}
