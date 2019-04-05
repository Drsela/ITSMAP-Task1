package com.ITSMAP.movielist.DAL;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;

import com.ITSMAP.movielist.JSONResponse.Movie;
import com.ITSMAP.movielist.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Database(entities = {Movie.class}, version = 2)
public abstract class MovieDatabase extends RoomDatabase {
    private static MovieDatabase movieDatabase;

    public abstract MovieQueries movieDao();

    public static MovieDatabase getMovieDatabase(Context context) {
        if ( movieDatabase == null)
        {
            movieDatabase = Room.databaseBuilder(context.getApplicationContext(), MovieDatabase.class, "db_movies")                    //Allowed for using onCreate and inserting movie. Otherwise user would have to manually refresh layout on first load.
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            InputStream in = context.getResources().openRawResource(R.raw.startupmovies);
                            BufferedReader rd = new BufferedReader(new InputStreamReader(in));
                            StringBuilder builder = new StringBuilder();
                            String json;
                            try {
                                while ((json = rd.readLine()) != null) {
                                    builder.append(json);
                                }
                                List<Movie> movies = Movie.populateFromJson(builder.toString());

                                for (Movie film : movies) {
                                    ContentValues cv = new ContentValues();
                                    cv.put("title", film.getTitle());
                                    cv.put("year", film.getYear());
                                    cv.put("rated", film.getRated());
                                    cv.put("released", film.getReleased());
                                    cv.put("runtime", film.getRuntime());
                                    cv.put("genre", film.getGenre());
                                    cv.put("director", film.getDirector());
                                    cv.put("writer", film.getWriter());
                                    cv.put("actors", film.getActors());
                                    cv.put("plot", film.getPlot());
                                    cv.put("language", film.getLanguage());
                                    cv.put("country", film.getCountry());
                                    cv.put("awards", film.getAwards());
                                    cv.put("poster", film.getPoster());
                                    cv.put("metascore", film.getMetascore());
                                    cv.put("imdbRating", film.getImdbRating());
                                    cv.put("imdbVotes", film.getImdbVotes());
                                    cv.put("imdbID", film.getImdbID());
                                    cv.put("type", film.getType());
                                    cv.put("dVD", film.getDVD());
                                    cv.put("boxOffice", film.getBoxOffice());
                                    cv.put("production", film.getProduction());
                                    cv.put("website", film.getWebsite());
                                    cv.put("response", film.getResponse());
                                    cv.put("userComment", film.getUserComment());
                                    cv.put("personalRating", film.getPersonalRating());
                                    cv.put("watched", film.isWatched());
                                    db.insert("db_movies", 0, cv);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onOpen(@NonNull SupportSQLiteDatabase db) {
                            super.onOpen(db);
                        }
                    })
                    .build();
        }
        return movieDatabase;
    }

    public static void destroyInstance() {
        movieDatabase = null;
    }
}
