package com.example.xyzreader.data;

import com.example.xyzreader.data.model.Article;
import com.example.xyzreader.data.source.ArticleDataSource;
import java.util.List;
import timber.log.Timber;

public class ArticlesDataRepository {
  private static volatile ArticlesDataRepository sInstance = null;
  private final ArticleDataSource articleLocalDataSource;
  private final ArticleDataSource articleRemoteDataSource;

  private ArticlesDataRepository(ArticleDataSource articleLocalDataSource,
      ArticleDataSource articleRemoteDataSource) {
    this.articleLocalDataSource = articleLocalDataSource;
    this.articleRemoteDataSource = articleRemoteDataSource;
    if (sInstance != null) {
      throw new AssertionError(
          "Another instance of "
              + ArticlesDataRepository.class.getName()
              + " class already exists, Can't create a new instance.");
    }
  }

  public static ArticlesDataRepository getInstance(ArticleDataSource articleLocalDataSource,
      ArticleDataSource articleRemoteDataSource) {
    if (sInstance == null) {
      synchronized (ArticlesDataRepository.class) {
        if (sInstance == null) {
          sInstance = new ArticlesDataRepository(articleLocalDataSource, articleRemoteDataSource);
        }
      }
    }
    return sInstance;
  }

  public List<Article> refreshCacheWithRemoteArticleData() throws Exception {
    List<Article> articles = articleRemoteDataSource.getArticlesFromServer();
    articleLocalDataSource.storeArticlesLocally(articles);

    return articles;
  }

  public List<Article> getCachedArticlesOrFromServer() throws Exception {
    List<Article> articles = articleLocalDataSource.getArticlesFromLocal();
    if (articles.size() > 0) {
      Timber.d("Fetched articles from local, total articles: %d", articles.size());
      return articles;
    }
    return refreshCacheWithRemoteArticleData();
  }

  public Article getArticleById(int articleId) {
    return articleLocalDataSource.getArticleById(articleId);
  }
}