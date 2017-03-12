package com.udacity.androidcourse.egmp777.popularmovies.utils;

/**
 * Created by elena on 3/9/17.
 * This is a utility class that converts new line separated Strings into a String array
 */

public final class MovieDataUtils {

    private static final String TAG = MovieDataUtils.class.getSimpleName();

    /**
     * This method splits a String and places each substring obtained in a String array
     * @param movieData
     * @return
     */
    public static String [] buildMovieDataArray(String movieData){

        String [] oneMovieDataArray;
        oneMovieDataArray = movieData.split("\n");
        return oneMovieDataArray;
    }

}
