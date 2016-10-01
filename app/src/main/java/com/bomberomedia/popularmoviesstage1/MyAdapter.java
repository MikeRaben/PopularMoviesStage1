package com.bomberomedia.popularmoviesstage1;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    private ArrayList<MyMovie> mMovies;
    private Context mContext;

    MyAdapter(ArrayList<MyMovie> movies, Context context){
        mMovies = movies;
        mContext = context;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        String imgUrl = mMovies.get(position).getImageUrl();
        Glide.with(mContext).load(imgUrl)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.iv);
        holder.position = holder.getAdapterPosition();
    }


    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        int position;
        ImageView iv;

        ViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            MyMovie movie = mMovies.get(position);

            Intent intent = new Intent(v.getContext(), DetailActivity.class);
            intent.putExtra("title", movie.title);
            intent.putExtra("poster", movie.getImageUrl());
            intent.putExtra("release", movie.releaseDate);
            intent.putExtra("rating", movie.rating);
            intent.putExtra("synopsis", movie.synopsis);

            v.getContext().startActivity(intent);
        }
    }
}
