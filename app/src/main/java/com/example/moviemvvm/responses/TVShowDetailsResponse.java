package com.example.moviemvvm.responses;

import com.example.moviemvvm.models.TVShowDetails;
import com.google.gson.annotations.SerializedName;

public class TVShowDetailsResponse
{
    @SerializedName("tvShow")
    private TVShowDetails tvShowDetails;

    public TVShowDetails getTvShowDetails() {
        return tvShowDetails;
    }

    public void setTvShowDetails(TVShowDetails tvShowDetails) {
        this.tvShowDetails = tvShowDetails;
    }
}
