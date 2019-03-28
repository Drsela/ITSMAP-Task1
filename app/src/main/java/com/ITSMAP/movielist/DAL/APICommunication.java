package com.ITSMAP.movielist.DAL;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.ITSMAP.movielist.JSONResponse.Movie;
import com.ITSMAP.movielist.JSONResponse.Search;
import com.ITSMAP.movielist.JSONResponse.SearchResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class APICommunication {

    private final String API_KEY = "b453845";
    private final String URL_BY_IMDBID = "https://www.omdbapi.com/?apikey="+ API_KEY + "&i=";
    private final String URL_BY_SEARCH = "https://www.omdbapi.com/?apikey="+ API_KEY + "&s=";
    private Movie movieFromAPI;
    private List<Movie> movieSearchList;

    public void addMovieToDB(String imdbId, Context context) {
        Gson gson = new Gson();
        String URL = URL_BY_IMDBID + imdbId;

        RequestQueue queue = RequestQueueSingelton.getInstance(context).getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("SUCCESS", "onResponse: " + response.toString());
                        Type type = new TypeToken<Movie>() {}.getType();
                        Movie movie = gson.fromJson(response.toString(), type);
                        MovieDatabase db = MovieDatabase.getMovieDatabase(context);
                        AsyncTask.execute(() -> {
                            db.movieDao().insertMovie(movie);
                            int count = db.movieDao().getNumberOfItemsInDB();
                        });
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.d("ERROR", "onResponse: " + error.toString());
                    }
                });
        queue.add(jsonObjectRequest);
    }

    public void performSearch(String searchString, Context serviceContext) {
        Gson gson = new Gson();
        final List<Search> Search = new ArrayList();
        String URL = URL_BY_SEARCH + searchString;
        RequestQueue queue = RequestQueueSingelton.getInstance(serviceContext).getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("SUCCESS", "onResponse: " + response.toString());
                        Type type = new TypeToken<SearchResponse>() {}.getType();
                        SearchResponse search = gson.fromJson(response.toString(), type);
                        List<Search> SearchResult = search.getSearch();

                        Intent intent = new Intent("SEARCH_RESULT");
                        intent.putParcelableArrayListExtra("SEARCHED_MOVIES", (ArrayList<? extends Parcelable>) SearchResult);
                        LocalBroadcastManager.getInstance(serviceContext).sendBroadcast(intent);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.e("ERROR", "onResponse: " + error.toString());
                    }
                });

        queue.add(jsonObjectRequest);
    }

    public void getPoster(String Poster_URL, Context context){
        RequestQueue queue = RequestQueueSingelton.getInstance(context).getRequestQueue();

        ImageRequest request = new ImageRequest(Poster_URL,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        Intent intent = new Intent("POSTER_RESPONSE");
                        intent.putExtra("POSTER", bitmap);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error fetching poster", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(request);
    }

    public void updateMovie(Movie movieToUpdate) {
    }
}
