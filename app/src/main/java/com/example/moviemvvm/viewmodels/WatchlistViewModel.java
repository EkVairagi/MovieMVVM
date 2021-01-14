package com.example.moviemvvm.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.moviemvvm.database.TVShowDatabase;
import com.example.moviemvvm.models.TVShow;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class WatchlistViewModel extends AndroidViewModel
{
    private TVShowDatabase tvShowDatabase;
    public WatchlistViewModel(@NonNull Application application) {
        super(application);
        tvShowDatabase = TVShowDatabase.getTvShowDatabase(application);
    }

    public Flowable<List<TVShow>> loadWatchList()
    {
        return tvShowDatabase.tvShowDao().getWatchList();
    }

    public Completable removeTVShowFromWatchList(TVShow tvShow)
    {
        return tvShowDatabase.tvShowDao().removeFromWatchList(tvShow);
    }

}
