package com.example.moviemvvm.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.moviemvvm.R;
import com.example.moviemvvm.adapter.EpisodeAdapter;
import com.example.moviemvvm.adapter.ImageSliderAdapter;
import com.example.moviemvvm.databinding.ActivityTVShowDetailsBinding;
import com.example.moviemvvm.databinding.LayoutEpisodeBottomSheetBinding;
import com.example.moviemvvm.models.TVShow;
import com.example.moviemvvm.models.TVShowDetails;
import com.example.moviemvvm.utilities.TempDataHolder;
import com.example.moviemvvm.viewmodels.TVShowDetailsViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TVShowDetailsActivity extends AppCompatActivity {

    private ActivityTVShowDetailsBinding activityTVShowDetailsBinding;
    private TVShowDetailsViewModel tvShowDetailsViewModel;

    private BottomSheetDialog episodeBottomSheetDialog;
    private LayoutEpisodeBottomSheetBinding layoutEpisodeBottomSheetBinding;

    private TVShow tvShow;

    private Boolean isTVShowAvailableWatchlist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTVShowDetailsBinding = DataBindingUtil.setContentView(this,R.layout.activity_t_v_show_details);
        doInitialization();

    }

    private void doInitialization() {
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TVShowDetailsViewModel.class);
        activityTVShowDetailsBinding.imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvShow = (TVShow) getIntent().getSerializableExtra("tvShow");
        checkTVShowInWatchlist();
        getTVShowDetails();
    }

    private void checkTVShowInWatchlist()
    {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(tvShowDetailsViewModel.
                getTVShowFromWatchlist(String.valueOf(tvShow.getId()))
        .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShow->{
                    isTVShowAvailableWatchlist = true;
                    activityTVShowDetailsBinding.imageWatchList.setImageResource(R.drawable.ic_added);
                    compositeDisposable.dispose();
                })

        );



    }


    private void getTVShowDetails()
    {
        activityTVShowDetailsBinding.setIsLoading(true);
//        String tvShowId = String.valueOf(getIntent().getIntExtra("id",-1));
        String tvShowId = String.valueOf(tvShow.getId());

        tvShowDetailsViewModel.getTVShowDetails(tvShowId).observe(this,tvShowDetailsResponse ->
                {
                    activityTVShowDetailsBinding.setIsLoading(false);

                    if (tvShowDetailsResponse.getTvShowDetails()!=null)
                    {
                        if (tvShowDetailsResponse.getTvShowDetails().getPictures()!=null)
                        {
                            loadImageSlider(tvShowDetailsResponse.getTvShowDetails().getPictures());
                        }

                         activityTVShowDetailsBinding.setTvShowImageURL(tvShowDetailsResponse.getTvShowDetails().getImagePath());

                        activityTVShowDetailsBinding.imageTVShow.setVisibility(View.VISIBLE);

                        activityTVShowDetailsBinding.setDescription(String.valueOf(HtmlCompat.fromHtml
                                (tvShowDetailsResponse.getTvShowDetails().getDescription(),HtmlCompat.FROM_HTML_MODE_LEGACY)));

                        activityTVShowDetailsBinding.textDescription.setVisibility(View.VISIBLE);
                        activityTVShowDetailsBinding.textReadMore.setVisibility(View.VISIBLE);
                        activityTVShowDetailsBinding.textReadMore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (activityTVShowDetailsBinding.textReadMore.getText().toString().equals("Read more"))
                                {
                                    activityTVShowDetailsBinding.textDescription.setMaxLines(Integer.MAX_VALUE);
                                    activityTVShowDetailsBinding.textDescription.setEllipsize(null);
                                    activityTVShowDetailsBinding.textReadMore.setText(R.string.read_less);

                                }
                                else
                                {
                                    activityTVShowDetailsBinding.textDescription.setMaxLines(4);
                                    activityTVShowDetailsBinding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                                    activityTVShowDetailsBinding.textReadMore.setText(R.string.read_more);

                                }
                            }
                        });

                        activityTVShowDetailsBinding.setRating(String.format(Locale.getDefault(),"%.2f",
                                Double.parseDouble(tvShowDetailsResponse.getTvShowDetails().getRating())));


                        if (tvShowDetailsResponse.getTvShowDetails().getGenres()!=null)
                        {
                            activityTVShowDetailsBinding.setGenre(tvShowDetailsResponse.getTvShowDetails().getGenres()[0]);
                        }
                        else
                        {
                            activityTVShowDetailsBinding.setGenre("N/A");
                        }

                        activityTVShowDetailsBinding.setRuntime(tvShowDetailsResponse.getTvShowDetails().getRuntime()+" Min");
                        activityTVShowDetailsBinding.viewDivider1.setVisibility(View.VISIBLE);
                        activityTVShowDetailsBinding.layoutMisc.setVisibility(View.VISIBLE);
                        activityTVShowDetailsBinding.viewDivider2.setVisibility(View.VISIBLE);
                        activityTVShowDetailsBinding.buttonWebsite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(tvShowDetailsResponse.getTvShowDetails().getUrl()));
                                startActivity(intent);
                            }
                        });
                        activityTVShowDetailsBinding.buttonWebsite.setVisibility(View.VISIBLE);
                        activityTVShowDetailsBinding.buttonEpisodes.setVisibility(View.VISIBLE);

                        activityTVShowDetailsBinding.buttonEpisodes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (episodeBottomSheetDialog==null)
                                {
                                    episodeBottomSheetDialog = new BottomSheetDialog(TVShowDetailsActivity.this);
                                    layoutEpisodeBottomSheetBinding = DataBindingUtil.inflate(
                                            LayoutInflater.from(TVShowDetailsActivity.this),
                                            R.layout.layout_episode_bottom_sheet,findViewById(R.id.episodesContainer),false
                                    );

                                    episodeBottomSheetDialog.setContentView(layoutEpisodeBottomSheetBinding.getRoot());
                                    layoutEpisodeBottomSheetBinding.episodeRecyclerview.setAdapter(new EpisodeAdapter(
                                            tvShowDetailsResponse.getTvShowDetails().getEpisodes()
                                    ));

/*
                                    layoutEpisodeBottomSheetBinding.textTitle.setText(String.format(
                                            "Episodes | %s",getIntent().getStringExtra("name")
                                    ));
*/

                                    layoutEpisodeBottomSheetBinding.textTitle.setText(String.format(
                                            "Episodes | %s",tvShow.getName()
                                    ));

                                    layoutEpisodeBottomSheetBinding.imageClose.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            episodeBottomSheetDialog.dismiss();
                                        }
                                    });

                                }

                                FrameLayout frameLayout = episodeBottomSheetDialog.findViewById(
                                        com.google.android.material.R.id.design_bottom_sheet);

                                if (frameLayout!=null)
                                {
                                    BottomSheetBehavior<View> bottomSheetBehavior =BottomSheetBehavior.from(frameLayout);
                                    bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                }

                                episodeBottomSheetDialog.show();

                            }
                        });

                        activityTVShowDetailsBinding.imageWatchList.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                CompositeDisposable compositeDisposable = new CompositeDisposable();

                                if (isTVShowAvailableWatchlist)
                                {
                                    compositeDisposable.add(tvShowDetailsViewModel.removeTVShowFromWatchlist(tvShow)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(()->{

                                        isTVShowAvailableWatchlist = false;
                                        TempDataHolder.IS_WATCHLIST_UPDATED = true;
                                        activityTVShowDetailsBinding.imageWatchList.setImageResource(R.drawable.ic_watch);
                                        Toast.makeText(TVShowDetailsActivity.this, "Remove from watchlist", Toast.LENGTH_SHORT).show();
                                        compositeDisposable.dispose();
                                    }));
                                }
                                else
                                {

                                    compositeDisposable.add(tvShowDetailsViewModel.addToWatchList(tvShow)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread()).subscribe(()->{
                                                TempDataHolder.IS_WATCHLIST_UPDATED = true;
                                                activityTVShowDetailsBinding.imageWatchList.setImageResource(R.drawable.ic_added);
                                                Toast.makeText(TVShowDetailsActivity.this, "Added to watchlist", Toast.LENGTH_SHORT).show();
                                                compositeDisposable.dispose();
                                            }));

                                }
                            }
                        });
                        activityTVShowDetailsBinding.imageWatchList.setVisibility(View.VISIBLE);
                        loadBasicTVShowDetails();
                    }

                }
                );


    }

    private void loadImageSlider(String[] sliderImages)
    {
        activityTVShowDetailsBinding.sliderViewPager.setOffscreenPageLimit(1);
        activityTVShowDetailsBinding.sliderViewPager.setAdapter(new ImageSliderAdapter(sliderImages));
        activityTVShowDetailsBinding.sliderViewPager.setVisibility(View.VISIBLE);
        activityTVShowDetailsBinding.viewFadingPage.setVisibility(View.VISIBLE);

        setupSliderIndicators(sliderImages.length);
        activityTVShowDetailsBinding.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicator(position);
            }
        });
    }

    private void setupSliderIndicators(int count)
    {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(8,0,8,0);
        for (int i=0;i<indicators.length;i++)
        {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.background_slider_indicator_inactive));

            indicators[i].setLayoutParams(layoutParams);
            activityTVShowDetailsBinding.layoutSliderIndicators.addView(indicators[i]);
        }
        activityTVShowDetailsBinding.layoutSliderIndicators.setVisibility(View.VISIBLE);
        setCurrentSliderIndicator(0);
    }

    private void setCurrentSliderIndicator(int position)
    {
        int childCount = activityTVShowDetailsBinding.layoutSliderIndicators.getChildCount();
        for (int i=0;i<childCount;i++)
        {
            ImageView imageView = (ImageView) activityTVShowDetailsBinding
                    .layoutSliderIndicators.getChildAt(i);

            if (i==position)
            {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_slider_indicator_active));
            }
            else
            {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_slider_indicator_inactive));

            }

        }
    }

    private void loadBasicTVShowDetails()
    {
/*
        activityTVShowDetailsBinding.setTvShowName(getIntent().getStringExtra("name"));
        activityTVShowDetailsBinding.setNetworkCountry(getIntent().getStringExtra("network")+" ("+
                getIntent().getStringExtra("country")+")");

        activityTVShowDetailsBinding.setStatus(getIntent().getStringExtra("status"));
        activityTVShowDetailsBinding.setStartDate(getIntent().getStringExtra("startDate"));
*/

        activityTVShowDetailsBinding.setTvShowName(tvShow.getName());
        activityTVShowDetailsBinding.setNetworkCountry(tvShow.getNetwork()+" ("+
                tvShow.getCountry()+")");

        activityTVShowDetailsBinding.setStatus(tvShow.getStatus());
        activityTVShowDetailsBinding.setStartDate(tvShow.getStart_date());


        activityTVShowDetailsBinding.textName.setVisibility(View.VISIBLE);
        activityTVShowDetailsBinding.textNetworkCountry.setVisibility(View.VISIBLE);
        activityTVShowDetailsBinding.textStatus.setVisibility(View.VISIBLE);
        activityTVShowDetailsBinding.textStarted.setVisibility(View.VISIBLE);



    }



}