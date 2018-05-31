package com.example.mypc.officaligif.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.mypc.officaligif.fragments.DownloadedFragment;
import com.example.mypc.officaligif.fragments.FavoriteFragment;
import com.example.mypc.officaligif.fragments.HomeFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return position == 0 ? new HomeFragment()
                : position == 1 ? new FavoriteFragment()
                : position == 2 ? new DownloadedFragment()
                : null;

    }

    @Override
    public int getCount() {
        return 3;
    }
}
