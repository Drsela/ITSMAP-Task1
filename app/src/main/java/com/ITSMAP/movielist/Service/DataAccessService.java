package com.ITSMAP.movielist.Service;


import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;

import com.ITSMAP.movielist.DAL.MovieDatabase;
import com.ITSMAP.movielist.DAL.RequestQueueSingelton;
import com.ITSMAP.movielist.JSONResponse.Movie;
import com.ITSMAP.movielist.JSONResponse.Search;
import com.ITSMAP.movielist.JSONResponse.SearchResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataAccessService extends Service {
    private final String API_KEY = "b453845";
    private final String URL_BY_IMDBID = "https://www.omdbapi.com/?apikey="+ API_KEY + "&i=";
    private final String URL_BY_SEARCH = "https://www.omdbapi.com/?apikey="+ API_KEY + "&s=";

    public final static String ACTION_FETCH_DB_MOVIES = "FETCHING_FROM_DB";
    public static final String RESULT_FETCH_DB_MOVIES = "FETCHING_FROM_DB_RESULT";

    public final static String ACTION_FETCH_SEARCH_TITLES = "FETCHING_FROM_SEARCH";
    public final static String RESULT_FETCH_SEARCH_TITLES = "RESULTS_FROM_SEARCH";

    public static final String ACTION_FETCH_DB_SPECIFIC_MOVIE = "ACTION_FETCH_DB_SPECIFIC_MOVIE";
    public static final String RESULT_FETCH_DB_SPECIFIC_MOVIE = "ACTION_FETCH_DB_SPECIFIC_MOVIE";


    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public DataAccessService getService() {
            // Return object that can call public methods
            return DataAccessService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
            return mBinder;
        }

        //Service Methods

    public void getMoviesFromDB() {
        AsyncTask.execute(() -> {
            MovieDatabase db = MovieDatabase.getMovieDatabase(getApplicationContext());
            List<Movie> db_movies = db.movieDao().getMovies();
            Intent intent = new Intent(ACTION_FETCH_DB_MOVIES);

            intent.putParcelableArrayListExtra(RESULT_FETCH_DB_MOVIES, (ArrayList<? extends Parcelable>) db_movies);
            sendBroadcast(intent);
        });
    }

    public void getSpecificMovieFromDB(int id) {
        AsyncTask.execute(() -> {
            MovieDatabase db = MovieDatabase.getMovieDatabase(getApplicationContext());
            Movie dbMovie = db.movieDao().getMovie(id);
            Intent intent = new Intent(ACTION_FETCH_DB_SPECIFIC_MOVIE);
            intent.putExtra(RESULT_FETCH_DB_SPECIFIC_MOVIE,dbMovie);
            sendBroadcast(intent);
        });
    }

    public void performSearchAPI(String searchString) {
        Gson gson = new Gson();
        final List<Search> Search = new ArrayList();
        String URL = URL_BY_SEARCH + searchString;
        RequestQueue queue = RequestQueueSingelton.getInstance(getApplicationContext()).getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null,
                        response -> {
                    Type type = new TypeToken<SearchResponse>() {}.getType();
                    SearchResponse search = gson.fromJson(response.toString(), type);
                    List<com.ITSMAP.movielist.JSONResponse.Search> SearchResult = search.getSearch();

                    Intent intent = new Intent(ACTION_FETCH_SEARCH_TITLES);
                    intent.putParcelableArrayListExtra(RESULT_FETCH_SEARCH_TITLES, (ArrayList<? extends Parcelable>) SearchResult);
                    sendBroadcast(intent);
                    }, error -> {
                    // TODO: Handle error
                    Log.e("ERROR", "onResponse: " + error.toString());
                });

        queue.add(jsonObjectRequest);
    }

    public void addMovieFromSearch(String imdbId) {
        Gson gson = new Gson();
        String URL = URL_BY_IMDBID + imdbId;

        RequestQueue queue = RequestQueueSingelton.getInstance(getApplicationContext()).getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null,
                        response -> {
                    Log.d("SUCCESS", "onResponse: " + response.toString());
                    Type type = new TypeToken<Movie>() {}.getType();

                    Movie movie = gson.fromJson(response.toString(), type);
                    MovieDatabase db = MovieDatabase.getMovieDatabase(getApplicationContext());
                    AsyncTask.execute(() -> {
                        db.movieDao().insertMovie(movie);
                    });
                    }, error -> {
                    // TODO: Handle error
                    Log.d("ERROR", "onResponse: " + error.toString());
                });

        queue.add(jsonObjectRequest);
    }

    public void deleteMovieFromDB(Movie movie) {
        AsyncTask.execute(() -> {
            MovieDatabase db = MovieDatabase.getMovieDatabase(getApplicationContext());
            db.movieDao().deleteMovie(movie);
        });
    }

    public void updateMovieInDB(Movie movie) {
        AsyncTask.execute(() -> {
            MovieDatabase db = MovieDatabase.getMovieDatabase(getApplicationContext());
            db.movieDao().updateMovie(movie);
        });
    }

}
