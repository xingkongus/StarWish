package us.xingkong.starwishingbottle.module.main;

import cn.bmob.v3.listener.FindListener;
import us.xingkong.starwishingbottle.base.BasePresenter;
import us.xingkong.starwishingbottle.base.BaseView;
import xyz.sealynn.bmobmodel.model.Message;

/**
 * Created by SeaLynn0 on 2018/4/23 19:49
 * <p>
 * Email：sealynndev@gmail.com
 */
interface MainContract {

    interface View extends BaseView<Presenter>{

    }

    interface Presenter extends BasePresenter{
        void getBottle();
        void loadMyBottles(FindListener<Message> listener);
    }
}
