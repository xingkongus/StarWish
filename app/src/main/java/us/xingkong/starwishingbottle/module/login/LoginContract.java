package us.xingkong.starwishingbottle.module.login;

import us.xingkong.starwishingbottle.base.BasePresenter;
import us.xingkong.starwishingbottle.base.BaseView;

/**
 * Created by SeaLynn0 on 2018/4/23 23:25
 * <p>
 * Email：sealynndev@gmail.com
 */
interface LoginContract {

    interface View extends BaseView<Presenter>{
        void freeze();
        void unfreeze();
    }

    interface Presenter extends BasePresenter{
        void login(String username,String password);
    }
}
