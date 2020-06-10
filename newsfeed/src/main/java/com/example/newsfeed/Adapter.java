package com.example.newsfeed;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.newsfeed.models.Article;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.RecyclerViewNewsHolder> {

    Context context;
    private List<Article> articles;

    public Adapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }


    @NonNull
    @Override
    public RecyclerViewNewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new RecyclerViewNewsHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewNewsHolder holder, int position) {
        Article articleHolder = articles.get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();
        requestOptions.timeout(3000);

        Glide.with(context)
                .load(articleHolder.getUrlToImage())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);
        holder.title.setText(articleHolder.getTitle());
        holder.desc.setText(articleHolder.getDescription());
        holder.source.setText(articleHolder.getSource().getName());
        holder.time.setText(" \u2022 " + Utils.DateToTimeFormat(articleHolder.getPublishedAt()));
        holder.published_ad.setText(Utils.DateFormat(articleHolder.getPublishedAt()));
        holder.author.setText(articleHolder.getAuthor());
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    /**
     * The type Recycler view news holder.
     */
    class RecyclerViewNewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, desc, author, published_ad, source, time;
        ImageView imageView;
        ProgressBar progressBar;

        public RecyclerViewNewsHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            author = itemView.findViewById(R.id.author);
            published_ad = itemView.findViewById(R.id.publishedAt);
            source = itemView.findViewById(R.id.source);
            time = itemView.findViewById(R.id.time);
            imageView = itemView.findViewById(R.id.img);
            progressBar = itemView.findViewById(R.id.progress_load_photo);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, NewsDetailActivity.class);

            int position = getAdapterPosition();
            Article article = articles.get(position);
            intent.putExtra("url", article.getUrl());
            intent.putExtra("title", article.getTitle());
            intent.putExtra("img", article.getUrlToImage());
            intent.putExtra("date", article.getPublishedAt());
            intent.putExtra("source", article.getSource().getName());
            intent.putExtra("author", article.getAuthor());
            context.startActivity(intent);
        }

    }
}
