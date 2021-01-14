package com.example.moviemvvm.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviemvvm.database.TVShowDatabase;
import com.example.moviemvvm.models.TVShow;
import com.example.moviemvvm.repositories.TVShowDetailsRepository;
import com.example.moviemvvm.responses.TVShowDetailsResponse;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class TVShowDetailsViewModel extends AndroidViewModel
{
    private TVShowDetailsRepository tvShowDetailsRepository;
    private TVShowDatabase tvShowDatabase;

    public TVShowDetailsViewModel(@NonNull Application application)
    {
        super(application);
        tvShowDetailsRepository = new TVShowDetailsRepository();
        tvShowDatabase = TVShowDatabase.getTvShowDatabase(application);
    }

    public LiveData<TVShowDetailsResponse> getTVShowDetails(String tvShowId)
    {
        return tvShowDetailsRepository.getTVShowDetails(tvShowId);
    }

    public Completable addToWatchList(TVShow tvShow)
    {
        return tvShowDatabase.tvShowDao().addToWatchList(tvShow);
    }

    public Flowable<TVShow> getTVShowFromWatchlist(String tvShowId)
    {
        return tvShowDatabase.tvShowDao().getTVShowFromWatchList(tvShowId);
    }

    public Completable removeTVShowFromWatchlist(TVShow tvShow)
    {
        return tvShowDatabase.tvShowDao().removeFromWatchList(tvShow);
    }

}
