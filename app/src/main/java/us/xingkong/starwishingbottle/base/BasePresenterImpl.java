package us.xingkong.starwishingbottle.base;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by SeaLynn0 on 2018/4/23 19:46
 * <p>
 * Email：sealynndev@gmail.com
 */
public class BasePresenterImpl implements BasePresenter {
    private CompositeSubscription mSubscriptions;

    @Override
    public void onStart() {
        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }
    }

    @Override
    public void onDestroy() {
        if (mSubscriptions != null && mSubscriptions.hasSubscriptions()) {
            mSubscriptions.unsubscribe();
            mSubscriptions.clear();
        }
    }
}
