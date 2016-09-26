package com.amadeus.myweatherapp.home.model;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Timestamp;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by aappukuttan on 9/24/2016.
 */

public class GetForecastsParser extends AsyncTask<Void, Void, ArrayList<ForecastModel>> implements AsyncLoader.LoaderListener{

    private static final String TAG = "GetForecastsParser";
    String responseString;
    AsyncLoader loader;

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

    public void getForecasts() {
        loader = new AsyncLoader(AsyncLoader.GET, AsyncLoader.OUTPUT_TEXT);
        loader.executeRequest("http://openweathermap.org/data/2.5/forecast/daily?lat=35&lon=139&cnt=7&mode=json&appid=b1b15e88fa797225412429c1c50c122a1");
        loader.setListener(this);
    }

    @Override
    protected ArrayList<ForecastModel> doInBackground(Void... params) {

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
                    Date d = new Date(timestamp);

                    ForecastModel forecastModel = new ForecastModel();

                    //forecastModel.day = dataObject.getString("");
                    forecastModel.day = d.toString().substring(0,10);
                    forecastModel.temperature = temperatureObject.getString("min")+ "\u00b0C / "+temperatureObject.getString("max")+"°C";
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
