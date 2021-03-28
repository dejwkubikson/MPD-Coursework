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
    ArrayList<String> headlines = new ArrayList();
    ArrayList<String> descriptions = new ArrayList();

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
            while(eventType != XmlPullParser.END_DOCUMENT)
            {
                if(eventType == XmlPullParser.START_TAG)
                {
                    if(xpp.getName().equalsIgnoreCase("item"))
                    {
                        insideItem = true;
                    }
                    else if(xpp.getName().equalsIgnoreCase("title"))
                    {
                        if(insideItem)
                        {
                            String headline = xpp.nextText();
                            headlines.add(headline); // Extracting the headline
                            Log.e("MyTag", "Description " + headline);
                        }
                    }
                    else if(xpp.getName().equalsIgnoreCase("description"))
                    {
                        if(insideItem)
                        {
                            String description = xpp.nextText();
                            descriptions.add(description); // Extracting the description
                            Log.e("MyTag", "Description " + description);
                        }
                    }
                }
                else if(eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item"))
                {
                    insideItem = false;
                }

                eventType = xpp.next(); // Moving to next element
            }
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

        return headlines;
    }

    public InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    }

    public ArrayList<String> heads()
    {
        return headlines;
    }
}


