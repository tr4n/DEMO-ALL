package com.example.mypc.demosuper.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import com.example.mypc.demosuper.fragments.GifSearchingFragment;
import com.example.mypc.demosuper.fragments.StickerSearchingFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return position == 0 ? new GifSearchingFragment()
                : position == 1 ? new StickerSearchingFragment()
                : null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
