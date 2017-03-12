package com.udacity.androidcourse.egmp777.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.androidcourse.egmp777.popularmovies.utils.MovieDataUtils;

/**
 * Activity that displays the details of the selected movie
 */
public class MovieDetails extends AppCompatActivity {

    private String overview;
    private TextView mMovieOverview;
    private String [] mMovieData;
    private String mMovieString;
    private TextView mOriginalTitle;
    private ImageView mPosterImage;
    private TextView mReleaseDate;
    private TextView mRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        mMovieOverview = (TextView)findViewById(R.id.tv_movie_overview);
        mOriginalTitle = (TextView)findViewById(R.id.tv_original_title);
        mPosterImage = (ImageView)findViewById(R.id.iv_thumbnail_poster);
        mReleaseDate = (TextView)findViewById(R.id.tv_release_date);
        mRating = (TextView) findViewById(R.id.tv_user_rating);
        Intent intentComingFormMain = getIntent();

        if(intentComingFormMain != null){
            if(intentComingFormMain.hasExtra(Intent.EXTRA_TEXT)){
                mMovieString = intentComingFormMain.getStringExtra(Intent.EXTRA_TEXT);

            }
        }

        String [] movieDataArray = MovieDataUtils.buildMovieDataArray(mMovieString);
        mMovieOverview.setText(movieDataArray[1]);
        mOriginalTitle.setText(movieDataArray[2]);
        Picasso.with(this).load(movieDataArray[0]).into(mPosterImage);
        mRating.setText(movieDataArray[3]);
        mReleaseDate.setText(movieDataArray[4]);
    }


}
