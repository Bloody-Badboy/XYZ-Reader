package com.example.xyzreader.data.source.remote;

import com.example.xyzreader.data.source.ArticleDataSource;
import com.example.xyzreader.data.model.Article;
import java.io.IOException;
import java.util.List;

public class RemoteArticleDataSource implements ArticleDataSource {
  private static volatile RemoteArticleDataSource sInstance = null;
  private final ArticleDataService articleDataService;

  private RemoteArticleDataSource(ArticleDataService articleDataService) {
    this.articleDataService = articleDataService;
    if (sInstance != null) {
      throw new AssertionError(
          "Another instance of "
              + RemoteArticleDataSource.class.getName()
              + " class already exists, Can't create a new instance.");
    }
  }

  public static RemoteArticleDataSource getInstance(ArticleDataService articleDataService) {
    if (sInstance == null) {
      synchronized (RemoteArticleDataSource.class) {
        if (sInstance == null) {
          sInstance = new RemoteArticleDataSource(articleDataService);
        }
      }
    }
    return sInstance;
  }

  @Override public List<Article> getArticlesFromLocal() {
    throw new UnsupportedOperationException("Method not implemented.");
  }

  @Override public List<Article> getArticlesFromServer() throws IOException {
    return articleDataService.getArticles();
  }

  @Override public void storeArticlesLocally(List<Article> articles) {
    throw new UnsupportedOperationException("Method not implemented.");
  }

  @Override public Article getArticleById(int articleId) {
    throw new UnsupportedOperationException("Method not implemented.");
  }
}