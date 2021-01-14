package com.example.moviemvvm.listeners;

import com.example.moviemvvm.models.TVShow;

public interface WatchlistListener
{
    void onTVShowClicked(TVShow tvShow);

    void removeTVShowFromWatchList(TVShow tvShow,int position);
}
