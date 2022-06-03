package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import org.parceler.Parcels;

public class MovieDetailsActivity extends AppCompatActivity {

    // The movie to display
    Movie movie;

    // The positions of the objects in the popup (activity movie details)
    RatingBar rbVoteAverage;
    TextView int_tvTitle;
    TextView int_tvOverview;
    ImageView int_ivPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Get the fields in this View
        rbVoteAverage = findViewById(R.id.rbVoteAverage);
        int_tvTitle = findViewById(R.id.int_tvTitle);
        int_tvOverview = findViewById(R.id.int_tvOverview);
        int_ivPoster = findViewById(R.id.int_ivPoster);

        // Using the intent, get the movie from that intent and unwrap it
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));

        // Log to confirm deserialization of movie
        Log.d("MovieDetailsAcvitity", String.format("Showing details for '%s'", movie.getTitle()));

        // Set the movie information
        int_tvTitle.setText(movie.getTitle());
        int_tvOverview.setText(movie.getOverview());
        rbVoteAverage.setRating(movie.getVoteAverage().floatValue()/2.0f);

        // Set the image
        String imageURL = movie.getPosterPath();
        String placeHolder = "app/imgs/flicks_movie_placeholder.png";

        // Create the image
        Glide.with(int_ivPoster)
                .load(imageURL)
                .placeholder(Drawable.createFromPath(placeHolder))
                .override(Target.SIZE_ORIGINAL/2, Target.SIZE_ORIGINAL/3)
                .into(int_ivPoster);
    }
}