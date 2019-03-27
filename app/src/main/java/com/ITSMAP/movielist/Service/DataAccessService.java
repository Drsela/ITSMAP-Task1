package com.ITSMAP.movielist.Service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Parcelable;

import com.ITSMAP.movielist.DAL.APICommunication;
import com.ITSMAP.movielist.DAL.MovieDatabase;
import com.ITSMAP.movielist.GUI.MainActivity;
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
            List<Movie> dbMovies = db.movieDao().getMovies().getValue();
            Intent returnToMainActivity = new Intent(getBaseContext(), MainActivity.class);
            returnToMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            returnToMainActivity.putParcelableArrayListExtra("DB_MOVIES", (ArrayList<? extends Parcelable>) dbMovies);
            getApplication().startActivity(returnToMainActivity);
        }

        if(command.equals("GET_SPECIFIC_MOVIE")) {
            final int movieId = intent.getIntExtra("MOVIE_ID",0);
            Movie dbMovie = db.movieDao().getMovie(movieId);
            if(additionalCommand.equals("DATA_TO_DETAIL_ACTIVITY")){
                Intent returnToDetailActivity = new Intent();
                returnToDetailActivity.putExtra("DB_MOVIE",dbMovie);
                startActivity(returnToDetailActivity);
            }

            if(additionalCommand.equals("DATA_TO_EDIT_ACTIVITY")){
                Intent returnToEditActivity = new Intent();
                returnToEditActivity.putExtra("DB_MOVIE",dbMovie);
                startActivity(returnToEditActivity);
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
    }
}
