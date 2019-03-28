package com.ITSMAP.movielist.GUI;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ITSMAP.movielist.JSONResponse.Movie;
import com.ITSMAP.movielist.R;
import com.ITSMAP.movielist.Service.DataAccessService;
import com.ITSMAP.movielist.drawableGenerator;

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
    private com.ITSMAP.movielist.drawableGenerator drawableGenerator;
    private Movie databaseMovie;
    private Context activityContext;
    private ProgressDialog progressDialog;

    private BroadcastReceiver movieBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            databaseMovie = intent.getParcelableExtra("MOVIE");
            getPoster();
        }
    };
    private void getPoster(){
        Intent getPoster = new Intent(this,DataAccessService.class);
        getPoster.putExtra("COMMAND","GET_POSTER");
        getPoster.putExtra("ADDITIONAL_COMMAND", databaseMovie.getPoster());
        startService(getPoster);
    }
    private BroadcastReceiver posterBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bitmap poster = intent.getParcelableExtra("POSTER");
            setPoster(poster);
            updateUI(databaseMovie);
        }
    };

    private void setPoster(Bitmap poster) {
        if(poster != null){
            this.poster.setImageBitmap(poster);
        }
        progressDialog.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        drawableGenerator = new drawableGenerator(this);
        activityContext = this;
        progressDialog = new ProgressDialog(this);
        initializeUI();

        Intent intent = getIntent();
        int movieId = intent.getIntExtra("MOVIE_DB_ID",5000);

        Intent getMovieIntent = new Intent(this, DataAccessService.class);
        getMovieIntent.putExtra("COMMAND", "GET_SPECIFIC_MOVIE");
        getMovieIntent.putExtra("ADDITIONAL_COMMAND", String.valueOf(movieId));
        startService(getMovieIntent);
        progressDialog.setMessage("Fetching information");
        progressDialog.show();
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
        this.userRatingText.setText(String.format("%s%s", getString(R.string.edit_activity_user_rating), movie.getPersonalRating()));
        this.userCommentText.setText(movie.getUserComment());
        this.movieGenres.setText(movie.getGenre());
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(movieBroadcastReceiver,new IntentFilter("MOVIE_FROM_DB_BY_ID"));
        LocalBroadcastManager.getInstance(this).registerReceiver(posterBroadcastReceiver,new IntentFilter("POSTER_RESPONSE"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(movieBroadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(posterBroadcastReceiver);
    }
}
