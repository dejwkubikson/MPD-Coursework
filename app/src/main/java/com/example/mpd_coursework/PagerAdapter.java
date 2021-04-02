package com.example.mpd_coursework;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int numOfTabs;

    public PagerAdapter(FragmentManager fm, int numOfTabs)
    {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position)
    {
        Log.e("PageAdapter", "getItem(" + position + ")");
        switch (position){
            case 0:
                ListFragment tab1 = new ListFragment();
                return tab1;
            case 1:
                MapFragment tab2 = new MapFragment();
                return tab2;
            case 2:
                StatisticsFragment tab3 = new StatisticsFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount()
    {
        return numOfTabs;
    }
}
