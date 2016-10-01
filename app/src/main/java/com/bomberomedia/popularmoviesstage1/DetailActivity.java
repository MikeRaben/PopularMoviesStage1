package com.bomberomedia.popularmoviesstage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class DetailActivity extends AppCompatActivity {
    TextView overview, releaseDate;
    ImageView moviePoster;
    RatingBar rating;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null){
            ab.setDisplayShowTitleEnabled(false);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        overview = (TextView) findViewById(R.id.synopsis);
        releaseDate = (TextView) findViewById(R.id.release_date);
        moviePoster = (ImageView) findViewById(R.id.movie_poster);
        rating = (RatingBar) findViewById(R.id.ratingBar);

        populateViews();
    }

    private void populateViews() {
        Intent i = getIntent();

        toolbar.setTitle(i.getStringExtra("title"));
        String release = i.getStringExtra("release");
        releaseDate.setText(release.substring(0,4));

        overview.setText(i.getStringExtra("synopsis"));
        double readRating = i.getDoubleExtra("rating", 0.0);
        rating.setRating((float) readRating);

        String imgUrl = i.getStringExtra("poster");
        Glide.with(this).load(imgUrl)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(moviePoster);
    }
}
