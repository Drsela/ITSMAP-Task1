package com.ITSMAP.movielist;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.ITSMAP.movielist.DTO.Movie;

import java.util.Objects;

public class Details extends AppCompatActivity {
    private ImageView moviePoster;
    private TextView moviePlot;
    private TextView movieTitle;
    private TextView movieiMDB;
    private TextView watchStatus;
    private TextView movieUserRating;
    private TextView movieUserComment;
    private TextView movieGenres;
    private Button OK_Btn;
    private drawableGenerator drawableGenerator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        drawableGenerator = new drawableGenerator(this);
        Intent intent = getIntent();
        Movie clickedMovie = Objects.requireNonNull(intent.getExtras()).getParcelable("MOVIE");
        initializeUI();
        updateUI(clickedMovie);

        OK_Btn.setOnClickListener(v -> finish());
    }

    private void initializeUI(){
        moviePoster = findViewById(R.id.details_image);
        moviePlot = findViewById(R.id.details_plot_textView);
        movieTitle = findViewById(R.id.details_movieTitle_textView);
        movieiMDB = findViewById(R.id.details_iMDB_textView);
        watchStatus = findViewById(R.id.details_watchStatus_textView);
        movieUserRating = findViewById(R.id.details_userRating_textView);
        movieUserComment = findViewById(R.id.details_UserComment_textView);
        movieGenres = findViewById(R.id.details_Genres_textView);
        OK_Btn = findViewById(R.id.details_ok_btn);
        movieUserRating.setPaintFlags(movieUserRating.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        movieiMDB.setPaintFlags(movieiMDB.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }


    private void updateUI(Movie movie) {
        this.setTitle(movie.getName());
        //movieTitle.setText(movie.getName());
        movieTitle.setText(null);
        moviePlot.setText(movie.getPlot());
        watchStatus.setText(movie.isWatchStatus() ? getString(R.string.edit_movie_watched_status) : getString(R.string.movie_not_seen));
        movieiMDB.setText(getString(R.string.details_iMDB));
        movieiMDB.append(movie.getiMDBRating());
        movieUserComment.setText(movie.getUserComment());
        if (movie.hasUserRating()){
            movieUserRating.setText(R.string.edit_activity_user_rating);
            movieUserRating.append(movie.getUserRating());
        }
        else {
            movieUserRating.setText(getString(R.string.User_Rating_Not_Set));
        }
        movieGenres.setText(movie.getGenres());
        moviePoster.setImageDrawable(drawableGenerator.getDrawableByGenre(movie));
    }
}
