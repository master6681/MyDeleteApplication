package com.example.newsfeed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsfeed.api.ApiClient;
import com.example.newsfeed.api.ApiInterface;
import com.example.newsfeed.models.Article;
import com.example.newsfeed.models.News;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// https://www.youtube.com/watch?v=9oNZAzIhL7s


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String API_KEY = "45839046a88d45c68e518f4a86203b5e";
    private List<Article> articles = new ArrayList<>();
    private String TAG = MainActivity.class.getSimpleName();

    private TextView topHeadLine;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Adapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topHeadLine = findViewById(R.id.topHeadLine);
        //SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);
        //RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        //LoadJson(""); // если использовать без SwipeRefreshLayout

        onLoadingSwipeRefresh("");

    }

    public void LoadJson(final String keyword) {  //keyword - ключевое слово если нужен поиск по новостям
        topHeadLine.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(true);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        String country = Utils.getCountry();
        String language = Utils.getLanguage();

        Call<News> call;
        //Для поиска по новостям
        if (keyword.length() > 0) {
            call = apiInterface.getNewsSearch(keyword, language, "publishedAT", API_KEY);
        } else {
            call = apiInterface.getNews(country, API_KEY);
        }
        //Для поиска по новостям
        //call = apiInterface.getNews(country, API_KEY); // если со строкой поиска то в этом запросе нет необходимости

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (!response.isSuccessful() || response.body().getArticle() == null) {
                    topHeadLine.setVisibility(View.INVISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(MainActivity.this, "Error Response " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!articles.isEmpty()) {
                    articles.clear();
                }

                articles = response.body().getArticle();
                adapter = new Adapter(MainActivity.this, articles);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                topHeadLine.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                topHeadLine.setVisibility(View.INVISIBLE);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

//    private void initListener (){
//        adapter.
//    }


    /**
     * Для поиска по новостям, переопределяем Меню
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        // Retrieve the SearchView and plug it into SearchManager
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search Latest News...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.length() > 2) {  // строка которую вводит пользователь для поиска
                    onLoadingSwipeRefresh(s);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
              //  LoadJson(s); // нужен если нет onLoadingSwipeRefresh
                return false;
            }
        });
        searchMenuItem.getIcon().setVisible(false, false);
        return true;
    }

    /**
     * Реализация метода SwipeRefreshLayout
     */
    @Override
    public void onRefresh() {
       LoadJson("");
    }

    private void onLoadingSwipeRefresh(final String keyword) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                LoadJson(keyword);
            }
        });
    }
}
