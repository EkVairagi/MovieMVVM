package com.example.moviemvvm.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviemvvm.repositories.MostPopularTVShowRepository;
import com.example.moviemvvm.responses.TVShowResponse;

public class MostPopularTVShowsViewModel extends ViewModel
{
    private MostPopularTVShowRepository mostPopularTVShowRepository;

    public MostPopularTVShowsViewModel() {
        mostPopularTVShowRepository = new MostPopularTVShowRepository();
    }

    public LiveData<TVShowResponse> getMostPopularTVShows(int page)
    {
        return mostPopularTVShowRepository.getMostPopularTVShows(page);
    }

}
