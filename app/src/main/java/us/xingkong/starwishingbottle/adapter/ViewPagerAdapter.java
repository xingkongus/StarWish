package us.xingkong.starwishingbottle.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import us.xingkong.starwishingbottle.util.FragmentUtil;

/**
 * Created by SeaLynn0 on 2018/5/13 17:49
 * <p>
 * Email：sealynndev@gmail.com
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private FragmentManager fm;
    private Fragment[] fms;
    private String[] titles;

    public ViewPagerAdapter(FragmentManager fm,List<Class> fragments,String[] titles) {
        super(fm);
        fms = new Fragment[fragments.size()];
        for(int i = 0;i < fms.length;i++){
            try {
                fms[i] = (Fragment) fragments.get(i).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.titles = titles;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return fms[position];
    }

    @Override
    public int getCount() {
        return fms.length;
    }
}
