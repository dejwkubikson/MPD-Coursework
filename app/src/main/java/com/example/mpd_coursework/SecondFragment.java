package com.example.mpd_coursework;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

// Displays a map view which allows the user to zoom in/out to view the location of a specific earthquake.
// Pins are colour coded as per the strength of the earthquake.
// Colour coding done intentionally in such a way that the weakest magnitude will be green, rather than treating 0.0f as green.
public class SecondFragment extends Fragment implements OnMapReadyCallback {
    DataFeed dFeed;
    ArrayList<EarthQuake> ListEarthQuakes = new ArrayList<EarthQuake>();

    GoogleMap mMap;

    public SecondFragment()
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
        //Log.e("SecondFragment", "Created map view");

        AssignData();

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.map_layout, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Enabling Zoom in / out button
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //LatLng UCA = new LatLng(55, -4);
        //mMap.addMarker(new MarkerOptions().position(UCA).title("TEST").icon(BitmapDescriptorFactory.defaultMarker((BitmapDescriptorFactory.HUE_RED)))).showInfoWindow(); // showInfoWindow selects marker.
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(UCA,17));

        for(int i = 0; i < ListEarthQuakes.size(); i++)
        {
            EarthQuake earthQuake = ListEarthQuakes.get(i);

            //Log.e("SecondFragment", "Latitude: " + earthQuake.latitude + ", Longitude: " + earthQuake.longitude);

            LatLng mLocation = new LatLng(earthQuake.latitude, earthQuake.longitude);

            mMap.addMarker(new MarkerOptions().position(mLocation).icon(BitmapDescriptorFactory.defaultMarker((getMarkerColour(earthQuake.magnitude)))).title(Float.toString(earthQuake.magnitude)));
        }

        // Setting the map's camera centre to UK's centre coordinates (55.3781° N, 3.4360° W) and zooming just about to get the whole UK
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(55.3f, -3.4), 5.0f));
    }

    public void AssignData(){
        dFeed = new DataFeed();
        dFeed.execute();

        ListEarthQuakes = dFeed.getEarthQuakes();
    }

    public Float getMarkerColour(float magnitude){
        // Google map's markers' colours range from 0.0f to 360.0f with red being 0.0f and green being 120.0f
        float greenColour = 120.0f;
        float ratio = 0.0f;

        // Getting the difference between highest magnitude and lowest magnitude that will be
        // later used to calculate the '%' requested magnitude is.
        float magDiff = dFeed.getHighestMag() - dFeed.getLowestMag();
        // Need to subtract the lowest magnitude from the requested magnitude as well.
        float newMag = magnitude - dFeed.getLowestMag();

        // Avoiding division by 0. If the highest magnitude is the same as the lowest magnitude
        // then all the markers will be green.
        if(magDiff > 0)
            ratio = newMag / magDiff;
        else
            ratio = 0.0f;

        //Log.e("SecondFragment", "Highest: " + dFeed.getHighestMag() + ", lowest: " + dFeed.getLowestMag() + ", ratio: " + ratio + ", float colour result: " + (greenColour - (greenColour * ratio)));

        // Now, with the given ratio, we can multiply the 120.0f and subtract that from the green colour value.
        return greenColour - (greenColour * ratio);
    }
}
