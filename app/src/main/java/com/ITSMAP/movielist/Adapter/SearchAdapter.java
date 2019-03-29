package com.ITSMAP.movielist.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ITSMAP.movielist.GUI.SearchActivity;
import com.ITSMAP.movielist.JSONResponse.Search;
import com.ITSMAP.movielist.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{
    private List<Search> searchList;
    private Context context;
    private SearchActivity searchActivity;

    public SearchAdapter(List<Search> searchList, Context context,SearchActivity activity) {
        this.searchList = searchList;
        this.context = context;
        this.searchActivity = activity;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.search_item, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder viewHolder, int i) {
        Search searchItem = searchList.get(i);
        viewHolder.movieTitle.setText(searchItem.getTitle());

    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView movieTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            movieTitle = itemView.findViewById(R.id.search_title);

            itemView.setOnClickListener(v -> {
                String imdbId = searchList.get(getAdapterPosition()).getImdbID();
                searchActivity.addMovieToDB(imdbId);
                Toast.makeText(context, "Fetching: " + searchList.get(getAdapterPosition()).getTitle() +". \nPress back to see movies", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
