package com.pariana.beaware;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pariana.beaware.API.APIClient;
import com.pariana.beaware.API.APIInterface;
import com.pariana.beaware.model.Article;
import com.pariana.beaware.model.News;
import com.pariana.beaware.model.Source;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ss.com.infinitescrollprovider.InfiniteScrollProvider;
import ss.com.infinitescrollprovider.OnLoadMoreListener;

public class GoogleNewsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private Snackbar snackbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar, postBar;
    public static final String API_KEY = "5060b8b3904e474c813bd84d0e770465";
    private List<Article> articles = new ArrayList<>();
    private String TAG = MainActivity.class.getSimpleName();
    private NewsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_news);

        toolbar = (Toolbar) findViewById(R.id.googleNewsToolbar);
        setSupportActionBar(toolbar);

        //MainActivity.TypeA ff =   new Gson().fromJson("{ \"A\":\"Ali\"}",MainActivity.TypeA.class);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        postBar = (ProgressBar) findViewById(R.id.postProgressbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        isOnline();
    }

    public void isOnline() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {
            if (snackbar != null)
                snackbar.dismiss();
            swipeRefreshLayout.setEnabled(true);
            progressBar.setVisibility(View.VISIBLE);
            requestData();
        } else {
            swipeRefreshLayout.setEnabled(false);
            View view = findViewById(R.id.googleNews_Activity);
            progressBar.setVisibility(View.GONE);
            snackbar = Snackbar.make(view, "Please check your connectivity...", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isOnline();
                }
            });
            View v = snackbar.getView();
            v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            snackbar.show();
        }
    }

    private void requestData() {

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//      recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        LoadJson();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadJson();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void LoadJson() {

        APIInterface apiInterface = APIClient.getApiClient().create(APIInterface.class);

        String country = Utils.getCountry();

        retrofit2.Call<News> call;
        call = apiInterface.getNews(country, API_KEY);

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if(response.isSuccessful() && response.body() != null) {
                    if (!articles.isEmpty())
                        articles.clear();

                    articles = response.body().getArticles();
                    adapter = new NewsAdapter(GoogleNewsActivity.this, articles);
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();

//                    initListener();
                }
                else
                    Toast.makeText(GoogleNewsActivity.this, "No Result", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.i(TAG, t.getMessage());
            }
        });
    }
}
