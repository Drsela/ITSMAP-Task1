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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ITSMAP.movielist.Adapter.SearchAdapter;
import com.ITSMAP.movielist.JSONResponse.Search;
import com.ITSMAP.movielist.R;
import com.ITSMAP.movielist.Service.DataAccessService;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private SearchAdapter searchAdapter;
    private EditText searchTextBox;
    private Button searchButton;
    private RecyclerView recycleView;
    List<Search> searchResults;
    private ProgressDialog Dialog;

    private DataAccessService mService;
    MyReceiver mReciver;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        Dialog = new ProgressDialog(this);
    }

    private void initView() {
        searchTextBox = findViewById(R.id.searchTextBox);
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);
        searchResults = new ArrayList<>();
        Search search1 = new Search();
        search1.setTitle("TESTING");
        searchResults.add(search1);

        searchAdapter = new SearchAdapter(searchResults,this,SearchActivity.this);
        recycleView = findViewById(R.id.moviesFromSearchRecycleView);
        recycleView.setAdapter(searchAdapter);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.addItemDecoration(new DividerItemDecoration(recycleView.getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.searchButton:
                if(!searchTextBox.toString().isEmpty()) {
                    if(mBound){
                        mService.performSearchAPI(searchTextBox.getText().toString());
                        Dialog.setMessage("Fetching movies from API");
                        Dialog.show();
                    }
                }
                break;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DataAccessService.ACTION_FETCH_SEARCH_TITLES);
        mReciver = new MyReceiver();
        registerReceiver(mReciver,intentFilter);


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

    @Override
    protected void onPause() {
        super.onPause();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DataAccessService.LocalBinder binder = (DataAccessService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    public void addMovieToDB(String imdbId) {
        if(mBound){
            mService.addMovieFromSearch(imdbId);
            Toast.makeText(mService, "Movie added to DB", Toast.LENGTH_SHORT).show();
        }
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            List<Search> searchList = arg1.getParcelableArrayListExtra(DataAccessService.RESULT_FETCH_SEARCH_TITLES);
            searchResults.clear();
            searchResults.addAll(searchList);
            searchAdapter.notifyDataSetChanged();
            Dialog.dismiss();
            Toast.makeText(getApplicationContext(), "successful response fetched from API", Toast.LENGTH_LONG).show();
        }
    }

}
