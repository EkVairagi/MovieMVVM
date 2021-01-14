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
import com.example.moviemvvm.models.TVShow;

import java.util.List;

public class TVShowAdapter extends RecyclerView.Adapter<TVShowAdapter.TVShowViewHolder>
{
    private List<TVShow> tvShows;
    private LayoutInflater layoutInflater;

    private TVShowListener tvShowListener;

    public TVShowAdapter(List<TVShow> tvShows, TVShowListener tvShowListener) {
        this.tvShows = tvShows;
        this.tvShowListener = tvShowListener;
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
            itemContainerBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvShowListener.onTVShowClicked(tvShow);
                }
            });
        }

    }
}
