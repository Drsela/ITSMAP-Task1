package com.ITSMAP.movielist;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.ITSMAP.movielist.DTO.Movie;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        Button exitBtn = findViewById(R.id.main_btn_exit);


        List<Movie> data = getData();
        List<Movie> moviesList = getMovieObjects(data);
        MovieAdapter adapter = new MovieAdapter(moviesList,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        exitBtn.setOnClickListener(v -> {
            finish();
        });

    }

    private List getData() {
        InputStream stream = getResources().openRawResource(R.raw.movielist);
        CSVReader reader = new CSVReader(stream);
        return reader.getMovies();
    }
    
    private ArrayList<Movie> getMovieObjects(List<Movie> data) {
        ArrayList<Movie> movieList = new ArrayList<>();
        data.remove(0);

        for (int i = 0; i < data.size(); i++) {
            Movie currentMovie = data.get(i);
            movieList.add(currentMovie);
        }
        return movieList;
    }
}
