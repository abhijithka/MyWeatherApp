package com.amadeus.myweatherapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amadeus.myweatherapp.home.model.AsyncLoader;
import com.amadeus.myweatherapp.home.model.ForecastModel;
import com.amadeus.myweatherapp.home.model.ForecastProvider;
import com.amadeus.myweatherapp.home.model.GetForecastsParser;
import com.amadeus.myweatherapp.home.model.WeatherDetailActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements GetForecastsParser.GetForecastsParserListener, View.OnClickListener, AdapterView.OnItemClickListener {

    ArrayList<ForecastModel> forecasts;
    GetForecastsParser parser;
    TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        //ForecastProvider forecastProvider = new ForecastProvider();
        //forecasts = forecastProvider.getForecasts();

        //Log.e("MainActivity", ""+forecasts.size());
        parser = new GetForecastsParser();
        parser.getForecasts();
        parser.setListener(this);

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        titleTextView.setOnClickListener(this);



    }

    @Override
    public void didReceivedForecasts(ArrayList<ForecastModel> forecasts) {

        this.forecasts = forecasts;

        ListView listView = (ListView) findViewById(R.id.forecastListView);
        listView.setAdapter(new forecastListAdapter());
        listView.setOnItemClickListener(this);
    }

    @Override
    public void didReceivedError() {
        Log.e("MainActivity", "didReceivedError");
    }

    @Override
    public void onClick(View v) {
        parser = new GetForecastsParser();
        parser.getForecasts();
        parser.setListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ForecastModel forecast = forecasts.get(position);
        Intent intent = new Intent(this, WeatherDetailActivity.class);
        intent.putExtra("day number", position );
        intent.putExtra("forecast", forecast);
        startActivity(intent);
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

            if(convertView == null) {
                ViewHolder viewHolder = new ViewHolder();

                convertView = layoutInflater.inflate(R.layout.forecast_content, null);
                viewHolder.forecastDayTexView = (TextView) convertView.findViewById(R.id.forecastDayTexView);
                viewHolder.forecastImageView = (ImageView) convertView.findViewById(R.id.forecastImageView);
                viewHolder.weatherTextView = (TextView) convertView.findViewById(R.id.weatherTextView);
                viewHolder.temperatureTextView = (TextView) convertView.findViewById(R.id.temperatureTextView);

                convertView.setTag(viewHolder);
            }

            ForecastModel forecast = forecasts.get(position);

            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            //Log.e("MainActivity", position+": "+forecast.day+" "+forecast.weather);
            //viewHolder.forecastImageView.setImageURI("");
            viewHolder.forecastDayTexView.setText(forecast.day);
            viewHolder.weatherTextView.setText(forecast.weather);
            viewHolder.temperatureTextView.setText(forecast.temperature);
            //Log.e("MainActivity", forecast.weather);

            if (forecast.weather.equals("Rain")){
                //Log.e("MainActivity", "forecasted");
                viewHolder.forecastImageView.setBackgroundResource(R.drawable.rain);
            }
            else if (forecast.weather.equals("Clear")){
                viewHolder.forecastImageView.setBackgroundResource(R.drawable.sun);
            }

            return convertView;
        }
    }

}
