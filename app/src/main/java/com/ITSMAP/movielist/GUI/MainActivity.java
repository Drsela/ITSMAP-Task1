package com.ITSMAP.movielist.GUI;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.Toast;

import com.ITSMAP.movielist.Adapter.MovieAdapter;
import com.ITSMAP.movielist.JSONResponse.Movie;
import com.ITSMAP.movielist.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MovieAdapter movieAdapter;
    private List<com.ITSMAP.movielist.JSONResponse.Movie> moviesList;
    private FloatingActionButton fab;
    RecyclerView recyclerView;
    Intent dataAccessService;
    private ProgressDialog Dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        Button exitBtn = findViewById(R.id.main_btn_exit);
        moviesList = new ArrayList<>();
        Dialog = new ProgressDialog(this);
        dataAccessService = new Intent(this, com.ITSMAP.movielist.Service.DataAccessService.class);
        dataAccessService.putExtra("COMMAND","GET_DB_MOVIES");
        startService(dataAccessService);
        Dialog.setMessage("Fetching from database");
        Dialog.show();
        movieAdapter = new MovieAdapter(moviesList, this);
        recyclerView.setAdapter(movieAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(v -> {
            Intent searchActivity = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(searchActivity);
        });
        exitBtn.setOnClickListener(v -> finish());
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            moviesList.clear();
                List<Movie> databaseMovies = intent.getParcelableArrayListExtra("DB_MOVIES");
                if(databaseMovies != null) {
                    Toast.makeText(context, "Items from DB: " + String.valueOf(databaseMovies.size()), Toast.LENGTH_SHORT).show();
                    moviesList.addAll(databaseMovies);
                    movieAdapter.notifyDataSetChanged();
                }
                else {
                Toast.makeText(context, "No objects in DB", Toast.LENGTH_SHORT).show();
            }
            Dialog.dismiss();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,new IntentFilter("DB_MOVIES_RESULT"));
        dataAccessService = new Intent(this, com.ITSMAP.movielist.Service.DataAccessService.class);
        dataAccessService.putExtra("COMMAND","GET_DB_MOVIES");
        startService(dataAccessService);
        Dialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        Dialog.dismiss();
    }
}
