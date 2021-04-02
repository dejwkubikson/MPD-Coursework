package com.example.mpd_coursework;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.SupportMapFragment;

public class DetailsFragment extends Fragment {
    MainActivity activity;

    public DetailsFragment(){
        // Empty public constructor
    }

    public DetailsFragment(int earthQuakeID)
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        activity = (MainActivity)getActivity();
        super.onCreate(savedInstanceState);
    }


    /*@Nullable
    @Override
    public View getView() {
        //Log.e("DetailsFragment", "getView()");

        return super.getView();
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Log.e("DetailsFragment", "onCreateView()");

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.details_layout, container, false);
        //SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);

        return view;
    }
}
