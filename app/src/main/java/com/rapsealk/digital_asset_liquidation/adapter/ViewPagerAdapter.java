package com.rapsealk.digital_asset_liquidation.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.rapsealk.digital_asset_liquidation.view.SettingsFragment;
import com.rapsealk.digital_asset_liquidation.view.MainFragment;
import com.rapsealk.digital_asset_liquidation.view.SearchFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private int mNumberOfFragments;
    // private List<Fragment> mFragments;

    public ViewPagerAdapter(FragmentManager fm, int numberOfFragments) {
        super(fm);
        this.mNumberOfFragments = numberOfFragments;
        // mFragments = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mNumberOfFragments;
        // return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MainFragment();
            case 1:
                return new SearchFragment();
            case 2:
                return new SettingsFragment();
            default:
                return null;
        }
    }
}
