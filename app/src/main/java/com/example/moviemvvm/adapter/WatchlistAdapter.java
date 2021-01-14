package com.example.moviemvvm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviemvvm.R;
import com.example.moviemvvm.databinding.ItemContainerTvShowBinding;
import com.example.moviemvvm.listeners.TVShowListener;
import com.example.moviemvvm.listeners.WatchlistListener;
import com.example.moviemvvm.models.TVShow;

import java.util.List;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.TVShowViewHolder>
{
    private List<TVShow> tvShows;
    private LayoutInflater layoutInflater;

    private WatchlistListener watchlistListener;


    public WatchlistAdapter(List<TVShow> tvShows, WatchlistListener watchlistListener) {
        this.tvShows = tvShows;
        this.watchlistListener = watchlistListener;
    }

    @NonNull
    @Override
    public TVShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater==null)
        {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerTvShowBinding itemContainerTvShowBinding = DataBindingUtil
                .inflate(layoutInflater, R.layout.item_container_tv_show,parent,false);
        return new TVShowViewHolder(itemContainerTvShowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowViewHolder holder, int position) {
        holder.bindShow(tvShows.get(position));
    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

    class TVShowViewHolder extends RecyclerView.ViewHolder
    {
        private ItemContainerTvShowBinding itemContainerBinding;
        public TVShowViewHolder(ItemContainerTvShowBinding itemContainerBinding) {
            super(itemContainerBinding.getRoot());
            this.itemContainerBinding = itemContainerBinding;
        }

        public void bindShow(TVShow tvShow)
        {
            itemContainerBinding.setTvShow(tvShow);
            itemContainerBinding.executePendingBindings();
            itemContainerBinding.getRoot().setOnClickListener(v -> watchlistListener.onTVShowClicked(tvShow));
            itemContainerBinding.imageDelete.setOnClickListener(v -> watchlistListener.removeTVShowFromWatchList(tvShow,getAdapterPosition()));
            itemContainerBinding.imageDelete.setVisibility(View.VISIBLE);

        }

    }
}
