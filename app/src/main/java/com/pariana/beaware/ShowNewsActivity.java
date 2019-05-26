package com.pariana.beaware;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class ShowNewsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView title, content, author, source, publishedAt, time, link;
    private ImageView newsImage;
    private ProgressBar imagePbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news);

        initComponent();
        setValues();
    }

    private void initComponent() {
        toolbar = (Toolbar) findViewById(R.id.googleNewsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        title = (TextView) findViewById(R.id.newsTitle);
        content = (TextView) findViewById(R.id.newsContent);
        author = (TextView) findViewById(R.id.author);
        source = (TextView) findViewById(R.id.source);
        publishedAt = (TextView) findViewById(R.id.publishedAt);
        time = (TextView) findViewById(R.id.time);
        link = (TextView) findViewById(R.id.linkTextview);
        newsImage = (ImageView) findViewById(R.id.showOriginalImage);
        imagePbar = (ProgressBar) findViewById(R.id.imagepbar);
    }


    public void setValues() {
        final Intent intent = this.getIntent();
            title.setText(intent.getStringExtra("title"));
            content.setText(intent.getStringExtra("content"));
            author.setText("Author : " + intent.getStringExtra("author"));
            source.setText("Source : " + intent.getStringExtra("source"));

            String unConvertedTime = intent.getStringExtra("time");
            System.out.print(intent.getStringExtra("imageURL"));
            Glide.with(this)
                    .load(intent.getStringExtra("imageURL"))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            imagePbar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            imagePbar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .centerCrop()
                    .into(newsImage);

            publishedAt.setText(Utils.DateFormat(unConvertedTime));
            time.setText("\u2022" + Utils.DateToTimeFormat(unConvertedTime));

            link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(intent.getStringExtra("link"))));
                }
            });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
