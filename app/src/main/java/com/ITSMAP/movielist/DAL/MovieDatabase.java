package com.ITSMAP.movielist.DAL;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.ITSMAP.movielist.JSONResponse.Movie;

@Database(entities = {Movie.class}, version = 1)
public abstract class MovieDatabase extends RoomDatabase {
    private static MovieDatabase movieDatabase;

    public abstract MovieQueries movieDao();

    public static MovieDatabase getMovieDatabase(Context context) {
        if ( movieDatabase == null)
        {
            movieDatabase = Room.databaseBuilder(context.getApplicationContext(),MovieDatabase.class,"db_movies").build();
        }
        return movieDatabase;
    }

    public static void destroyInstance() {
        movieDatabase = null;
    }
}
