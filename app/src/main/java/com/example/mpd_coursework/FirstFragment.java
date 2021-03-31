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

// Displays a list that the user can scroll through.
// The list should simply display the location and strength of the earthquake.
// Colour coding that displays the earthquakes from strongest to weakest.
// Colour coding done intentionally in such a way that the weakest magnitude will be green, rather than treating 0.0f as green.
public class FirstFragment extends Fragment {
    ArrayList<EarthQuake> ListEarthQuakes = new ArrayList<EarthQuake>();
    MainActivity activity;

    public FirstFragment()
    {
        // Empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        activity = (MainActivity)getActivity();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ListEarthQuakes = activity.getEarthQuakesData();

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.list_layout, container, false);
        ListView listView = (ListView)root.findViewById(R.id.listView);
        FirstFragment.CustomAdapter adapter = new FirstFragment.CustomAdapter();
        listView.setAdapter(adapter);

        return root;
    }

    public Float getMagnitudeColourRatio(float magnitude){
        // Green colour when ratio is 0.0f, red colour when ratio is 1.0f
        float ratio = 0.0f;
        // Getting the difference between highest magnitude and lowest magnitude that will be
        // later used to calculate the '%' requested magnitude is.
        float magDiff = activity.getHighestMag() - activity.getLowestMag();
        // Need to subtract the lowest magnitude from the requested magnitude as well.
        float newMag = magnitude - activity.getLowestMag();

        // Avoiding division by 0. If the highest magnitude is the same as the lowest magnitude
        // then all the markers will be green.
        if(magDiff > 0)
            return ratio = newMag / magDiff;
        else
            return ratio = 1.0f;
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
            TextView mag = itemView.findViewById(R.id.Magnitude);
            TextView location = itemView.findViewById(R.id.Location);
            EarthQuake earthQuake = ListEarthQuakes.get(i);

            eID.setText(Integer.toString(earthQuake.earthQuakeID));
            mag.setText(Float.toString(earthQuake.magnitude));
            location.setText(earthQuake.location);

            int resultColour = ColorUtils.blendARGB(Color.parseColor("#00FF00"), Color.parseColor("#FF0000"), getMagnitudeColourRatio(earthQuake.magnitude));

            // Changing the background colour to colour coding
            itemView.findViewById(R.id.itemLayout).setBackgroundColor(resultColour);

            //Log.e("FirstFragment", "Magnitude: " + earthQuake.magnitude + " Hex colour " + Integer.toHexString(resultColour).substring(2));

            return itemView;
        }
    }
}
