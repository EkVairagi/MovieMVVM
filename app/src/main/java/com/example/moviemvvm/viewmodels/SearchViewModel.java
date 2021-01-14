package com.example.moviemvvm.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviemvvm.repositories.SearchTVShowRepository;
import com.example.moviemvvm.responses.TVShowResponse;

public class SearchViewModel extends ViewModel
{
    private SearchTVShowRepository searchTVShowRepository;

    public SearchViewModel() {
        searchTVShowRepository = new SearchTVShowRepository();
    }

    public LiveData<TVShowResponse> searchTVShow(String query,int page)
    {
        return searchTVShowRepository.searchTVShow(query, page);
    }

}
