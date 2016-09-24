package com.amadeus.myweatherapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aappukuttan on 9/24/2016.
 */

public class ForecastModel implements Parcelable {
    public String day;
    public String weather;
    public String temperature;

    public ForecastModel() {

    }

    protected ForecastModel(Parcel in) {
        day = in.readString();
        weather = in.readString();
        temperature = in.readString();
    }

    public static final Creator<ForecastModel> CREATOR = new Creator<ForecastModel>() {
        @Override
        public ForecastModel createFromParcel(Parcel in) {
            return new ForecastModel(in);
        }

        @Override
        public ForecastModel[] newArray(int size) {
            return new ForecastModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(day);
        dest.writeString(weather);
        dest.writeString(temperature);
    }
}
