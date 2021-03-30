package com.example.mpd_coursework;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ThirdFragment extends Fragment {
    DataFeed dFeed;
    ArrayList<EarthQuake> ListEarthQuakes = new ArrayList<EarthQuake>();

    public ThirdFragment()
    {
        // Empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.stats_layout, container, false);
    }

    public void AssignData(){
        dFeed = new DataFeed();
        dFeed.execute();

        ListEarthQuakes = dFeed.getEarthQuakes();
    }
}
