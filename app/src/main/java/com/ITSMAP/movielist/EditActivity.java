package com.ITSMAP.movielist;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ITSMAP.movielist.DTO.Movie;

import java.util.Objects;

public class EditActivity extends AppCompatActivity {
    private TextView moviePlot;
    private TextView userRating;
    private SeekBar userRatingSeekbar;
    private TextView userComment;
    private Button saveBtn;
    private float seekbarValue;
    private CheckBox watchedCheckbox;
    public static String EDITACTIVITY_MOVIE_RESPONSE = "UPDATED_MOVIE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        Movie clickedMovie = Objects.requireNonNull(intent.getExtras()).getParcelable("MOVIE");
        initializeUI();
        updateUI(clickedMovie);

        saveBtn.setOnClickListener(v -> {
            Movie updatedMovie = updateMovieSettings(clickedMovie);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("TEST",updatedMovie);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        });

        userRatingSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekbarValue = ((float)progress / 10);
                userRating.setText(getString(R.string.edit_activity_user_rating));
                userRating.append(" " + String.valueOf(seekbarValue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initializeUI(){
        moviePlot = findViewById(R.id.editActivity_movieTitle_textView);
        userRating = findViewById(R.id.editActivity_userRating_textView);
        userRatingSeekbar = findViewById(R.id.editActivity_userRating_seekBar);
        userComment = findViewById(R.id.editActivity_userComment_textView);
        watchedCheckbox = findViewById(R.id.editActivity_watched_checkbox);
        saveBtn = findViewById(R.id.editActivity_save_btn);
    }
    private void updateUI(Movie movie) {
        moviePlot.setText(movie.getName());
        userRating.setText(getString(R.string.edit_activity_user_rating));
        if (movie.hasUserRating())
        {
            userRating.append(movie.getUserRating());
            float userRating = Float.valueOf(movie.getUserRating());
            userRatingSeekbar.setProgress(Math.round(userRating*10));
            seekbarValue = ((float)userRatingSeekbar.getProgress() / 10);
        }
        else
            userRating.append("N/A");
        if (movie.hasUserComment())
        {
            userComment.setText(movie.getUserComment());
        }
        if(movie.isWatchStatus()){
            watchedCheckbox.setChecked(true);
        }
    }
    private Movie updateMovieSettings(Movie movie){
        movie.setUserRating(String.valueOf(seekbarValue));
        movie.setUserRating(true);
        movie.setUserComment(userComment.getText().toString());
        movie.setUserComment(true);
        if (watchedCheckbox.isChecked())
            movie.setWatchStatus(true);
        else
            movie.setWatchStatus(false);
        return movie;
    }
}
