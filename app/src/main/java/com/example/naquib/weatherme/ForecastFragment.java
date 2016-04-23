package com.example.naquib.weatherme;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Manifest;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    private ArrayAdapter<String> forecastAdapter;


    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ArrayAdapter<String> mDrawerAdapter;

    private String[] mPlanetTitles = {"Home", "Get HitMap", "Location"};

    public ForecastFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview =  inflater.inflate(R.layout.fragment_main, container, false);



        String [] forecastData = {
                "Today  - Sunny - 88/33",
                "Tomorrow  - Sunny - 78/30",
                "Wed  - Cloudy - 64/21",
                "Thurs  - Rainy - 67/25",
                "Fri  - Sunny - 85/32",
                "Sat  - Sunny - 79/30",
                "Sun  - Sunny - 82/31",
                "Mon  - Cloudy - 68/26",
                "Tue  - Rainy - 66/25"
        };


        ArrayList<String> list = new ArrayList<>();

        Arrays.asList(forecastData);

        ListView listView = (ListView) rootview.findViewById(R.id.listview_forecast);

        //adapter
        forecastAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_forecast, R.id.list_item_forecast_textview, forecastData);

       // Map latLong = locationLatLong();
        //Log.d("LAtitude and Longitude","-------->"+ latLong.get("Latitude") + "------->" + latLong.get("Longitude"));

        listView.setAdapter(forecastAdapter);



        //-----network call----





        return rootview;
    }
/*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_refresh) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/
    public Map locationLatLong() throws SecurityException {


        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

//        if(ContextCompat.checkSelfPermission(getActivity(), ))

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Map<String, Double> latLong = new HashMap<>();
        if(location != null) {

            latLong.put("Latitude", location.getLatitude());
            latLong.put("Longitude", location.getLongitude());


        }

    return latLong;
    }
}



class FetchWeatherTask extends AsyncTask<Void, Void, Void> {

    private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

    @Override
    protected Void doInBackground(Void... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String forecastjson = null;

        try {

            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/city?id=1337179&APPID=aac866fda4f730946c97efee8cc050bf");

            urlConnection= (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if(inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while((line=reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if(buffer.length() == 0) {
                return null;
            }

            forecastjson = buffer.toString();



        } catch (Exception err) {

            Log.e(LOG_TAG, "Error: ", err);
            return null;

        } finally {

            if(urlConnection != null) urlConnection.disconnect();
            if(reader != null)
                try {
                    reader.close();
                } catch (final IOException err) {
                    Log.e("MainActivityFragnment","Error closing stream", err);
                }
        }


        return null;
    }



}
