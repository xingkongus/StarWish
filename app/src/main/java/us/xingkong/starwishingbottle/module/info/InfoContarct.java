package us.xingkong.starwishingbottle.module.info;

import android.support.v7.widget.AppCompatButton;
import android.widget.TextView;

import us.xingkong.starwishingbottle.base.BasePresenter;
import us.xingkong.starwishingbottle.base.BaseView;
import xyz.sealynn.bmobmodel.model.User;

/**
 * Created by SeaLynn0 on 2018/5/15 14:45
 * <p>
 * Email：sealynndev@gmail.com
 */
public class InfoContarct {

    interface View extends BaseView<Presenter> {
        void showMessage(String message);
    }

    interface Presenter extends BasePresenter {
        void updateInfo(User user, String value, TextView view);

        void changePassword(String oriPass, String newPass, final AppCompatButton bt);
    }
}
