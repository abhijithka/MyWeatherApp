package com.amadeus.myweatherapp.home.model;

import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.compat.BuildConfig;
import android.util.Log;

import com.amadeus.myweatherapp.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.Timestamp;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by aappukuttan on 9/24/2016.
 */

public class GetForecastsParser extends AsyncTask<String, Void, ArrayList<ForecastModel>> implements AsyncLoader.LoaderListener{

    private static final String TAG = GetForecastsParser.class.getSimpleName();
    String responseString;
    AsyncLoader loader;
    int temperature_unit;

    @Override
    public void didReceivedData(byte[] data) {
        responseString = new String(data);
        execute();
    }

    @Override
    public void didReceivedLoaderError() {
        if(listener != null){
            listener.didReceivedError();
        }
    }

    public interface GetForecastsParserListener{
        void didReceivedForecasts(ArrayList<ForecastModel> forecasts);
        void didReceivedError();
    }

    private GetForecastsParserListener listener;

    public GetForecastsParserListener getListener() {
        return listener;
    }

    public void setListener(GetForecastsParserListener listener) {
        this.listener = listener;
    }

    public void getForecasts(String cityId, int temp_unit) {
        temperature_unit = temp_unit;
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences();
        String format = "json";
        String units = "metric" ;
        if (temp_unit == 0){
            units = "metric";
        }
        else if (temp_unit ==1 ) {
            units = "imperial";
        }
        int numDays = 7;
        String apiKey = "9e6eea1e850eb62ec6e176bfd57267fe";

        // Additional check for our custom parser - if the city id passed is null or not
        if(cityId.length() != 0){
            final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
            final String QUERY_PARAM = "q";
            final String FORMAT_PARAM = "mode";
            final String UNITS_PARAM = "units";
            final String DAYS_PARAM = "cnt";
            final String APPID_PARAM = "APPID";
            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon().appendQueryParameter(QUERY_PARAM, cityId)
                              .appendQueryParameter(FORMAT_PARAM, format)
                              .appendQueryParameter(UNITS_PARAM, units)
                              .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                              .appendQueryParameter(APPID_PARAM, apiKey)
                              .build();

            try {
                URL url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            Log.v(TAG, "Built URI " + builtUri.toString());
            loader = new AsyncLoader(AsyncLoader.GET, AsyncLoader.OUTPUT_TEXT);
            loader.executeRequest(builtUri.toString());
        }



        //loader.executeRequest("http://api.openweathermap.org/data/2.5/forecast/daily?id=1277333&mode=json&units=metric&cnt=7&APPID=9e6eea1e850eb62ec6e176bfd57267fe");
        //loader.executeRequest("http://api.openweathermap.org/data/2.5/forecast/daily?id=580597&mode=json&units=metric&cnt=7&APPID=9e6eea1e850eb62ec6e176bfd57267fe");
        loader.setListener(this);
    }

    @Override
    protected ArrayList<ForecastModel> doInBackground(String... params) {

        try {
            // Process the ResponseString
            JSONObject jsonObject = new JSONObject(responseString);
            int Status = jsonObject.getInt("cod");

            if (Status == 200) {

                JSONArray data = jsonObject.getJSONArray("list");
                ArrayList<ForecastModel> forecasts = new ArrayList<>();

                for (int i = 0; i < data.length(); i++) {

                    JSONObject dataObject = data.getJSONObject(i);
                    JSONObject temperatureObject = dataObject.getJSONObject("temp");
                    JSONObject weatherObject = dataObject.getJSONArray("weather").getJSONObject(0);
                    Long timestamp = Long.parseLong(dataObject.getString("dt"));
                    Date d = new Date(timestamp*1000L);

                    ForecastModel forecastModel = new ForecastModel();

                    //forecastModel.day = dataObject.getString("");
                    String date = d.toString() ;
                    forecastModel.day = date.substring(0,3)+ ",  " + date.substring(4,10) ;
                    if (temperature_unit == 0) {
                        forecastModel.temperature = temperatureObject.getString("min") + "\u00b0C / " + temperatureObject.getString("max") + "°C";
                    }
                    else if (temperature_unit == 1){
                        forecastModel.temperature = temperatureObject.getString("min") + "F / " + temperatureObject.getString("max") + "F";
                    }
                    forecastModel.weather = weatherObject.getString("main");

                    forecasts.add(forecastModel);
                }
                return forecasts;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    protected void onPostExecute(ArrayList<ForecastModel> forecastModels) {
        super.onPostExecute(forecastModels);

        if(listener != null){
            if(forecastModels == null){
                listener.didReceivedError();
            }
            else {
                listener.didReceivedForecasts(forecastModels);
            }
        }
    }
}
