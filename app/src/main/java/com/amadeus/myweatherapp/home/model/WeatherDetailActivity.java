package com.amadeus.myweatherapp.home.model;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.amadeus.myweatherapp.R;

public class WeatherDetailActivity extends AppCompatActivity {

    TextView dateTextView ;
    TextView tempTextView;
    TextView weatherTypeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);

        getSupportActionBar().hide();

        ForecastModel forecast = getIntent().getParcelableExtra("forecast");

        if (forecast != null) {
            Log.d("WeatherDetailActivity", forecast.day);
            dateTextView = (TextView) findViewById(R.id.dateTextView);
            dateTextView.setText(forecast.day);
            tempTextView = (TextView) findViewById(R.id.tempTextView);
            weatherTypeTextView = (TextView) findViewById(R.id.weatherTypeTextView);
            tempTextView.setText(forecast.temperature);
            weatherTypeTextView.setText(forecast.weather);
        }
    }
}
