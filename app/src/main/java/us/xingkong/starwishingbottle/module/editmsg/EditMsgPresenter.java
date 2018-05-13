package us.xingkong.starwishingbottle.module.editmsg;

import android.util.Log;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;
import us.xingkong.starwishingbottle.base.BasePresenterImpl;
import xyz.sealynn.bmobmodel.model.Message;
import xyz.sealynn.bmobmodel.model.User;

import static cn.bmob.v3.BmobRealTimeData.TAG;

/**
 * Created by SeaLynn0 on 2018/5/2 21:53
 * <p>
 * Email：sealynndev@gmail.com
 */
public class EditMsgPresenter extends BasePresenterImpl implements EditMsgContract.Presenter {

    private EditMsgContract.View mView;

    EditMsgPresenter(EditMsgContract.View mView) {
        this.mView = mView;
        this.mView.setPresenter(this);
    }

    @Override
    public void publishMessage(User user, File file, String content, Boolean published, final UploadFileListener listener) {
        Message.publish(user, file, content, published, listener);
    }
}
