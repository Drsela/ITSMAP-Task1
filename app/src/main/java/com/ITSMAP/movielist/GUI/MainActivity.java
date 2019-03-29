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
    List<com.ITSMAP.movielist.JSONResponse.Movie> moviesList;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private ProgressDialog Dialog;
    private Button exitBtn;

    private DataAccessService mService;
    MyReceiver mReciver;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateUI();

        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(v -> {
            Intent searchActivity = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(searchActivity);
        });
        exitBtn.setOnClickListener(v -> finish());
    }

    private void RegistorReciever() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DataAccessService.ACTION_FETCH_DB_MOVIES);
        mReciver = new MyReceiver();
        registerReceiver(mReciver,intentFilter);
    }

    private void initiateUI() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        exitBtn = findViewById(R.id.main_btn_exit);
        moviesList = new ArrayList<>();
        Dialog = new ProgressDialog(this);
        Dialog.setMessage("Fetching from database");
        Dialog.show();
        movieAdapter = new MovieAdapter(moviesList, this);
        recyclerView.setAdapter(movieAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        RegistorReciever();
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
        RegistorReciever();
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

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            List<Movie> movieListDB = arg1.getParcelableArrayListExtra(DataAccessService.RESULT_FETCH_DB_MOVIES);
            moviesList.clear();
            moviesList.addAll(movieListDB);
            movieAdapter.notifyDataSetChanged();
            Dialog.dismiss();
            Toast.makeText(MainActivity.this, "successful response fetched from DB", Toast.LENGTH_LONG).show();
        }
    }
}
