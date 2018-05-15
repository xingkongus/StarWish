package us.xingkong.starwishingbottle.module.first;

import us.xingkong.starwishingbottle.base.BasePresenterImpl;

/**
 * Created by SeaLynn0 on 2018/4/24 17:24
 * <p>
 * Email：sealynndev@gmail.com
 */
class FirstPresenter extends BasePresenterImpl implements FirstContract.Presenter {

    FirstContract.View mView;

    FirstPresenter(FirstContract.View mView) {
        this.mView = mView;
        this.mView.setPresenter(this);
    }
}
