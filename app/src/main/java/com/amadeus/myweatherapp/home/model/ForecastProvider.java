package com.amadeus.myweatherapp.home.model;

import java.util.ArrayList;

/**
 * Created by aappukuttan on 9/24/2016.
 */

public class ForecastProvider {

    public ArrayList<ForecastModel> getForecasts(){
        ArrayList<ForecastModel> forecasts = new ArrayList<>();

        ForecastModel forecastModel1 = new ForecastModel();
        forecastModel1.day = "Sun - 22 Jun";
        forecastModel1.temperature = "22/28";
        forecastModel1.weather = "Sunny";
        forecasts.add(forecastModel1);

        ForecastModel forecastModel2 = new ForecastModel();
        forecastModel2.day = "Mon - 23 Jun";
        forecastModel2.temperature = "22/28";
        forecastModel2.weather = "Cloudy";
        forecasts.add(forecastModel2);

        ForecastModel forecastModel3 = new ForecastModel();
        forecastModel3.day = "Tue - 24 Jun";
        forecastModel3.temperature = "22/28";
        forecastModel3.weather = "Breezy";
        forecasts.add(forecastModel3);

        ForecastModel forecastModel4 = new ForecastModel();
        forecastModel4.day = "Wed - 25 Jun";
        forecastModel4.temperature = "22/28";
        forecastModel4.weather = "Sunny";
        forecasts.add(forecastModel4);

        //Log.e("ForecastProvider", " "+forecasts.size());
        //Log.e("ForecastProvider", ""+forecasts.get(0).day+" "+forecasts.get(1).day+" "+forecasts.get(2).weather);
        return forecasts;
    }
}
