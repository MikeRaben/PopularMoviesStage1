package com.bomberomedia.popularmoviesstage1;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv;

        ViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.movie_image);
        }
    }
}
