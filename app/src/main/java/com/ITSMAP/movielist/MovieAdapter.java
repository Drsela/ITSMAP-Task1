package com.ITSMAP.movielist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ITSMAP.movielist.DTO.Movie;
import java.util.List;
import java.util.Objects;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private List<com.ITSMAP.movielist.DTO.Movie>  movieList;
    private Context context;
    private static final Integer MOVIE_FROM_ADAPTER_CODE = 100;
    private int lastClickedIndex;
    private drawableGenerator drawableGenerator;
    public static String MOVIE_FROM_ADAPTER = "Adapter movie";

    public MovieAdapter(List<com.ITSMAP.movielist.DTO.Movie> movies, Context context){
        movieList = movies;
        this.context = context;
        drawableGenerator = new drawableGenerator(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.movie_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Movie currentMovie = movieList.get(i);

        viewHolder.movieName.setText(currentMovie.getName());
        viewHolder.movieRating.setText(currentMovie.getiMDBRating());
        viewHolder.movieSeenStatus.setText(R.string.watchStatus);
        viewHolder.movieSeenStatus.append(currentMovie.hasBeenWatched() ? context.getResources().getString(R.string.edit_movie_watched_status) : context.getResources().getString(R.string.movie_not_seen));
        viewHolder.movieUserRating.setText(currentMovie.hasUserRating() ? currentMovie.getUserRating() : null);
        viewHolder.poster.setImageDrawable(drawableGenerator.getDrawableByGenre(currentMovie));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView movieName;
        TextView movieRating;
        TextView movieUserRating;
        TextView movieSeenStatus;
        ImageView poster;

        public ViewHolder(
        View itemView) {
            super(itemView);

            movieName = itemView.findViewById(R.id.list_item_title);
            movieRating = itemView.findViewById(R.id.list_item_rating);
            movieUserRating = itemView.findViewById(R.id.list_item_userRating);
            movieSeenStatus = itemView.findViewById(R.id.list_item_viewStatus);
            poster = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(v -> {
                Movie clickedMovie = movieList.get(getAdapterPosition());
                Intent detailsIntent = new Intent(context, Details.class);

                detailsIntent.putExtra(MOVIE_FROM_ADAPTER,clickedMovie);
                context.startActivity(detailsIntent);
            });

            itemView.setOnLongClickListener(v -> {
                lastClickedIndex = getAdapterPosition();
                Movie clickedMovie = movieList.get(getAdapterPosition());
                Intent detailsIntent = new Intent(context, EditActivity.class);
                detailsIntent.putExtra(MOVIE_FROM_ADAPTER,clickedMovie);
                ((Activity) context).startActivityForResult(detailsIntent,MOVIE_FROM_ADAPTER_CODE);
                return true;
            });
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MyAdapter", "onActivityResult");
        if (requestCode == MOVIE_FROM_ADAPTER_CODE) {
            if(resultCode == Activity.RESULT_OK){
                Movie updatedMovie = Objects.requireNonNull(data.getExtras()).getParcelable("TEST");
                movieList.set(lastClickedIndex, updatedMovie);
                notifyDataSetChanged();
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }
}
