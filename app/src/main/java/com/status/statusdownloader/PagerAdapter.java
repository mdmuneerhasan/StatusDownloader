package com.status.statusdownloader;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class PagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> fragments=new ArrayList<>();
    public PagerAdapter(FragmentManager fm,ArrayList<Fragment> list) {
        super(fm);
        this.fragments=list;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "new status";
            case 1:
                return "downloaded status";
        }
        return null;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
