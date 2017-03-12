package com.udacity.androidcourse.egmp777.popularmovies.utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by elena on 3/7/17.
 * This is a utility class with methods to handle the JSON response Data
 */

public final class TheMovieDBJsonUtils {


    /**
     * This method converts the JSON response into a String array
     * @param context
     * @param movieJsonStr
     * @return String array thet holds each element of the movie info that comes in the JSON response
     * @throws JSONException
     */
    public static String [] getMovieDetailsStringFromJson(Context context, String movieJsonStr)
                    throws JSONException {

        final String OWM_SYNOPSYS = "overview";
        final String OWM_ORIGINAL_TITLE = "original_title";
        final String OWM_POSTER_PATH = "poster_path";
        final String OWM_USER_RATING = "vote_average";
        final String OWM_RELEASE_DATE = "release_date";

        final String OWM_TMDB_STATUS_CODE = "status_code";

        /* The JSON array where all the movie arrays are returned */
        final String OWM_RESULTS = "results";

        final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185";

        /* Holds the details for one movie */
        String [] parsedMovieData = null;

        JSONObject movieJson = new JSONObject(movieJsonStr);

        /* Handling error codes*/
        if (movieJson.has(OWM_TMDB_STATUS_CODE)){

            int jsonError = movieJson.getInt(OWM_TMDB_STATUS_CODE);
            switch(jsonError){
                case 1:
                    break;
                default:

                    return null;

            }

        }
        JSONArray movies = movieJson.getJSONArray(OWM_RESULTS);
        parsedMovieData = new String[movies.length()];


        for (int i = 0; i < movies.length(); i++) {
            //Getting each movie array
            JSONObject oneMovie = movies.getJSONObject(i);
            String poster_relative_path = oneMovie.getString(OWM_POSTER_PATH);
            String poster = POSTER_BASE_URL + poster_relative_path;
            String overview = oneMovie.getString(OWM_SYNOPSYS);
            String originalTitle = oneMovie.getString(OWM_ORIGINAL_TITLE);
            String userRating = String.valueOf(oneMovie.getInt(OWM_USER_RATING));
            String releaseDate = oneMovie.getString(OWM_RELEASE_DATE);

            parsedMovieData[i] = poster + "\n" + overview + "\n" + originalTitle
                    + "\n" + userRating + "\n" + releaseDate;

        }
        return parsedMovieData;
    }
}
