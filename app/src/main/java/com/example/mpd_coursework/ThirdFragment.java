package com.example.mpd_coursework;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

public class ThirdFragment extends Fragment {
    ArrayList<EarthQuake> ListEarthQuakes = new ArrayList<EarthQuake>();
    MainActivity activity;

    public ThirdFragment()
    {
        // Empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        MainActivity activity = (MainActivity)getActivity();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        MainActivity activity = (MainActivity)getActivity();
        ListEarthQuakes = activity.getEarthQuakesData();
        GetStatistics("20/03/2021", "30/03/2021");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.stats_layout, container, false);
    }

    public void GetStatistics(String dateFrom, String dateTo)
    {
        Log.e("ThirdFragment", "Date from: " + dateFrom + ", date to: " + dateTo);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Date dFrom = new Date();
        Date dTo = new Date();

        try {
            dFrom = sdf.parse(dateFrom);
            dTo = sdf.parse(dateTo);
        }
        catch (ParseException e) {
        }

        // Highest latitude
        float north = -Float.MAX_VALUE;
        // Lowest longitude
        float west = Float.MAX_VALUE;
        // Highest longitude
        float east = -Float.MAX_VALUE;
        // Lowest latitude
        float south = Float.MAX_VALUE;
        // Highest magnitude
        float mag = 0.0f;
        // Highest 'depth' value
        int deep = 0;
        // Lowest 'depth' value
        int shallow = 0;

        // Creating dictionary that will store earthquake IDs as values for above statistics
        Hashtable<String, Integer> statsDict = new Hashtable<String, Integer>();
        statsDict.put("North", 0);
        statsDict.put("West", 0);
        statsDict.put("East", 0);
        statsDict.put("South", 0);
        statsDict.put("Magnitude", 0);
        statsDict.put("Deepest", 0);
        statsDict.put("Shallowest", 0);
        for(int i = 0; i < ListEarthQuakes.size(); i++)
        {
            EarthQuake earthQuake = ListEarthQuakes.get(i);
            Date eDate = new Date();
            try {
                eDate = sdf.parse(earthQuake.eDate);
            }
            catch (ParseException e) {
            }

            Log.e("ThirdFragment", i + " String date: " + earthQuake.eDate + ", Date: " + eDate);

            if((eDate.after(dFrom) && eDate.before(dTo)) || eDate.equals(dFrom) || eDate.equals(dTo))
            {
                if(earthQuake.latitude > north)
                {
                    statsDict.put("North", earthQuake.earthQuakeID);
                    north = earthQuake.latitude;
                }
                if(earthQuake.longitude < west)
                {
                    statsDict.put("West", earthQuake.earthQuakeID);
                    west = earthQuake.longitude;
                }
                if(earthQuake.longitude > east)
                {
                    statsDict.put("East", earthQuake.earthQuakeID);
                    east = earthQuake.longitude;
                }
                if(earthQuake.latitude < south)
                {
                    statsDict.put("South", earthQuake.earthQuakeID);
                    south = earthQuake.latitude;
                }
                if(earthQuake.magnitude > mag)
                {
                    statsDict.put("Magnitude", earthQuake.earthQuakeID);
                    mag = earthQuake.magnitude;
                }
                if(earthQuake.depth > deep)
                {
                    statsDict.put("Deepest", earthQuake.earthQuakeID);
                    deep = earthQuake.depth;
                }
                if(earthQuake.depth < shallow)
                {
                    statsDict.put("Shallowest", earthQuake.earthQuakeID);
                    shallow = earthQuake.depth;
                }
            }
        }

        Log.e("ThridFragment", "Most northerly " + statsDict.get("North"));
        Log.e("ThridFragment", "Most westerly " + statsDict.get("West"));
        Log.e("ThridFragment", "Most easterly " + statsDict.get("East"));
        Log.e("ThridFragment", "Most southerly " + statsDict.get("South"));
        Log.e("ThridFragment", "Highest magnitude " + statsDict.get("Magnitude"));
        Log.e("ThridFragment", "Deepest " + statsDict.get("Deepest"));
        Log.e("ThridFragment", "Shallowest " + statsDict.get("Shallowest"));
    }
}
