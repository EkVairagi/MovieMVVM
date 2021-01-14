package com.example.moviemvvm.responses;

import com.example.moviemvvm.models.TVShow;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TVShowResponse
{
    @SerializedName("page")
    private int page;

    @SerializedName("pages")
    private int pages;

    @SerializedName("tv_shows")
    private List<TVShow> tvShows;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<TVShow> getTvShows() {
        return tvShows;
    }

    public void setTvShows(List<TVShow> tvShows) {
        this.tvShows = tvShows;
    }
}