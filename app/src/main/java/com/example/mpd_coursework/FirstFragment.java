package com.example.mpd_coursework;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import java.util.ArrayList;

public class FirstFragment extends Fragment {
    ArrayList<EarthQuake> ListEarthQuakes = new ArrayList<EarthQuake>();
    DataFeed dFeed;

    public FirstFragment()
    {
        // Empty public constructor
        AssignData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.list_layout, container, false);
        ListView listView = (ListView)root.findViewById(R.id.listView);

        AssignData();

        FirstFragment.CustomAdapter adapter = new FirstFragment.CustomAdapter();
        listView.setAdapter(adapter);

        return root;
    }

    public void AssignData(){
        dFeed = new DataFeed();
        dFeed.execute();

        ListEarthQuakes = dFeed.getEarthQuakes();
    }

    public Float getMagnitudeColourRatio(float magnitude){
        float newHighMag = dFeed.getHighestMag() - dFeed.getLowestMag();
        //float newMag = magnitude - dFeed.getLowestMag();

        //Log.e("FirstFragment", "newHighMag " + newHighMag + " newMag " + newMag + " ratio " + newMag / newHighMag);

        // Returning ratio
        if(newHighMag > 0)
            //return newMag / newHighMag;
            return magnitude / dFeed.getHighestMag();
        else
            return 0.0f;
    }

    private class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount()
        {
            return ListEarthQuakes.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View itemView = getLayoutInflater().inflate(R.layout.item_layout, null);

            TextView eID = itemView.findViewById(R.id.EarthQuakeID);
            View eColour = itemView.findViewById(R.id.EarthQuakeColour);
            TextView mag = itemView.findViewById(R.id.Magnitude);
            TextView location = itemView.findViewById(R.id.Location);
            EarthQuake earthQuake = ListEarthQuakes.get(i);

            eID.setText(Integer.toString(earthQuake.earthQuakeID));
            mag.setText(Float.toString(earthQuake.magnitude));
            location.setText(earthQuake.location);

            int resultColour = ColorUtils.blendARGB(Color.parseColor("#00FF00"), Color.parseColor("#FF0000"), getMagnitudeColourRatio(earthQuake.magnitude));

            //Log.e("FirstFragment", "Magnitude: " + earthQuake.magnitude + " Hex colour " + Integer.toHexString(resultColour).substring(2));

            eColour.setBackgroundColor(resultColour);

            return itemView;
        }
    }
}
