package us.xingkong.starwishingbottle.module.editmsg;

import java.io.File;

import cn.bmob.v3.listener.UploadFileListener;
import us.xingkong.starwishingbottle.base.BasePresenter;
import us.xingkong.starwishingbottle.base.BaseView;
import xyz.sealynn.bmobmodel.model.User;

/**
 * Created by SeaLynn0 on 2018/5/2 21:51
 * <p>
 * Email：sealynndev@gmail.com
 */
public interface EditMsgContract {

    interface View extends BaseView<Presenter>{

    }

    interface Presenter extends BasePresenter{
        void publishMessage(User user, File file, String content, Boolean published,
                            final UploadFileListener listener);
    }
}
