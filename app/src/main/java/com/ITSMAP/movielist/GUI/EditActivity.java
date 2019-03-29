package com.ITSMAP.movielist.GUI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ITSMAP.movielist.JSONResponse.Movie;
import com.ITSMAP.movielist.R;
import com.ITSMAP.movielist.Service.DataAccessService;


public class EditActivity extends AppCompatActivity {
    private Movie databaseMovie;
    private TextView movieTitle;
    private TextView userRating;
    private SeekBar userRatingSeekbar;
    private TextView userComment;
    private Button saveBtn;
    private Button cancelBtn;
    private Button clearBtn;
    private Button deleteBtn;
    private float seekbarValue;
    private CheckBox watchedCheckbox;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            databaseMovie = intent.getParcelableExtra("MOVIE");
            updateUI(databaseMovie);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initializeUI();
        if(savedInstanceState != null){
            databaseMovie = savedInstanceState.getParcelable("MOVIE");
            updateUI(databaseMovie);
        }
        else{
            getServiceInformation();
        }

        saveBtn.setOnClickListener(v -> {
            saveChangesToDatabase();
            finish();
        });
        clearBtn.setOnClickListener(v -> {
            removeUserInfo();
            finish();
        });

        deleteBtn.setOnClickListener(v -> {
            deleteMovie();
            finish();
        });
        cancelBtn.setOnClickListener((View v) -> finish());

        userRatingSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekbarValue = ((float) progress / 10);
                userRating.setText(getString(R.string.edit_activity_user_rating));
                userRating.append(" " + String.valueOf(seekbarValue));
                databaseMovie.setPersonalRating(String.valueOf(seekbarValue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void removeUserInfo() {
        databaseMovie.setPersonalRating(null);
        databaseMovie.setUserComment(null);
        databaseMovie.setWatched(false);
        Intent updateMovieIntent = new Intent(this, DataAccessService.class);
        updateMovieIntent.putExtra("COMMAND","UPDATE_MOVIE");
        updateMovieIntent.putExtra("ADDITIONAL_COMMAND","UPDATE");
        updateMovieIntent.putExtra("MOVIE_TO_UPDATE", databaseMovie);
        startService(updateMovieIntent);
        updateUI(databaseMovie);
    }

    private void deleteMovie(){
        Intent updateMovieIntent = new Intent(this, DataAccessService.class);
        updateMovieIntent.putExtra("COMMAND","DELETE_MOVIE");
        updateMovieIntent.putExtra("ADDITIONAL_COMMAND","DELETE");
        updateMovieIntent.putExtra("MOVIE_TO_DELETE", databaseMovie);
        startService(updateMovieIntent);
    }

    private void getServiceInformation() {
        Intent intent = getIntent();
        int movieId = intent.getIntExtra("MOVIE_DB_ID",5000);

        Intent getMovieIntent = new Intent(this, DataAccessService.class);
        getMovieIntent.putExtra("COMMAND", "GET_SPECIFIC_MOVIE");
        getMovieIntent.putExtra("ADDITIONAL_COMMAND", String.valueOf(movieId));
        startService(getMovieIntent);
    }

    private void saveChangesToDatabase(){
        Intent updateMovieIntent = new Intent(this, DataAccessService.class);
        databaseMovie.setWatched(watchedCheckbox.isChecked());
        databaseMovie.setUserComment(this.userComment.getText().toString());
        updateMovieIntent.putExtra("COMMAND","UPDATE_MOVIE");
        updateMovieIntent.putExtra("ADDITIONAL_COMMAND","UPDATE");
        updateMovieIntent.putExtra("MOVIE_TO_UPDATE", databaseMovie);
        startService(updateMovieIntent);
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
        deleteBtn = findViewById(R.id.editActivity_delete_btn);
    }

    private void updateUI(Movie movie) {
        this.movieTitle.setText(movie.getTitle());
        this.userRating.setText((movie.getPersonalRating() != null) ? String.format("%s%s", getString(R.string.edit_activity_user_rating), movie.getPersonalRating()) : getString(R.string.no_prev_user_rating));
        this.userRatingSeekbar.setProgress((movie.getPersonalRating() != null) ? Double.valueOf(Double.valueOf(movie.getPersonalRating())*10).intValue() : 50);  //Starts at 50%
        this.userComment.setText(movie.getUserComment());
        this.watchedCheckbox.setChecked(movie.isWatched());
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,new IntentFilter("MOVIE_FROM_DB_BY_ID"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("MOVIE",databaseMovie);
    }
}
