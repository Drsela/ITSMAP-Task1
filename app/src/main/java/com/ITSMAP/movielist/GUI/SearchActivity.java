package com.ITSMAP.movielist.GUI;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    private List<Search> searchResults;
    private ProgressDialog Dialog;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            searchResults.clear();
            List<Search> searchResultsFromService = intent.getParcelableArrayListExtra("SEARCHED_MOVIES");
            Dialog.dismiss();
            searchResults.addAll(searchResultsFromService);
            searchAdapter.notifyDataSetChanged();
        }
    };
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

        searchAdapter = new SearchAdapter(searchResults,this);
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
                    Intent searchIntent = new Intent(this, DataAccessService.class);
                    searchIntent.putExtra("COMMAND","SEARCH_MOVIES");
                    searchIntent.putExtra("ADDITIONAL_COMMAND",searchTextBox.getText().toString());
                    startService(searchIntent);
                    Dialog.setMessage("Fetching movies from API");
                    Dialog.show();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,new IntentFilter("SEARCH_RESULT"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }
}
