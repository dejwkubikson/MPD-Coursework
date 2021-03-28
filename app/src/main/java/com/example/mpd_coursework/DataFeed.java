package com.example.mpd_coursework;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


// Dawid Kubiak (S1717751)
public class DataFeed extends AsyncTask {
    private String urlSource = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    ArrayList<EarthQuake> earthQuakes = new ArrayList<EarthQuake>();

    int currentID = 0;

    @Override
    protected Object doInBackground(Object[] objects)
    {
        try{
            URL url = new URL(urlSource);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();

            // Get XML from input stream
            xpp.setInput(getInputStream(url), "UTF_8");
            int eventType = xpp.getEventType();
            boolean insideItem = false;

            EarthQuake earthQuake = new EarthQuake();
            while(eventType != XmlPullParser.END_DOCUMENT)
            {
                if(eventType == XmlPullParser.START_TAG)
                {
                    if(xpp.getName().equalsIgnoreCase("item"))
                    {
                        insideItem = true;
                        earthQuake = new EarthQuake();
                        currentID++;
                        earthQuake.earthQuakeID = currentID;
                    }
                    else if(xpp.getName().equalsIgnoreCase("title"))
                    {
                        if(insideItem)
                        {
                            // Used for testing purposes
                            //Log.e("DataFeed", "Title " + xpp.nextText());
                        }
                    }
                    else if(xpp.getName().equalsIgnoreCase("description"))
                    {
                        if(insideItem)
                        {
                            String description = xpp.nextText();

                            // Extracting location - looking for "Location:" and ";"
                            earthQuake.location = description.substring(description.indexOf("Location:") + 9, (description.indexOf(";", description.indexOf("Location:") + 9))).trim();
                            //Log.e("DataFeed", "location " + earthQuake.location);

                            // Extracting magnitude - looking for "Magnitude:"
                            earthQuake.magnitude = Float.parseFloat(description.substring(description.indexOf("Magnitude:") + 10).trim());
                            //Log.e("DataFeed", "magnitude " + earthQuake.magnitude);

                            // Extracting depth - looking for "Depth:" and "km"
                            earthQuake.depth = Integer.parseInt(description.substring(description.indexOf("Depth:") + 6, (description.indexOf("km", description.indexOf("Depth:") + 6))).trim());
                            //Log.e("DataFeed", "depth " + earthQuake.depth);
                        }
                    }
                    else if(xpp.getName().equalsIgnoreCase("pubDate"))
                    {
                        if(insideItem)
                        {
                            earthQuake.eDate = xpp.nextText();
                            //Log.e("DataFeed", "eDate " + earthQuake.eDate);
                        }
                    }
                    else if(xpp.getName().equalsIgnoreCase("geo:lat"))
                    {
                        if(insideItem)
                        {
                            earthQuake.latitude = Float.parseFloat(xpp.nextText());
                            //Log.e("DataFeed", "latitude " + earthQuake.latitude);
                        }
                    }
                    else if(xpp.getName().equalsIgnoreCase("geo:lat"))
                    {
                        if(insideItem)
                        {
                            earthQuake.longitude = Float.parseFloat(xpp.nextText());
                            //Log.e("DataFeed", "longitude " + earthQuake.longitude);
                        }
                    }
                }
                else if(eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item"))
                {
                    insideItem = false;
                    earthQuakes.add(earthQuake);
                }

                eventType = xpp.next(); // Moving to next element
            }

            //Log.e("DataFeed", "EarthQuakes Length: " + earthQuakes.size());
        }
        catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (XmlPullParserException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return earthQuakes;
    }

    public InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    }

    public ArrayList<EarthQuake> getEarthQuakes(){return earthQuakes;}
}


