package com.ITSMAP.movielist.GUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ITSMAP.movielist.Adapter.MovieAdapter;
import com.ITSMAP.movielist.DTO.Movie;
import com.ITSMAP.movielist.R;

import java.util.Objects;

public class EditActivity extends AppCompatActivity {
    private TextView movieTitle;
    private TextView userRating;
    private SeekBar userRatingSeekbar;
    private TextView userComment;
    private Button saveBtn;
    private Button cancelBtn;
    private Button clearBtn;
    private float seekbarValue;
    private CheckBox watchedCheckbox;
    private Movie clickedMovie;
    private String SEEKBAR_VALUE_KEY = "seekbarValue";
    private String WATCH_STATUS_KEY = "watchStatus";
    private String COMMENT_KEY = "comment";
    private String MOVIE_TITLE_KEY = "movieTitle";
    private String USER_RATING_KEY = "userRating";
    private int ADAPTER_POSITION_FROM_RECYCLE_VIEW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initializeUI();
        Intent intent = getIntent();
        clickedMovie = Objects.requireNonNull(intent.getExtras()).getParcelable(MovieAdapter.MOVIE_FROM_ADAPTER);
        ADAPTER_POSITION_FROM_RECYCLE_VIEW = intent.getIntExtra(MovieAdapter.ADAPTER_POSITION, 0);
        if (savedInstanceState != null) {
            int oldSeekbarValue = savedInstanceState.getInt(SEEKBAR_VALUE_KEY);
            boolean checkbox = savedInstanceState.getBoolean(WATCH_STATUS_KEY);
            boolean hasUserRating = savedInstanceState.getBoolean(USER_RATING_KEY);
            String oldComment = savedInstanceState.getString(COMMENT_KEY);
            String title = savedInstanceState.getString(MOVIE_TITLE_KEY);
            clickedMovie.setUserRating(hasUserRating);
            userRatingSeekbar.setProgress(oldSeekbarValue);
            watchedCheckbox.setChecked(checkbox);
            userComment.setText(oldComment);
            movieTitle.setText(title);
            if (!hasUserRating) {
                userRating.setText(R.string.no_prev_user_rating);
            } else {
                userRating.setText(getString(R.string.edit_activity_user_rating));
                seekbarValue = ((float) oldSeekbarValue / 10);
                userRating.append(" " + String.valueOf(seekbarValue));
            }
        } else {
            updateUI(Objects.requireNonNull(clickedMovie));
        }

        saveBtn.setOnClickListener(v -> {
            Movie updatedMovie = updateMovieSettings(clickedMovie);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("TEST", updatedMovie);
            returnIntent.putExtra(MovieAdapter.ADAPTER_POSITION, ADAPTER_POSITION_FROM_RECYCLE_VIEW);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });
        clearBtn.setOnClickListener(v -> {
            clickedMovie.setUserRating(null);
            clickedMovie.setUserComment(null);
            clickedMovie.setWatchStatus(false);
            clickedMovie.setUserComment(false);
            clickedMovie.setUserRating(false);

            Intent returnIntent = new Intent();
            returnIntent.putExtra("TEST", clickedMovie);
            returnIntent.putExtra(MovieAdapter.ADAPTER_POSITION, ADAPTER_POSITION_FROM_RECYCLE_VIEW);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });
        cancelBtn.setOnClickListener((View v) -> finish());

        userRatingSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekbarValue = ((float) progress / 10);
                userRating.setText(getString(R.string.edit_activity_user_rating));
                userRating.append(" " + String.valueOf(seekbarValue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                clickedMovie.setUserRating(true);
            }
        });
    }

    private void initializeUI() {
        movieTitle = findViewById(R.id.editActivity_movieTitle_textView);
        userRating = findViewById(R.id.editActivity_userRating_textView);
        userRatingSeekbar = findViewById(R.id.editActivity_userRating_seekBar);
        userComment = findViewById(R.id.editActivity_userComment_textView);
        watchedCheckbox = findViewById(R.id.editActivity_watched_checkbox);
        saveBtn = findViewById(R.id.editActivity_save_btn);
        cancelBtn = findViewById(R.id.editActivity_cancel_btn);
        clearBtn = findViewById(R.id.edit_activity_clear_btn);
    }

    private void updateUI(Movie movie) {
        movieTitle.setText(movie.getName());

        if (movie.hasUserRating()) {
            userRating.setText(getString(R.string.edit_activity_user_rating));
            userRating.append(movie.getUserRating());
            float userRating = Float.valueOf(movie.getUserRating());
            userRatingSeekbar.setProgress(Math.round(userRating * 10));
            seekbarValue = ((float) userRatingSeekbar.getProgress() / 10);
        } else {
            userRating.setText(getString(R.string.no_prev_user_rating));
        }

        if (movie.hasUserComment()) {
            userComment.setText(movie.getUserComment());
        }
        if (movie.hasBeenWatched()) {
            watchedCheckbox.setChecked(true);
        }
    }

    private Movie updateMovieSettings(Movie movie) {
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(SEEKBAR_VALUE_KEY, userRatingSeekbar.getProgress());
        savedInstanceState.putBoolean(WATCH_STATUS_KEY, watchedCheckbox.isChecked());
        savedInstanceState.putBoolean(USER_RATING_KEY, clickedMovie.hasUserRating());
        savedInstanceState.putString(COMMENT_KEY, userComment.getText().toString());
        savedInstanceState.putString(MOVIE_TITLE_KEY, movieTitle.getText().toString());
    }
}
