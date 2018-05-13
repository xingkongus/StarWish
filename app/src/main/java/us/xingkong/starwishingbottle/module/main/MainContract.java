package us.xingkong.starwishingbottle.module.main;

import us.xingkong.starwishingbottle.base.BasePresenter;
import us.xingkong.starwishingbottle.base.BaseView;

/**
 * Created by SeaLynn0 on 2018/4/23 19:49
 * <p>
 * Email：sealynndev@gmail.com
 */
interface MainContract {

    interface View extends BaseView<Presenter>{

        void setText( String content);
    }

    interface Presenter extends BasePresenter{
        void getBottle();
        void loadMyBottle(String username);
    }
}
