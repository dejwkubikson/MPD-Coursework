package com.example.mpd_coursework;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import java.util.ArrayList;

public class FirstFragment extends Fragment {
    ArrayList<EarthQuake> ListEarthQuakes = new ArrayList<EarthQuake>();
    float highestMag = 0.0f;
    float lowestMag = 0.0f;


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

        //DataFeed getFeed = new DataFeed();
        //getFeed.execute();

        return root;
    }

    public void AssignData(){
        DataFeed getFeed = new DataFeed();
        getFeed.execute();

        ListEarthQuakes = getFeed.getEarthQuakes();
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

            // EarthQuakeID, EarthQuakeColour,
            TextView eID = itemView.findViewById(R.id.EarthQuakeID);
            View eColour = itemView.findViewById(R.id.EarthQuakeColour);
            TextView mag = itemView.findViewById(R.id.Magnitude);
            TextView location = itemView.findViewById(R.id.Location);
            TextView eDate = itemView.findViewById(R.id.EarthQuakeDate);

            EarthQuake earthQuake = ListEarthQuakes.get(i);

            eID.setText(Integer.toString(earthQuake.earthQuakeID));
            mag.setText(Float.toString(earthQuake.magnitude));
            location.setText(earthQuake.location);
            eDate.setText(earthQuake.eDate);

            return itemView;
        }
    }
}
