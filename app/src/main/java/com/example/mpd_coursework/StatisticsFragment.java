package com.example.mpd_coursework;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

// Dawid Kubiak (S1717751)
// Allows the user to enter a specific date or date range and shows the following information:
// - Most Northerly / Southerly / Westerly / Easterly earthquake
// - Largest magnitude Earthquake
// - Deepest and shallowest earthquake
public class StatisticsFragment extends Fragment implements View.OnClickListener {
    View view;
    MainActivity activity;
    Boolean dataDisplayed = false;
    Boolean dateRange = false;
    String strDateFrom = "";
    String strDateTo = "";

    public StatisticsFragment()
    {
        // Empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            strDateFrom = savedInstanceState.getString("dateFrom");
            strDateTo = savedInstanceState.getString("dateTo");
            dataDisplayed = savedInstanceState.getBoolean("dataDisplayed");
            dateRange = savedInstanceState.getBoolean("dateRange");

            //Log.e("StatisticsFragment", "strDateFrom " + strDateFrom + " strDateTo " + strDateTo + " dataDispalyed " + dataDisplayed + " dateRange " + dateRange);

            if(dataDisplayed)
            {
                if(strDateFrom.length() == 10 && strDateTo.length() == 10)
                {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    try{
                        GetStatistics(sdf.parse(strDateFrom), sdf.parse(strDateTo));
                    }catch(ParseException e){
                        ShowError("Couldn't parse the date.");
                    }
                }
            }

            if(dateRange)
            {
                // Changing visibility
                LinearLayout dateToLayout = (LinearLayout)view.findViewById(R.id.dateToLayout);
                if(dateToLayout != null)
                    dateToLayout.setVisibility(View.VISIBLE);
                // Changing Date text
                TextView datePlainTV = (TextView)view.findViewById(R.id.datePlainText);
                if(datePlainTV != null)
                    datePlainTV.setText("Date From");
            }
            else
            {
                // Changing visibility
                LinearLayout dateToLayout = (LinearLayout)view.findViewById(R.id.dateToLayout);
                if(dateToLayout != null)
                    dateToLayout.setVisibility(View.GONE);
                // Changing Date text
                TextView datePlainTV = (TextView)view.findViewById(R.id.datePlainText);
                if(datePlainTV != null)
                    datePlainTV.setText("Date");
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean("dataDisplayed", dataDisplayed);
        savedInstanceState.putBoolean("dateRange", dateRange);
        savedInstanceState.putString("dateFrom", strDateFrom);
        savedInstanceState.putString("dateTo", strDateTo);
    }

    @Nullable
    @Override
    public View getView() {
        //Log.e("StatisticsFragment", "getView()");
        return super.getView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.stats_layout, container, false);
        Switch dateSwitch = (Switch)view.findViewById(R.id.dateSwitch);
        dateSwitch.setOnClickListener(this);
        Button showBtn = (Button)view.findViewById(R.id.showBtn);
        showBtn.setOnClickListener(this);

        return view;
    }

    public void ShowError(String errorMsg)
    {
        TextView errorTV = (TextView)view.findViewById(R.id.errorText);
        errorTV.setVisibility(View.VISIBLE);
        errorTV.setText(errorMsg);
    }

    public void EraseError()
    {
        TextView errorTV = (TextView)view.findViewById(R.id.errorText);
        errorTV.setVisibility(View.GONE);
        errorTV.setText("");
    }

    @Override
    public void onClick(View v) {
        Switch dateSwitch = (Switch)view.findViewById(R.id.dateSwitch);
        TextView dateFromTV = (TextView)view.findViewById(R.id.dateFrom);
        TextView dateToTV = (TextView)view.findViewById(R.id.dateTo);

        EraseError();
        //Log.e("StatisticsFragment", "v.getId() " + v.getId());
        switch(v.getId())
        {
            case R.id.showBtn:
                Log.e("ThirdFragment", "Clicked show button");
                if(dateFromTV.getText().length() != 10)
                {
                    Log.e("ThirdFragment", "Please input the date in dd/mm/yyyy format");
                    if(dateSwitch.isChecked())
                    {
                        ShowError("Please input the dates in dd/mm/yyyy format");
                    }
                    else
                    {
                        ShowError("Please input the date in dd/mm/yyyy format");
                    }
                    return;
                }
                if(dateToTV.getText().length() != 10 && dateSwitch.isChecked())
                {
                    Log.e("ThirdFragment", "Please input the date to in dd/mm/yyyy format");
                    ShowError("Please input the dates in dd/mm/yyyy format");
                    return;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try{
                    Date dateFrom = sdf.parse(dateFromTV.getText().toString());
                    strDateFrom = dateFromTV.getText().toString();
                    Date dateTo = new Date();
                    if(dateSwitch.isChecked())
                    {
                        dateTo = sdf.parse(dateToTV.getText().toString());
                        strDateTo = dateToTV.getText().toString();
                    }else
                    {
                        dateTo = dateFrom;
                        strDateTo = strDateFrom;
                    }

                    GetStatistics(dateFrom, dateTo);

                }catch(ParseException e){
                    Log.e("ThirdFragment", "Couldn't parse the dates.");
                    ShowError("An error occurred when converting the date.");
                    return;
                }
                break;
            case R.id.dateSwitch:
                Log.e("ThirdFragment", "Clicked switch button");
                if(dateSwitch.isChecked())
                {
                    LinearLayout dateToLayout = (LinearLayout)view.findViewById(R.id.dateToLayout);
                    dateToLayout.setVisibility(View.VISIBLE);

                    // Changing Date text
                    TextView datePlainTV = (TextView)view.findViewById(R.id.datePlainText);
                    datePlainTV.setText("Date From");

                    dateRange = true;
                }
                else
                {
                    LinearLayout dateToLayout = (LinearLayout)view.findViewById(R.id.dateToLayout);
                    dateToLayout.setVisibility(View.GONE);

                    // Changing Date From text
                    TextView datePlainTV = (TextView)view.findViewById(R.id.datePlainText);
                    datePlainTV.setText("Date");

                    dateRange = false;
                }
                break;
        }
    }

    public void GetStatistics(Date dateFrom, Date dateTo) {
        //Log.e("StatisticsFragment","GetStatistics(" + dateFrom + ", " + dateTo + ")");
        activity = (MainActivity)getActivity();
        dataDisplayed = false;
        if(activity == null)
            return;

        //Log.e("StatisticsFragment", "Date from: " + dateFrom + ", date to: " + dateTo);

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
        int shallow = Integer.MAX_VALUE;

        // Creating dictionary that will store earthquake IDs as values for above statistics
        Hashtable<String, Integer> statsDict = new Hashtable<String, Integer>();
        statsDict.put("North", 0);
        statsDict.put("West", 0);
        statsDict.put("East", 0);
        statsDict.put("South", 0);
        statsDict.put("Magnitude", 0);
        statsDict.put("Deepest", 0);
        statsDict.put("Shallowest", 0);

        // Format to match XML's date format
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");

        int totalEarthQuakes = 0;

        for (int i = 0; i < activity.ListEarthQuakes.size(); i++) {
            EarthQuake earthQuake = activity.ListEarthQuakes.get(i);
            Date eDate = new Date();
            try {
                eDate = sdf.parse(earthQuake.eDate);
                // Zeroing the times to make both dates inclusive
                eDate.setHours(0);
                eDate.setMinutes(0);
                eDate.setSeconds(0);
            } catch (ParseException e) {
                Log.e("StatisticsFragment", "ParseException eDate " + e.getMessage());
            }

            //Log.e("StatisticsFragment", i + " String date: " + earthQuake.eDate + ", Date: " + eDate);

            /*
            if(eDate.after(dFrom))
                Log.e("StatisticsFragment", i + " eDate " + eDate + " is after dFrom " + dFrom);
            if(eDate.before(dTo))
                Log.e("StatisticsFragment", i + " eDate " + eDate + " is before dTo " + dTo);
            if(eDate.equals(dFrom))
                Log.e("StatisticsFragment", i + " eDate " + eDate + " is equal to dFrom " + dFrom);
            if(eDate.equals(dTo))
                Log.e("StatisticsFragment", i + " eDate " + eDate + " is equal to dTo " + dTo);
            */

            if ((eDate.after(dateFrom) && eDate.before(dateTo)) || eDate.equals(dateFrom) || eDate.equals(dateTo)) {
                totalEarthQuakes++;
                if (earthQuake.latitude > north) {
                    statsDict.put("North", earthQuake.earthQuakeID);
                    north = earthQuake.latitude;
                }
                if (earthQuake.longitude > east) {
                    statsDict.put("East", earthQuake.earthQuakeID);
                    east = earthQuake.longitude;
                }
                if (earthQuake.latitude < south) {
                    statsDict.put("South", earthQuake.earthQuakeID);
                    south = earthQuake.latitude;
                }
                if (earthQuake.longitude < west) {
                    statsDict.put("West", earthQuake.earthQuakeID);
                    west = earthQuake.longitude;
                }
                if (earthQuake.magnitude > mag) {
                    statsDict.put("Magnitude", earthQuake.earthQuakeID);
                    mag = earthQuake.magnitude;
                }
                if (earthQuake.depth > deep) {
                    statsDict.put("Deepest", earthQuake.earthQuakeID);
                    deep = earthQuake.depth;
                }
                if (earthQuake.depth < shallow) {
                    statsDict.put("Shallowest", earthQuake.earthQuakeID);
                    shallow = earthQuake.depth;
                }
            }
        }

        /*Log.e("StatisticsFragment", "Total earth quakes within date range: " + totalEarthQuakes);
        Log.e("StatisticsFragment", "Most northerly " + statsDict.get("North"));
        Log.e("StatisticsFragment", "Most easterly " + statsDict.get("East"));
        Log.e("StatisticsFragment", "Most southerly " + statsDict.get("South"));
        Log.e("StatisticsFragment", "Most westerly " + statsDict.get("West"));
        Log.e("StatisticsFragment", "Highest magnitude " + statsDict.get("Magnitude"));
        Log.e("StatisticsFragment", "Deepest " + statsDict.get("Deepest"));
        Log.e("StatisticsFragment", "Shallowest " + statsDict.get("Shallowest"));*/
        if(totalEarthQuakes > 0)
        {
            ShowStats("North", activity.getEarthQuakeByID(statsDict.get("North")));
            ShowStats("East", activity.getEarthQuakeByID(statsDict.get("East")));
            ShowStats("South", activity.getEarthQuakeByID(statsDict.get("South")));
            ShowStats("West", activity.getEarthQuakeByID(statsDict.get("West")));
            ShowStats("Magnitude", activity.getEarthQuakeByID(statsDict.get("Magnitude")));
            ShowStats("Deepest", activity.getEarthQuakeByID(statsDict.get("Deepest")));
            ShowStats("Shallowest", activity.getEarthQuakeByID(statsDict.get("Shallowest")));
            dataDisplayed = true;
        }
        else
        {
            LinearLayout statsDetails = (LinearLayout)view.findViewById(R.id.statsDetails);
            statsDetails.setVisibility(View.GONE);
            ShowError("No earthquakes found within specified date/s.");
        }
    }

    public void ShowStats(String name, EarthQuake earthQuake)
    {
        LinearLayout statsDetails = (LinearLayout)view.findViewById(R.id.statsDetails);
        statsDetails.setVisibility(View.VISIBLE);
        switch(name)
        {
            case "North":
                TextView northDetails = (TextView)view.findViewById(R.id.northDetails);
                TextView northDetailsVal = (TextView)view.findViewById(R.id.northDetailsValue);
                northDetails.setText(earthQuake.location);
                northDetailsVal.setText("Latitude: " + earthQuake.latitude);
                break;
            case "East":
                TextView eastDetails = (TextView)view.findViewById(R.id.eastDetails);
                TextView eastDetailsVal = (TextView)view.findViewById(R.id.eastDetailsValue);
                eastDetails.setText(earthQuake.location);
                eastDetailsVal.setText("Longitude: " + earthQuake.longitude);
                break;
            case "South":
                TextView southDetails = (TextView)view.findViewById(R.id.southDetails);
                TextView southDetailsVal = (TextView)view.findViewById(R.id.southDetailsValue);
                southDetails.setText(earthQuake.location);
                southDetailsVal.setText("Latitude: " + earthQuake.latitude);
                break;
            case "West":
                TextView westDetails = (TextView)view.findViewById(R.id.westDetails);
                TextView westDetailsVal = (TextView)view.findViewById(R.id.westDetailsValue);
                westDetails.setText(earthQuake.location);
                westDetailsVal.setText("Longitude: " + earthQuake.longitude);
                break;
            case "Magnitude":
                TextView magnitudeDetails = (TextView)view.findViewById(R.id.magnitudeDetails);
                TextView magnitudeDetailsVal = (TextView)view.findViewById(R.id.magnitudeDetailsValue);
                magnitudeDetails.setText(earthQuake.location);
                magnitudeDetailsVal.setText("Magnitude: " + earthQuake.magnitude);
                break;
            case "Deepest":
                TextView deepestDetails = (TextView)view.findViewById(R.id.deepestDetails);
                TextView deepestDetailsVal = (TextView)view.findViewById(R.id.deepestDetailsValue);
                deepestDetails.setText(earthQuake.location);
                deepestDetailsVal.setText("Depth: " + earthQuake.depth + "km");
                break;
            case "Shallowest":
                TextView shallowestDetails = (TextView)view.findViewById(R.id.shallowestDetails);
                TextView shallowestDetailsVal = (TextView)view.findViewById(R.id.shallowestDetailsValue);
                shallowestDetails.setText(earthQuake.location);
                shallowestDetailsVal.setText("Depth: " + earthQuake.depth + "km");
                break;
            default: break;
        }
    }

}
