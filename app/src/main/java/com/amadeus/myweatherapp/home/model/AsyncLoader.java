package com.amadeus.myweatherapp.home.model;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by aappukuttan on 9/24/2016.
 */

public class AsyncLoader extends AsyncTask <String,Void,byte[]> {

    private static final String TAG = "AsyncLoader";

    public static final int GET = 1;
    public static final int POST = 2;

    public static final int OUTPUT_TEXT = 1;
    public static final int OUTPUT_IMAGE = 2;

    int methodType; //1-> GET & 2->POST
    int dataType; //1->Text & 2->Image

    public interface LoaderListener{

        void didReceivedData(byte[] data);
        void didReceivedLoaderError();
    }

    private LoaderListener listener;

    public LoaderListener getListener() {
        return listener;
    }

    public void setListener(LoaderListener listener) {
        this.listener = listener;
    }

    public AsyncLoader(int methodType, int dataType){

        this.methodType = methodType;
        this.dataType = dataType;
    }

    // If % character is not found that means we need to perform escape of
    // the percentage characters
    // so that url is properly formatted. if percentage character is found
    // that means url string already contains
    // escape character so we ignore injecting the percentage character

    // Standard code for checking formats

    public void executeRequest(String requestURL) {

        try {
            requestURL = requestURL.trim();
            if (requestURL.indexOf("%", 0) == -1) {
                URL url = new URL(requestURL);
                URI uri = null;

                uri = new URI(url.getProtocol(),
                        url.getUserInfo(), url.getHost(),
                        url.getPort(), url.getPath(),
                        url.getQuery(), url.getRef());
                url = uri.toURL();
                requestURL = String.valueOf(url);
            }

            Log.e(TAG, "Input URL -> " + requestURL);

        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.e("URISyntaxException", "e " + e);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("MalformedURLException", "e " + e);
        }

        execute(requestURL);

    }

    @Override
    protected byte[] doInBackground(String... strings) {
        if (methodType == GET && dataType == OUTPUT_TEXT) {
            //standard code follows for text
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection1 =
                        (HttpURLConnection) url.openConnection();
                if(!this.isCancelled()){
                    urlConnection1.setRequestMethod("GET");
                    urlConnection1.setDoOutput(false);

                    int statusCode = urlConnection1.getResponseCode();
                    StringBuilder response = new StringBuilder();
                    Log.e(TAG, "statusCode " + statusCode);
                    if (statusCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader r = new BufferedReader
                                (new InputStreamReader
                                        (urlConnection1.getInputStream()));
                        String line;
                        while ((line = r.readLine()) != null) {
                            response.append(line);
                        }
                        return response.toString().getBytes();
                    } else {
                        return null; //"Failed to fetch data!";
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(dataType == OUTPUT_IMAGE && methodType == GET){

            //standard code follows for image
            try {

                URL url = new URL(strings[0]);
                InputStream in = new BufferedInputStream(url.openStream());
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int n = 0;
                while (-1 != (n = in.read(buf))) {
                    out.write(buf, 0, n);
                }
                out.close();
                in.close();
                byte[] response = out.toByteArray();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("exception", "" + e);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("exception", "" + e);
            }


        }

        return null;
    }

    @Override
    protected void onPostExecute(byte[] bytes) {
        super.onPostExecute(bytes);
        if(listener != null){
            if(bytes == null){
                listener.didReceivedLoaderError();
            }
            else {
                listener.didReceivedData(bytes);
            }
        }
    }
}
