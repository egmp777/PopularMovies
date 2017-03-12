package com.udacity.androidcourse.egmp777.popularmovies.utils;

import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;

import com.udacity.androidcourse.egmp777.popularmovies.MainActivity;
import com.udacity.androidcourse.egmp777.popularmovies.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static android.provider.Settings.Global.getString;


/**
 * Created by elena on 3/6/17.
 * Utilitiy class to communicate with TMB server
 */

public final class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();


    private static final String API_KEY = "api_key";
    private static final String DEFAULT_SORT_CRITERION = "popular";
    private static final String MOVIE_DATABASE_URL = "https://api.themoviedb.org/3/movie/";

    /**
     * Builds the URL used to talk to the weather server using a location. This location is based
     * on the query capabilities of the weather provider that we are using.
     *
     * @param criterion The criteria for searchinh movies.
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrl(String criterion, String api_key) {


        if (criterion == null || criterion == ""){
            criterion = DEFAULT_SORT_CRITERION;
        }
        Uri builtUri = Uri.parse(MOVIE_DATABASE_URL + criterion).buildUpon()
                .appendQueryParameter(API_KEY, api_key)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * This mmethod checks if there is network availability
     *
     * @param cm the ConnectivityManager
     * @return The boolean response of checking network connection availability
     *
     */
    public static boolean networkConnectionAvailable(ConnectivityManager cm) {

        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;

    }

}
