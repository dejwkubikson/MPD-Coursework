package com.example.mpd_coursework;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

// Dawid Kubiak (S1717751)
// Displays a list that the user can scroll through.
// The list simply displays the location and strength of the earthquake with colour coding that displays the earthquakes from strongest to weakest.
// Colour coding done intentionally in such a way that the weakest magnitude will be green, rather than treating 0.0f as green.
// When the earthquake item is clicked, the item is 'expanded' which displays further details of the earthquake.
public class ListFragment extends Fragment{
    MainActivity activity;

    public ListFragment()
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
        //ListEarthQuakes = activity.getEarthQuakesData();

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.list_layout, container, false);
        ListView listView = (ListView)root.findViewById(R.id.listView);
        ListFragment.CustomAdapter adapter = new ListFragment.CustomAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //String selectedItem = (String)adapterView.getItemAtPosition(i);
                //Log.e("ListFragment", "Selected earthquake ID" + selectedItem);

                LinearLayout itemDetails = (LinearLayout)view.findViewById(R.id.itemDetails);

                //Log.e("ListFragment", "itemDetails Visibility " + itemDetails.getVisibility());

                if(itemDetails.getVisibility() == View.VISIBLE)
                {
                    itemDetails.setVisibility(View.GONE);
                }
                else
                {
                    itemDetails.setVisibility(View.VISIBLE);
                }

            }
        });

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
        public int getCount(){
            return activity.ListEarthQuakes.size();
        }

        @Override
        public Object getItem(int i) {
            //Log.e("ListFragment", "getItem(" + i + ")");
            return activity.ListEarthQuakes.get(i);
        }

        @Override
        public long getItemId(int i) {
            return activity.ListEarthQuakes.get(i).earthQuakeID;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View itemView = getLayoutInflater().inflate(R.layout.item_layout, null);

            TextView mag = itemView.findViewById(R.id.Magnitude);
            TextView location = itemView.findViewById(R.id.Location);
            EarthQuake earthQuake = activity.ListEarthQuakes.get(i);

            mag.setText(Float.toString(earthQuake.magnitude));
            location.setText(earthQuake.location);

            // Item Details
            TextView detLat = itemView.findViewById(R.id.detLatitude);
            detLat.setText("Latitude: " + Float.toString(earthQuake.latitude));

            TextView detLong = itemView.findViewById(R.id.detLongitude);
            detLong.setText("Longitude: " + Float.toString(earthQuake.longitude));

            TextView detDate = itemView.findViewById(R.id.detDateTime);
            detDate.setText(earthQuake.eDate);

            TextView detMag = itemView.findViewById(R.id.detMagnitude);
            detMag.setText("Magnitude: " + Float.toString(earthQuake.magnitude));

            TextView detDepth = itemView.findViewById(R.id.detDepth);
            detDepth.setText("Depth: " + Integer.toString(earthQuake.depth) + "km");

            TextView mapView = itemView.findViewById(R.id.detMapView);
            mapView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Log.e("ListFragment", "Clicked the view map button");
                    activity.viewEarthQuakeOnMap(i);
                }
            });

            int resultColour = ColorUtils.blendARGB(Color.parseColor("#00FF00"), Color.parseColor("#FF0000"), getMagnitudeColourRatio(earthQuake.magnitude));

            // Changing the background colour to colour coding
            itemView.findViewById(R.id.itemLayout).setBackgroundColor(resultColour);

            //Log.e("ListFragment", "Magnitude: " + earthQuake.magnitude + " Hex colour " + Integer.toHexString(resultColour).substring(2));

            return itemView;
        }
    }
}
