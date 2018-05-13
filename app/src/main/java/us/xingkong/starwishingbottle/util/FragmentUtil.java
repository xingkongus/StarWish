package us.xingkong.starwishingbottle.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

/**
 * Created by SeaLynn0 on 2018/5/8 1:49
 * <p>
 * Email：sealynndev@gmail.com
 */
public class FragmentUtil {

    public static void addFragmentToContainer(FragmentManager manager, Fragment fragment,int frameId){
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    public static void addFragmentToContainer(FragmentManager manager, List<Fragment> fragments, int frameId){
        FragmentTransaction transaction = manager.beginTransaction();
        for(Fragment fragment: fragments)
            transaction.add(frameId, fragment);
        transaction.commit();
    }
}
