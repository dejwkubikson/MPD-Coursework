package com.example.mpd_coursework;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

// Dawid Kubiak (S1717751)
// Responsible for passing the data to the fragments.
public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    PagerAdapter adapter;

    ArrayList<EarthQuake> ListEarthQuakes = new ArrayList<EarthQuake>();
    DataFeed dFeed;

    float highestMag = 0.0f;
    float lowestMag = Float.MAX_VALUE;

    @Override
    public void onResume()
    {
        super.onResume();
        //Log.e("MainActivity", "onResume() ListEarthQuakes.size() " + ListEarthQuakes.size());
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        //Log.e("MainActivity", "onSaveInstanceState()");
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("EarthQuakesData", ListEarthQuakes);
        savedInstanceState.putFloat("highestMag", getHighestMag());
        savedInstanceState.putFloat("lowestMag", getLowestMag());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        //Log.e("MainActivity", "onRestoreInstanceState()");
        super.onRestoreInstanceState(savedInstanceState);
        ListEarthQuakes = savedInstanceState.getParcelableArrayList("EarthQuakesData");
        highestMag = savedInstanceState.getFloat("highestMag");
        lowestMag = savedInstanceState.getFloat("lowestMag");
        adapter.notifyDataSetChanged();
        //Log.e("MainActivity", "onRestoreInstanceState() ListEarthQuakes.size() " + ListEarthQuakes.size());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AssignData();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the reference of ViewPager and TabLayout
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        tabLayout = (TabLayout)findViewById(R.id.tabs);

        // Creating first Tab named "List"
        TabLayout.Tab listTab = tabLayout.newTab();
        listTab.setText(getString(R.string.tab_text_1));
        // Adding tab in the tabLayout
        tabLayout.addTab(listTab);

        // Creating second Tab named "Map"
        TabLayout.Tab mapTab = tabLayout.newTab();
        mapTab.setText(getString(R.string.tab_text_2));
        tabLayout.addTab(mapTab);

        // Creating third Tab named "Statistics"
        TabLayout.Tab statsTab = tabLayout.newTab();
        statsTab.setText(getString(R.string.tab_text_3));
        tabLayout.addTab(statsTab);

        adapter = new com.example.mpd_coursework.PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        // addOnPageChangeListener event change the tab on slide
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        adapter.notifyDataSetChanged();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tabSelected)
            {
                viewPager.setCurrentItem(tabSelected.getPosition());

                // Hiding the back button on map in case the user didn't use it to get back to the list.
                if(tabSelected.getPosition() == 1)
                    callHideBtnOnMap();
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

        setDataFeedRefresh();
    }

    public void viewEarthQuakeOnMap(int earthQuakeID)
    {
        viewPager.setCurrentItem(1);
        MapFragment mapFragment = (MapFragment)viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
        mapFragment.selectMarker(earthQuakeID);
        mapFragment.zoomMapCamera(ListEarthQuakes.get(earthQuakeID).latitude, ListEarthQuakes.get(earthQuakeID).longitude);
        mapFragment.enableBackButton();
    }

    public void callHideBtnOnMap()
    {
        MapFragment mapFragment = (MapFragment)viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
        if(mapFragment.btnEnabled)
            mapFragment.hideBackButton();
    }

    public void AssignData(){
        dFeed = new DataFeed();
        dFeed.execute();

        ListEarthQuakes = dFeed.getEarthQuakes();
    }

    public ArrayList<EarthQuake> getEarthQuakesData(){
        return ListEarthQuakes;
    }

    public Float getHighestMag(){
        return dFeed.getHighestMag();
    }

    public Float getLowestMag(){
        return dFeed.getLowestMag();
    }

    public EarthQuake getEarthQuakeByID(int id)
    {
        for(int i = 0; i < ListEarthQuakes.size(); i++)
        {
            if(ListEarthQuakes.get(i).earthQuakeID == id)
                return ListEarthQuakes.get(i);
        }

        return null;
    }

    private void setDataFeedRefresh()
    {
        final Handler handler = new Handler();
        Timer timer = new Timer();

        TimerTask task = new TimerTask(){
            @Override
            public void run(){
             handler.post(new Runnable() {
                 @Override
                 public void run() {
                     try{
                         AssignData();
                         adapter.notifyDataSetChanged();
                     }catch(Exception e){
                        Log.e("MainActivity", "Failed to refresh data.");
                     }
                 }
             });
            }
        };

        // Setting interval to every 60 minutes (1 hour) since the earthquakes aren't that common
        timer.schedule(task, 0, 60*60*1000);
    }
}