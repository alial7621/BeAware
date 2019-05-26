package com.pariana.beaware;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.pariana.beaware.model.Article;
import com.pariana.beaware.model.Source;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Context context;
    private List<Article> articles;

    public NewsAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }


    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item, viewGroup, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, final int i) {
        Article article = articles.get(i);

        newsViewHolder.newsTitle.setText(article.getTitle());
        newsViewHolder.description.setText(article.getDescription());
        Glide.with(context)
                .load(article.getUrlToImage())
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(newsViewHolder.newsImage);
        newsViewHolder.publishedAt.setText("\u2022" + Utils.DateToTimeFormat(article.getPublishedAt()));

        newsViewHolder.cardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Article article = articles.get(i);
//
                Intent intent = new Intent(context, ShowNewsActivity.class);
                intent.putExtra("imageURL", article.getUrlToImage());
                intent.putExtra("title", article.getTitle());
                intent.putExtra("content", article.getContent());
                intent.putExtra("author", article.getAuthor());
                intent.putExtra("source", article.getSource().getName());
                intent.putExtra("time", article.getPublishedAt());
                intent.putExtra("link", article.getUrl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        private ImageView newsImage;
        private TextView newsTitle;
        private TextView description;
        private TextView publishedAt;
        private CardView cardItem;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsImage = (ImageView) itemView.findViewById(R.id.newsImage);
            newsTitle = (TextView) itemView.findViewById(R.id.newsTitle);
            description = (TextView) itemView.findViewById(R.id.description);
            publishedAt = (TextView) itemView.findViewById(R.id.publishedAt);
            cardItem = (CardView) itemView.findViewById(R.id.cardItem);
        }
    }
}
