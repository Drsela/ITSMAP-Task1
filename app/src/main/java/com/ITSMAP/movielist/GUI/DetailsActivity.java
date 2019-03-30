package com.ITSMAP.movielist.GUI;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ITSMAP.movielist.JSONResponse.Movie;
import com.ITSMAP.movielist.R;
import com.ITSMAP.movielist.Service.DataAccessService;
import com.bumptech.glide.Glide;

public class DetailsActivity extends AppCompatActivity {
    private ImageView poster;
    private TextView plot;
    private TextView title;
    private TextView iMDBRating;
    private TextView watchText;
    private TextView userRatingText;
    private TextView userCommentText;
    private TextView movieGenres;
    private Button OK_Btn;
    private ProgressDialog progressDialog;

    private DataAccessService mService;
    MyReceiver mReciver;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching data from DB");
        initializeUI();


        OK_Btn.setOnClickListener(v -> finish());
    }

    private void initializeUI() {
        poster = findViewById(R.id.details_image);
        plot = findViewById(R.id.details_plot_textView);
        title = findViewById(R.id.details_movieTitle_textView);
        iMDBRating = findViewById(R.id.details_iMDB_textView);
        watchText = findViewById(R.id.details_watchStatus_textView);
        userRatingText = findViewById(R.id.details_userRating_textView);
        userCommentText = findViewById(R.id.details_UserComment_textView);
        movieGenres = findViewById(R.id.details_Genres_textView);
        OK_Btn = findViewById(R.id.details_ok_btn);
        userRatingText.setPaintFlags(userRatingText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        iMDBRating.setPaintFlags(iMDBRating.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private void updateUI(com.ITSMAP.movielist.JSONResponse.Movie movie) {
        this.plot.setText(movie.getPlot());
        this.title.setText(movie.getTitle());
        this.iMDBRating.setText(String.format("%s%s", getString(R.string.details_iMDB), movie.getImdbRating()));
        this.watchText.setText(movie.isWatched() ? getString(R.string.movie_status_watched) : getString(R.string.movie_status_unwatched));
        this.userRatingText.setText(movie.getPersonalRating() != null ? String.format("%s%s", getString(R.string.edit_activity_user_rating), movie.getPersonalRating()) : "");
        this.userCommentText.setText(movie.getUserComment());
        this.movieGenres.setText(String.format("%s%s", getString(R.string.details_genres_text), movie.getGenre()));
        progressDialog.dismiss();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    protected void onStart() {
        super.onStart();
        registorReciever();
        progressDialog.show();
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
    }

    private void registorReciever() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DataAccessService.ACTION_FETCH_DB_SPECIFIC_MOVIE);
        mReciver = new MyReceiver();
        registerReceiver(mReciver,intentFilter);
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
            Intent intent = getIntent();
            int movieId = intent.getIntExtra("MOVIE_DB_ID",5000);
            mService.getSpecificMovieFromDB(movieId);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            Movie movie = arg1.getParcelableExtra(DataAccessService.RESULT_FETCH_DB_SPECIFIC_MOVIE);
            Glide.with(getApplicationContext()).load(movie.getPoster()).into(poster);
            updateUI(movie);
        }
    }
}
