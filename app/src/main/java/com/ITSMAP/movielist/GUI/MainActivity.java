package com.ITSMAP.movielist.GUI;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.Toast;

import com.ITSMAP.movielist.Adapter.MovieAdapter;
import com.ITSMAP.movielist.JSONResponse.Movie;
import com.ITSMAP.movielist.R;
import com.ITSMAP.movielist.Service.DataAccessService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MovieAdapter movieAdapter;
    private List<Movie> moviesList;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private ProgressDialog dialog;
    private Button exitBtn;
    private DataAccessService mService;
    private serviceReceiver mReceiver;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateUI();

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(v -> {
            Intent searchActivity = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(searchActivity);
        });
        startService(new Intent(this, DataAccessService.class));
        exitBtn.setOnClickListener(v -> finish());
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DataAccessService.ACTION_FETCH_DB_MOVIES);
        mReceiver = new serviceReceiver();
        registerReceiver(mReceiver, intentFilter);
    }

    private void initiateUI() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        exitBtn = findViewById(R.id.main_btn_exit);
        moviesList = new ArrayList<>();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching from database");
        dialog.show();
        movieAdapter = new MovieAdapter(moviesList, this);
        recyclerView.setAdapter(movieAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver();
        Intent serviceIntent = new Intent(this, DataAccessService.class);
        bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mReceiver);
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            mService.getMoviesFromDB();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    private class serviceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            List<Movie> movieListDB = arg1.getParcelableArrayListExtra(DataAccessService.RESULT_FETCH_DB_MOVIES);
            moviesList.clear();
            moviesList.addAll(movieListDB);
            movieAdapter.notifyDataSetChanged();
            dialog.dismiss();
            Toast.makeText(MainActivity.this, "successful response fetched from DB", Toast.LENGTH_LONG).show();
        }
    }
}
