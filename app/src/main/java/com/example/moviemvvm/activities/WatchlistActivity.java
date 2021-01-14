package com.example.moviemvvm.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.moviemvvm.R;
import com.example.moviemvvm.adapter.WatchlistAdapter;
import com.example.moviemvvm.databinding.ActivityWatchlistBinding;
import com.example.moviemvvm.listeners.WatchlistListener;
import com.example.moviemvvm.models.TVShow;
import com.example.moviemvvm.utilities.TempDataHolder;
import com.example.moviemvvm.viewmodels.WatchlistViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WatchlistActivity extends AppCompatActivity implements WatchlistListener{
    private ActivityWatchlistBinding activityWatchlistBinding;
    private WatchlistViewModel watchlistViewModel;
    private WatchlistAdapter watchlistAdapter;
    private List<TVShow> watchList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWatchlistBinding = DataBindingUtil.setContentView(this,R.layout.activity_watchlist);

        doInitialization();
    }

    public void doInitialization()
    {
        watchlistViewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
        activityWatchlistBinding.imageBack.setOnClickListener(v -> onBackPressed());
        watchList = new ArrayList<>();
        loadWatchlist();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TempDataHolder.IS_WATCHLIST_UPDATED)
        {
            loadWatchlist();
            TempDataHolder.IS_WATCHLIST_UPDATED = false;
        }
    }

    private void loadWatchlist()
    {
        activityWatchlistBinding.setIsLoading(true);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(watchlistViewModel.loadWatchList().subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
         .subscribe(tvShows -> {
             activityWatchlistBinding.setIsLoading(false);

             if (watchList.size()>0)
             {
                 watchList.clear();
             }
             watchList.addAll(tvShows);
             watchlistAdapter = new WatchlistAdapter(watchList,this);
             activityWatchlistBinding.watchlistRecyclerview.setAdapter(watchlistAdapter);
             activityWatchlistBinding.watchlistRecyclerview.setVisibility(View.VISIBLE);
             compositeDisposable.dispose();

         }));
    }

    @Override
    public void onTVShowClicked(TVShow tvShow)
    {
        Intent intent = new Intent(WatchlistActivity.this,TVShowDetailsActivity.class);
        intent.putExtra("tvShow",tvShow);
        startActivity(intent);
    }

    @Override
    public void removeTVShowFromWatchList(TVShow tvShow, int position)
    {
        CompositeDisposable compositeDisposableForDelete = new CompositeDisposable();
        compositeDisposableForDelete.add(watchlistViewModel.removeTVShowFromWatchList(tvShow)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    watchList.remove(position);
                    watchlistAdapter.notifyItemRemoved(position);
                    watchlistAdapter.notifyItemRangeChanged(position, watchlistAdapter.getItemCount());
                    compositeDisposableForDelete.dispose();
                }));
    }
}