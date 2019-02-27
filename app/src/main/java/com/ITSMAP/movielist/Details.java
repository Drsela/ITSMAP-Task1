package com.ITSMAP.movielist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ITSMAP.movielist.DTO.Movie;

import java.util.Objects;

public class Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        Movie clickedMovie = Objects.requireNonNull(intent.getExtras()).getParcelable("MOVIE");


    }
}
