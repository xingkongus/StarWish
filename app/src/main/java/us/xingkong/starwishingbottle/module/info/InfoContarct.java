package us.xingkong.starwishingbottle.module.info;

import android.widget.TextView;

import us.xingkong.starwishingbottle.base.BasePresenter;
import us.xingkong.starwishingbottle.base.BaseView;
import us.xingkong.starwishingbottle.dialog.EditTextDialog;

/**
 * Created by SeaLynn0 on 2018/5/15 14:45
 * <p>
 * Email：sealynndev@gmail.com
 */
public class InfoContarct {

    interface View extends BaseView<Presenter> {
        void showMessage(String message);

        void setText(String text, TextView textView);

        void setListener(final String key, final TextView value, final Boolean isSingle
                , final EditTextDialog.EditResult editResult);
    }

    interface Presenter extends BasePresenter {
        void changePassword(String oriPass, String newPass, String newPassRe, android.view.View view);
    }
}
