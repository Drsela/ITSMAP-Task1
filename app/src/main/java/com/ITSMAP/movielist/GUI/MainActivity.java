package com.ITSMAP.movielist.GUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.ITSMAP.movielist.Adapter.MovieAdapter;
import com.ITSMAP.movielist.CSVReader;
import com.ITSMAP.movielist.DTO.Movie;
import com.ITSMAP.movielist.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MovieAdapter movieAdapter;
    private List<Movie> moviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        Button exitBtn = findViewById(R.id.main_btn_exit);

        moviesList = getMovieListFromPrevSession(getString(R.string.MOVIE_LIST_FROM_PREV_SESSION));
        if (moviesList != null) {
            movieAdapter = new MovieAdapter(moviesList, this);
        } else {
            List data = getDataFromCsvFile();
            moviesList = getMovieObjects(data);
            movieAdapter = new MovieAdapter(moviesList, this);
        }

        recyclerView.setAdapter(movieAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        exitBtn.setOnClickListener(v -> finish());
    }

    private List getDataFromCsvFile() {
        InputStream stream = getResources().openRawResource(R.raw.movielist);
        CSVReader reader = new CSVReader(stream);
        return reader.getMovies();
    }

    private ArrayList<Movie> getMovieObjects(List<Movie> data) {
        ArrayList<Movie> movieList = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            Movie currentMovie = data.get(i);
            if (!currentMovie.getPlot().equals("Plot")) {
                movieList.add(currentMovie);
            }
        }

        for (Movie movie : movieList) {
            movie.setUserRating(false);
            movie.setUserComment(false);
        }
        return movieList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        movieAdapter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(moviesList);
        editor.putString(getString(R.string.MOVIE_LIST_FROM_PREV_SESSION), json);
        editor.apply();
        super.onPause();
    }

    private List<Movie> getMovieListFromPrevSession(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<List<Movie>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
}
