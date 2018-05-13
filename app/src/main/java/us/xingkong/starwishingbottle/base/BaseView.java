package us.xingkong.starwishingbottle.base;

import android.app.Activity;
import android.content.Context;

public interface BaseView<P> {
    void setPresenter(P presenter);

    Activity getActivity();

    Context getContext();
}
