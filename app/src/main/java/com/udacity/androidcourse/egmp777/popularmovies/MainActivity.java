package com.udacity.androidcourse.egmp777.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.androidcourse.egmp777.popularmovies.utils.MovieDataUtils;
import com.udacity.androidcourse.egmp777.popularmovies.utils.NetworkUtils;
import com.udacity.androidcourse.egmp777.popularmovies.utils.TheMovieDBJsonUtils;

import java.net.URL;


public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler,
        PopupMenu.OnMenuItemClickListener{
    private RecyclerView mRecyclerView;

    private final String CONNECTION_ERROR = "Oops...there is no network connection to the Internet";

    private ImageView imageView;

    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    private String movieSelectionCriterion;

    private GridLayoutManager gridLayoutManager;
    // For testing
    private static final String TAG  = "OVERVIEW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_listing);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movie_list);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        int numberOfColumns = 2;
        gridLayoutManager = new GridLayoutManager(this, numberOfColumns);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);
        loadMovieData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.sort_criterion, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_criterion) {

            //show popUp menu
            View view = (View)findViewById(R.id.action_sort_criterion);
            showPopup(view);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void showPopup(View view){
        PopupMenu popupMenu = new PopupMenu(this,view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.sort_criterion_options, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_popularity:
                //movieSelectionCriterion = "popularity.desc";
                movieSelectionCriterion = "popular";
                mMovieAdapter.setMovieData(null);
                loadMovieData();
                return true;
            case R.id.action_sort_rating:
                movieSelectionCriterion = "top_rated";
                mMovieAdapter.setMovieData(null);
                loadMovieData();
                return true;
            default:
                return false;
        }
    }


    private void loadMovieData() {
        showMovieDataView();
        //TODO: place the api_key here
        //String apiKey = "";
        //new FetcMovieTask().execute(apiKey);
    }

    @Override
    public void onClick(String movieData){
        String [] testArray = MovieDataUtils.buildMovieDataArray(movieData);
        String overview = testArray[1];
        Context context = this;
        Class destinationClass = MovieDetails.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, movieData);
        startActivity(intentToStartDetailActivity);
    }

    /**
     * This method will make the View for the weather data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the weather
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


    /**
     * This inner class is in charge of executing the async task of asking for data from the TMDB server
     */
    public class FetcMovieTask extends AsyncTask<String, Void, String[]>{

        private boolean haveConnection;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);

        }

        @Override
        protected String[] doInBackground(String... params) {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if(NetworkUtils.networkConnectionAvailable(cm) == true) {



            /* If there's no zip code, there's nothing to look up. */
                if (params.length == 0) {
                    return null;
                }

                String api_key = params[0];

                URL movieRequestUrl = NetworkUtils.buildUrl(movieSelectionCriterion, api_key);

                try {
                    String jsonTMDBResponse = NetworkUtils
                            .getResponseFromHttpUrl(movieRequestUrl);

                    String[] simpleJsonWeatherData = TheMovieDBJsonUtils
                            .getMovieDetailsStringFromJson(MainActivity.this, jsonTMDBResponse);

                    return simpleJsonWeatherData;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            else{
                haveConnection = false;

            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] movieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData != null) {
                showMovieDataView();
                mMovieAdapter.setMovieData(movieData);
            } else {
                if(!haveConnection){
                    Toast.makeText(MainActivity.this, "no internet!", Toast.LENGTH_SHORT).show();
                    mErrorMessageDisplay.setText("Opps, there is no internet connection right now!");

                }
                showErrorMessage();
            }
        }
    }
}
