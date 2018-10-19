package com.example.xyzreader.inject;

import android.content.Context;
import com.example.xyzreader.App;
import com.example.xyzreader.data.source.ArticleDataSource;
import com.example.xyzreader.data.ArticlesDataRepository;
import com.example.xyzreader.data.source.local.LocalArticleDataSource;
import com.example.xyzreader.data.source.local.db.ArticlesDatabase;
import com.example.xyzreader.data.source.remote.ArticleDataService;
import com.example.xyzreader.data.source.remote.RemoteArticleDataSource;
import com.example.xyzreader.utils.scheduler.AsyncScheduler;
import com.example.xyzreader.utils.scheduler.Scheduler;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import okhttp3.OkHttpClient;

@SuppressWarnings("WeakerAccess") public class Injection {

  private static volatile OkHttpClient sOkHttpClientInstance = null;

  public static Context provideApplicationContext() {
    return App.getInstance();
  }

  private static OkHttpClient provideOkHttpClient() {
    if (sOkHttpClientInstance == null) {
      synchronized (Injection.class) {
        if (sOkHttpClientInstance == null) {
          sOkHttpClientInstance =
              new OkHttpClient.Builder().addInterceptor(new StethoInterceptor()).build();
        }
      }
    }
    return sOkHttpClientInstance;
  }

  private static ArticleDataService provideRemoteFeedDataService() {
    return ArticleDataService.getInstance(Injection.provideOkHttpClient());
  }

  private static ArticlesDatabase provideRecipeDatabase() {
    return ArticlesDatabase.getInstance(Injection.provideApplicationContext());
  }

  private static ArticleDataSource provideArticlesRemoteDataSource() {
    return RemoteArticleDataSource.getInstance(Injection.provideRemoteFeedDataService());
  }

  private static ArticleDataSource provideArticlesLocalDataSource() {
    return LocalArticleDataSource.getInstance(Injection.provideRecipeDatabase());
  }

  public static ArticlesDataRepository provideArticlesDataRepository() {
    return ArticlesDataRepository.getInstance(
        Injection.provideArticlesLocalDataSource(),
        Injection.provideArticlesRemoteDataSource()
    );
  }

  public static Scheduler provideScheduler(){
    return AsyncScheduler.getInstance();
  }
}
