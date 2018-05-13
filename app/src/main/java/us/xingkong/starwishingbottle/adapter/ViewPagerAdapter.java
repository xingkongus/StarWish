package us.xingkong.starwishingbottle.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import java.util.List;

/**
 * Created by SeaLynn0 on 2018/5/13 17:49
 * <p>
 * Email：sealynndev@gmail.com
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Class> fragments;
    private String[] titles;

    public ViewPagerAdapter(FragmentManager fm,List<Class> list,String[] titles) {
        super(fm);
        this.fragments = list;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {

        try {
            return (Fragment) fragments.get(position).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
