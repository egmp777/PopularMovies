package com.udacity.androidcourse.egmp777.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.androidcourse.egmp777.popularmovies.utils.MovieDataUtils;

/**
 * Created by elena on 3/5/17.
 * This is the Movie Adaoter used handling the creation of GUI items and caching them them in the Recycler view
 * It also has inner class with a clickListener to add interactivity with the user of the RecyclerView
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {


    private String [] mMovieData;
    private Context context;

    /**
     * Create an OnTouchHandler to get the pad on the RecyclerView
     */
    private final MovieAdapterOnClickHandler mTouchHandler;

    /**
     * The interface that receives onTouch messages.
     */
    public interface MovieAdapterOnClickHandler {
        void onClick(String selectedMovieData);
    }

    public MovieAdapter(MovieAdapterOnClickHandler touchHandler){
        mTouchHandler = touchHandler;

    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mMovieImageView;

        public MovieAdapterViewHolder(View view) {
            super(view);
            mMovieImageView = (ImageView) view.findViewById(R.id.iv_movie_poster);
            view.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            String movieData = mMovieData[adapterPosition];
            String [] selectedMovieArray = MovieDataUtils.buildMovieDataArray(movieData);
             mTouchHandler.onClick(movieData);
        }
    }
        /**
         * This gets called when each new ViewHolder is created. This happens when the RecyclerView
         * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
         *
         * @param viewGroup The ViewGroup that these ViewHolders are contained within.
         * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
         *                  can use this viewType integer to provide a different layout. See
         *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
         *                  for more details.
         * @return A new ForecastAdapterViewHolder that holds the View for each list item
         */
        @Override
        public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            context = viewGroup.getContext();
            int layoutIdForListItem = R.layout.movie_item;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
            return new MovieAdapterViewHolder(view);
        }


    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param movieAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        String movieData = mMovieData[position];
        String [] selectedMovieDataPieces = MovieDataUtils.buildMovieDataArray(movieData);
        String poster = selectedMovieDataPieces[0];
        Picasso.with(context).load(poster).into(movieAdapterViewHolder.mMovieImageView);
    }


    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (mMovieData == null) return 0;
        return mMovieData.length;
    }

    public void setMovieData(String[] movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }

}


