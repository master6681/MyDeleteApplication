package com.example.newsfeed.api;

import com.example.newsfeed.models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("top-headlines")
    Call<News> getNews(
            @Query("country") String country,
            @Query("apiKey") String apiKey
    );

    // For Search from keywords
    // http://newsapi.org/v2/everything?q=bitcoin&from=2020-04-29&sortBy=publishedAt&apiKey=45839046a88d45c68e518f4a86203b5e
    @GET("everything")
    Call<News> getNewsSearch(
            @Query("q") String keyword,
            @Query("language") String language,
            @Query("sortBy") String sortBy,
            @Query("apiKey") String apiKey
    );

}
