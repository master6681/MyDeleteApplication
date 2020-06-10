package com.example.newsfeed.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class News {

    @SerializedName("status")
    private String status;
    @SerializedName("totalResults")
    private int totalResult;
    @SerializedName("articles")
    private List<Article> article;
    //articles: [
//+{
    // -"source": {
    //"id": null,
    //"name": "DailyFX"
    // },
//"author": "Mahmoud Alkudsi",
//"title": "Bitcoin Forecast: BTC/USD Price – Move Higher Hinted Ahead",
//"description": "Key chart levels and signals to keep in focus",
//"url": "https://www.dailyfx.com/forex/technical/article/special_report/2020/05/29/Bitcoin-Forecast-BTCUSD-Price--Move-Higher-Hinted-Ahead-MK.html",
//"urlToImage": "https://a.c-dn.net/b/2jPuVf/headline_shutterstock_365875643.jpg",
//"publishedAt": "2020-05-29T09:30:00Z",
//"content":
//
// },
//+{ … },
//+{ … }]

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public List<Article> getArticle() {
        return article;
    }

    public void setArticle(List<Article> article) {
        this.article = article;
    }

}
