package com.amadeus.myweatherapp.home.model;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.amadeus.myweatherapp.R;

public class WeatherDetailActivity extends AppCompatActivity {

    TextView dateTextView ;
    TextView tempTextView;
    TextView weatherTypeTextView;
    TextView windTextView;
    private static final String LOG_TAG = WeatherDetailActivity.class.getSimpleName();

    private static final String FORECAST_SHARE_HASHTAG = "#WeatherNow";
    private String mForecastStr;

    public WeatherDetailActivity(){

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);

        //getSupportActionBar().hide();

        ForecastModel forecast = getIntent().getParcelableExtra("forecast");

        if (forecast != null) {
            Log.d("WeatherDetailActivity", forecast.day);
            dateTextView = (TextView) findViewById(R.id.dateTextView);
            dateTextView.setText(forecast.day);
            tempTextView = (TextView) findViewById(R.id.tempTextView);
            windTextView = (TextView) findViewById(R.id.windTextView);
            weatherTypeTextView = (TextView) findViewById(R.id.weatherTypeTextView);
            tempTextView.setText(forecast.temperature);
            weatherTypeTextView.setText(forecast.weather);
            windTextView.setText(forecast.wind);
            mForecastStr = forecast.day+" - "+ forecast.weather + " - " + forecast.temperature + " ";
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        Log.e(LOG_TAG, "I am here");

        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(createShareForecastIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is missing");
        }
        return true;
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastStr + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }
}
