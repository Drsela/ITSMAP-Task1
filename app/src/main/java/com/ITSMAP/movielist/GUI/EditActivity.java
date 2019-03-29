package com.ITSMAP.movielist.GUI;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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

    private DataAccessService mService;
    MyReceiver mReciver;
    boolean mBound = false;
    boolean rotated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initializeUI();
        if(savedInstanceState != null){
            databaseMovie = savedInstanceState.getParcelable("MOVIE");
            rotated = savedInstanceState.getBoolean("ROTATED");
            updateUI(databaseMovie);
        }
        else{
            registorReciever();
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
        mService.updateMovieInDB(databaseMovie);
        finish();
    }

    private void deleteMovie(){
        mService.deleteMovieFromDB(databaseMovie);
    }

    private void registorReciever() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DataAccessService.ACTION_FETCH_DB_SPECIFIC_MOVIE);
        mReciver = new MyReceiver();
        registerReceiver(mReciver,intentFilter);
    }

    private void saveChangesToDatabase(){
        databaseMovie.setUserComment(userComment.getText().toString());
        databaseMovie.setWatched(watchedCheckbox.isChecked());
        mService.updateMovieInDB(databaseMovie);
        finish();
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
    protected void onStart() {
        super.onStart();
            Intent serviceIntent = new Intent(this, DataAccessService.class);
            bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mReciver);
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registorReciever();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DataAccessService.LocalBinder binder = (DataAccessService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            if(!rotated) {
                Intent intent = getIntent();
                int movieId = intent.getIntExtra("MOVIE_DB_ID",5000);
                mService.getSpecificMovieFromDB(movieId);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            databaseMovie = arg1.getParcelableExtra(DataAccessService.RESULT_FETCH_DB_SPECIFIC_MOVIE);
            updateUI(databaseMovie);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("MOVIE",databaseMovie);
        outState.putBoolean("ROTATED",true);
    }
}
