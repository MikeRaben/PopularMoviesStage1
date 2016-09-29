package com.bomberomedia.popularmoviesstage1;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MIKE";
    private RecyclerView.Adapter mAdapter;

    private String requestUrlBase = "https://api.themoviedb.org/3";
    private String apiKey;
    private String[] requestList = {"/movie/top_rated",
                                    "/movie/popular"};

    String topRatedJson = "";
    String popularJson = "";

    String requestUrl;
    ArrayList<MyMovie> movies;

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movies = new ArrayList<>();
        apiKey = getResources().getString(R.string.api_key);    //stored in key.xml as a string resource, file ignored by git

        spinner = (Spinner) findViewById(R.id.sort_selector);
        ArrayAdapter<CharSequence> spinAdapter = ArrayAdapter.createFromResource(this,
                R.array.movie_list_choices, android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Top: " + topRatedJson);
                Log.d(TAG, "Pop: " + popularJson);
                requestUrl = requestUrlBase + requestList[position] + apiKey;

                switch (position){
                    case 0:
                        //Top Rated selected, check if json has already been pulled
                        if (topRatedJson.length() > 1){
                            unPackMovies(topRatedJson);
                        } else {
                            new FetchMoviesTask().execute(requestUrl, "top");
                        }
                        break;
                    case 1:
                        if (popularJson.length() > 1){
                            unPackMovies(popularJson);
                        } else {
                            new FetchMoviesTask().execute(requestUrl, "pop");
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rv_thumbnail_grid);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(movies, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    String packUpMovies(){
        String moviesJson = "";

        if (movies.size() > 0) {
            try {
                JSONObject object = new JSONObject();
                JSONArray array = new JSONArray();
                for (MyMovie movie : movies) {
                    JSONObject jsonMovie = new JSONObject();
                    jsonMovie.put("title", movie.title);
                    jsonMovie.put("poster_url", movie.filePath);
                    jsonMovie.put("overview", movie.synopsis);
                    jsonMovie.put("release_date", movie.releaseDate);
                    jsonMovie.put("vote_average", movie.rating);

                    array.put(jsonMovie);
                }
                object.put("results", array);
                moviesJson = object.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return moviesJson;
    }

    void unPackMovies(String moviesJson){
        if (movies.size() > 0){
            movies.clear();
        }
        if (moviesJson.length() > 0){
            try {
                JSONObject object = new JSONObject(moviesJson);
                JSONArray results = object.getJSONArray("results");
                for (int i = 0; i < results.length(); i++){
                    MyMovie newMovie = new MyMovie();
                    JSONObject movie = results.getJSONObject(i);
                    newMovie.title = movie.getString("title");
                    newMovie.filePath = movie.getString("poster_path");
                    newMovie.releaseDate = movie.getString("release_date");
                    newMovie.synopsis = movie.getString("overview");
                    newMovie.rating = movie.getDouble("vote_average");

                    movies.add(newMovie);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("movieJson", packUpMovies());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        unPackMovies(savedInstanceState.getString("movieJson"));
    }

    private class FetchMoviesTask extends AsyncTask<String, String, String>{
        String topOrPop = "";
        @Override
        protected String doInBackground(String... params) {
            if (params.length > 1) {
                topOrPop = params[1];
            }

            HttpURLConnection connection;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(5000);
                connection.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

                String jsonString;

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();

                jsonString = sb.toString();
                return jsonString;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

            @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
                if (s != null){
                    if (Objects.equals(topOrPop, "top")) {
                        topRatedJson = s;
                    } else {
                        popularJson = s;
                    }
                    unPackMovies(s);
                }
        }
    }
}
