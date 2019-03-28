package com.ITSMAP.movielist.Service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;

import com.ITSMAP.movielist.DAL.APICommunication;
import com.ITSMAP.movielist.DAL.MovieDatabase;
import com.ITSMAP.movielist.JSONResponse.Movie;

import java.util.ArrayList;
import java.util.List;

public class DataAccessService extends IntentService {

    public DataAccessService(){
        super("DataAccessService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        final String command = intent.getStringExtra("COMMAND");
        final String additionalCommand = intent.getStringExtra("ADDITIONAL_COMMAND");

        MovieDatabase db = MovieDatabase.getMovieDatabase(getApplicationContext());

        if(command.equals("GET_DB_MOVIES")){
            List<Movie> dbMovies = db.movieDao().getMovies();
            Intent returnToMainActivity = new Intent("DB_MOVIES_RESULT");
            returnToMainActivity.putParcelableArrayListExtra("DB_MOVIES", (ArrayList<? extends Parcelable>) dbMovies);
            LocalBroadcastManager.getInstance(this).sendBroadcast(returnToMainActivity);
        }

        if(command.equals("GET_SPECIFIC_MOVIE")) {
            if(additionalCommand != null){
                Movie dbMovie = db.movieDao().getMovie(Integer.valueOf(additionalCommand));
                Intent returnToDetailActivity = new Intent("MOVIE_FROM_DB_BY_ID");
                returnToDetailActivity.putExtra("MOVIE",dbMovie);
                LocalBroadcastManager.getInstance(this).sendBroadcast(returnToDetailActivity);
            }

        }

        if(command.equals("ADD_MOVIE_FROM_API_ID")) {
            if (additionalCommand != null){
                APICommunication API = new APICommunication();
                API.addMovieToDB(additionalCommand,this);
            }
        }

        if(command.equals("SEARCH_MOVIES")) {
            if (additionalCommand != null) {
                APICommunication API = new APICommunication();
                API.performSearch(additionalCommand,this);
            }
        }

        if(command.equals("GET_POSTER")) {
            if(additionalCommand != null){
                String posterUrl = additionalCommand;
                APICommunication API = new APICommunication();
                API.getPoster(posterUrl,this);
            }
        }

        if(command.equals("UPDATE_MOVIE")){
            if(additionalCommand.equals("UPDATE")){
                Movie movieToUpdate = intent.getParcelableExtra("MOVIE_TO_UPDATE");
                db.movieDao().updateMovie(movieToUpdate);
            }
        }

        if(command.equals("DELETE_MOVIE")) {
            if (additionalCommand.equals("DELETE")) {
                Movie movieToDelete = intent.getParcelableExtra("MOVIE_TO_DELETE");
                db.movieDao().deleteMovie(movieToDelete);
            }
        }
    }
}
