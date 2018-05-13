package us.xingkong.starwishingbottle.module.register;

import us.xingkong.starwishingbottle.base.BasePresenter;
import us.xingkong.starwishingbottle.base.BaseView;

/**
 * Created by SeaLynn0 on 2018/4/23 23:42
 * <p>
 * Email：sealynndev@gmail.com
 */
interface RegisterContract {

    interface View extends BaseView<Presenter>{
        void freeze();
        void unfreeze();
    }

    interface Presenter extends BasePresenter{
        void register(String username, String password,String passwordRe);
    }
}
