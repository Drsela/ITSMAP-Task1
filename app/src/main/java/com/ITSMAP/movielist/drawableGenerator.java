package com.ITSMAP.movielist;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.ITSMAP.movielist.DTO.Movie;

public class drawableGenerator {
    private Context context;

    public drawableGenerator(Context context) {
        this.context = context;
    }

    public Drawable getDrawableByGenre(Movie currentMovie) {
        String genres = currentMovie.getGenres();
        if (genres.contains("Sci-fi")) {
            return context.getResources().getDrawable(R.drawable.category_sci_fi);
        }
        if (genres.contains("Action")) {
            return context.getResources().getDrawable(R.drawable.category_action);
        }
        if (genres.contains("Music")) {
            return context.getResources().getDrawable(R.drawable.category_music);
        }
        if (genres.contains("Drama")) {
            return context.getResources().getDrawable(R.drawable.category_drama);
        }

        return null;
    }
}
