package com.ITSMAP.movielist.DAL;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ITSMAP.movielist.JSONResponse.Movie;

import java.util.List;

@Dao
public interface MovieQueries {

    @Insert
    void insertMovie(Movie movie);

    @Query("SELECT * FROM db_movies")
    List<Movie> getMovies();

    @Query("SELECT * FROM db_movies WHERE id = :movieId")
    Movie getMovie(int movieId);

    @Update
    void updateMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);
}
