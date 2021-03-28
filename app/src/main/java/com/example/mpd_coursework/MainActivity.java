package com.example.mpd_coursework;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the reference of ViewPager and TabLayout
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        tabLayout = (TabLayout)findViewById(R.id.tabs);

        // Creating first Tab named "List"
        TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setText("List");
        // Adding tab in the tabLayout
        tabLayout.addTab(firstTab);

        // Creating second Tab named "Map"
        TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setText("Map");
        tabLayout.addTab(secondTab);

        // Creating third Tab named "Statistics"
        TabLayout.Tab thirdTab = tabLayout.newTab();
        thirdTab.setText("Statistics");
        tabLayout.addTab(thirdTab);

        PagerAdapter adapter = new com.example.mpd_coursework.PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        // addOnPageChangeListener event change the tab on slide
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tabSelected)
            {
                viewPager.setCurrentItem(tabSelected.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tabSelected)
            {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tabSelected)
            {

            }
        });
    }
}