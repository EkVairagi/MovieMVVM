package com.example.moviemvvm.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.moviemvvm.R;
import com.example.moviemvvm.adapter.TVShowAdapter;
import com.example.moviemvvm.databinding.ActivityMainBinding;
import com.example.moviemvvm.listeners.TVShowListener;
import com.example.moviemvvm.models.TVShow;
import com.example.moviemvvm.models.TVShowDetails;
import com.example.moviemvvm.viewmodels.MostPopularTVShowsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TVShowListener
{
    private MostPopularTVShowsViewModel viewModel;
    private ActivityMainBinding activityMainBinding;
    private List<TVShow> tvShows = new ArrayList<>();
    private TVShowAdapter tvShowAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        doInitialization();
    }

    private void doInitialization()
    {
        activityMainBinding.tvShowRecyclerView.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPopularTVShowsViewModel.class);
        tvShowAdapter = new TVShowAdapter(tvShows,this);
        activityMainBinding.tvShowRecyclerView.setAdapter(tvShowAdapter);
        activityMainBinding.tvShowRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!activityMainBinding.tvShowRecyclerView.canScrollHorizontally(1))
                {
                    if (currentPage<=totalAvailablePages)
                    {
                        currentPage+=1;
                        getMostPopularTVShows();
                    }
                }
            }
        });
        activityMainBinding.imageWatchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,WatchlistActivity.class));
            }
        });

        activityMainBinding.imageSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SearchActivity.class));
            }
        });

        getMostPopularTVShows();
    }

    private void getMostPopularTVShows()
    {
        toggleLoading();
        viewModel.getMostPopularTVShows(currentPage).observe(this,mostPopularTVShowsResponse ->
                {
                    toggleLoading();
                    if (mostPopularTVShowsResponse!=null)
                    {
                        totalAvailablePages = mostPopularTVShowsResponse.getPages();
                        if (mostPopularTVShowsResponse.getTvShows()!=null)
                        {
                            int oldCount = tvShows.size();
                            tvShows.addAll(mostPopularTVShowsResponse.getTvShows());
                            tvShowAdapter.notifyItemRangeInserted(oldCount,tvShows.size());



                        }
                    }

                }
                );
    }

    private void toggleLoading()
    {
        if (currentPage==1)
        {
            if (activityMainBinding.getIsLoading()!=null && activityMainBinding.getIsLoading())
            {
                activityMainBinding.setIsLoading(false);
            }
            else
            {
                activityMainBinding.setIsLoading(true);
            }
        }
        else
        {
            if (activityMainBinding.getIsLoadingMore()!=null && activityMainBinding.getIsLoadingMore())
            {
                activityMainBinding.setIsLoadingMore(false);
            }
            else
            {
                activityMainBinding.setIsLoadingMore(true);
            }
        }
    }


    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Intent intent = new Intent(getApplicationContext(), TVShowDetailsActivity.class);
/*
        intent.putExtra("id",tvShow.getId());
        intent.putExtra("name",tvShow.getName());
        intent.putExtra("startDate",tvShow.getStart_date());
        intent.putExtra("country",tvShow.getCountry());
        intent.putExtra("network",tvShow.getNetwork());
        intent.putExtra("status",tvShow.getStatus());
*/
        intent.putExtra("tvShow",tvShow);
        startActivity(intent);
    }
}