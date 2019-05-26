package com.pariana.beaware.API;

import com.pariana.beaware.model.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("top-headlines")
    Call<News> getNews(
        @Query("country") String country ,
        @Query("apiKey") String apiKey
        );
}
