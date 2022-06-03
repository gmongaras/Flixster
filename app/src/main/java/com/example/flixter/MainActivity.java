package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=cf82121809bcfadca6cd07b6efebebf8";
    public static final String TAG = "MainActivity";


    // Movies loaded from the API call
    List<Movie> movies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize movies to an empty list
        movies = new ArrayList<>();

        // Define the Recycler View
        RecyclerView rvMovies = findViewById(R.id.rvMovies);

        // Create the adapter
        MovieAdapter adapter = new MovieAdapter(this, movies);

        // Set the adapter on the Recycler View
        rvMovies.setAdapter(adapter);

        // Set a layout manager on the Recycler View
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        // Make an instance of AsyncHttpClient
        AsyncHttpClient client = new AsyncHttpClient();

        // Make a get request
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                // Get the JSON data
                JSONObject jsonObject = json.jsonObject;

                // Get the query results
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());

                    // Get all movies from the list of movies and store them
                    movies.addAll(Movie.fromJsonArray(results));

                    // When the data changes, let the adapter know
                    adapter.notifyDataSetChanged();

                    // Print out all movies
                    for (int i = 0; i < movies.size(); i++) {
                        Movie mov = movies.get(i);
                        Log.i(TAG, String.format("Path: %s, Title: %s, Overview: %s", mov.getPosterPath(), mov.getTitle(), mov.getOverview()));
                    }
                } catch (JSONException e) {
                    // If the key does not exist, log the error
                    Log.e(TAG, "JSON Key Exception", e);
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }
}