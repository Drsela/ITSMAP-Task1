package com.ITSMAP.movielist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ITSMAP.movielist.DTO.Movie;

import java.util.Objects;

public class Details extends AppCompatActivity {
    private ImageView moviePoster;
    private TextView moviePlot;
    private TextView movieTitle;
    private TextView movieiMDB;
    private TextView movieUserRating;
    private TextView movieUserComment;
    private TextView movieGenres;
    private Button OK_Btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        Movie clickedMovie = Objects.requireNonNull(intent.getExtras()).getParcelable("MOVIE");
        initializeUI();
        updateUI(clickedMovie);

        OK_Btn.setOnClickListener(v -> {
            finish();
        });
    }

    private void initializeUI(){
        moviePoster = findViewById(R.id.details_image);
        moviePlot = findViewById(R.id.details_plot_textView);
        movieTitle = findViewById(R.id.details_movieTitle_textView);
        movieiMDB = findViewById(R.id.details_iMDB_textView);
        movieUserRating = findViewById(R.id.details_userRating_textView);
        movieUserComment = findViewById(R.id.details_UserComment_textView);
        movieGenres = findViewById(R.id.details_Genres_textView);
        OK_Btn = findViewById(R.id.details_ok_btn);
    }


    private void updateUI(Movie movie) {
        movieTitle.setText(movie.getName());
        moviePlot.setText(movie.getPlot());
        movieiMDB.setText(movie.getiMDBRating());
        movieUserComment.setText(movie.getUserComment());
        movieUserRating.setText(movie.getUserRating());
        movieGenres.setText(movie.getGenres());
    }
}
