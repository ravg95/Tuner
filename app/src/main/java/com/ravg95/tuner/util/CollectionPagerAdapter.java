package com.ravg95.tuner.util;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ravg95.tuner.fragment.PagerObjectFragment;

/**
 * Created by rafal on 16/05/2018.
 */

public class CollectionPagerAdapter extends FragmentPagerAdapter {
    public CollectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new PagerObjectFragment();
        Bundle args = new Bundle();
        args.putInt(PagerObjectFragment.ARG_OBJECT, i + 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}

