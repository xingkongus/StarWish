package us.xingkong.starwishingbottle.module.setting;

import us.xingkong.starwishingbottle.base.BasePresenterImpl;

/**
 * Created by SeaLynn0 on 2018/5/7 20:55
 * <p>
 * Email：sealynndev@gmail.com
 */
public class SettingPresenter extends BasePresenterImpl implements SettingContract.Presenter {

    private SettingContract.View mView;

    SettingPresenter(SettingContract.View mView) {
        this.mView = mView;
        this.mView.setPresenter(this);
    }

}
