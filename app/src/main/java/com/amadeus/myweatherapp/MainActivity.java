package com.amadeus.myweatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<ForecastModel> forecasts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        ForecastProvider forecastProvider = new ForecastProvider();
        forecasts = forecastProvider.getForecasts();

        Log.e("MainActivity", ""+forecasts.size());
        ListView listView = (ListView) findViewById(R.id.forecastListView);
        listView.setAdapter(new forecastListAdapter());

    }

    class ViewHolder{
        ImageView forecastImageView;
        TextView forecastDayTexView;
        TextView temperatureTextView;
        TextView weatherTextView;
    }

    class forecastListAdapter extends BaseAdapter{

        private LayoutInflater layoutInflater;

        public forecastListAdapter(){
            //Log.e("MainActivity", "Constructor called");
            layoutInflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return forecasts.size();
        }

        @Override
        public Object getItem(int position) {
            return forecasts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Log.e("MainActivity", "getView() called");

            ViewHolder viewHolder = new ViewHolder();

            convertView = layoutInflater.inflate(R.layout.forecast_content, null);
            viewHolder.forecastDayTexView = (TextView) convertView.findViewById(R.id.forecastDayTexView);
            viewHolder.forecastImageView = (ImageView) convertView.findViewById(R.id.forecastImageView);
            viewHolder.weatherTextView = (TextView) convertView.findViewById(R.id.weatherTextView);
            viewHolder.temperatureTextView = (TextView) convertView.findViewById(R.id.temperatureTextView);

            ForecastModel forecast = forecasts.get(position);

            //Log.e("MainActivity", position+": "+forecast.day+" "+forecast.weather);
            //viewHolder.forecastImageView.setImageURI("");
            viewHolder.forecastDayTexView.setText(forecast.day);
            viewHolder.weatherTextView.setText(forecast.weather);
            viewHolder.temperatureTextView.setText(forecast.temperature);

            return convertView;
        }
    }
}
