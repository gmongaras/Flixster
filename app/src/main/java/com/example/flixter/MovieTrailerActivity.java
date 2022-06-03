package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);

        // Using the intent, get the movie from that intent and unwrap it
        Movie movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));

        // resolve the player view from the layout
        YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);

        // initialize with API key stored in secrets.xml
        playerView.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {
                // Make an instance of AsyncHttpClient
                AsyncHttpClient client = new AsyncHttpClient();

                // API call to get the youtube video
                String URL = "https://api.themoviedb.org/3/movie/" + String.valueOf(movie.getId()) + "/videos?api_key=" + getString(R.string.themoviedb_api_key);
                client.get(URL, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        // Get the JSON data
                        JSONObject jsonObject = json.jsonObject;

                        // Get the query results
                        try {
                            // Get the JSON data for the video
                            JSONArray results = jsonObject.getJSONArray("results");

                            // Get the movie Id
                            final String videoId = results.getJSONObject(0).getString("key");

                            // do any work here to cue video, play video, etc.
                            youTubePlayer.cueVideo(videoId);

                        } catch (JSONException e) {
                            // If the key does not exist, log the error
                            Log.e("MovieTrailerActivity", "JSON Key Exception", e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.d("MovieTrailerActivity", "onFailure");
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                // log the error
                Log.e("MovieTrailerActivity", "Error initializing YouTube player");
            }
        });
    }
}