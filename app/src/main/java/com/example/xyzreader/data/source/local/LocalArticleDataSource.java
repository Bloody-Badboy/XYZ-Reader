package com.example.xyzreader.data.source.local;

import com.example.xyzreader.data.source.ArticleDataSource;
import com.example.xyzreader.data.source.local.db.ArticlesDatabase;
import com.example.xyzreader.data.model.Article;
import java.util.List;
import timber.log.Timber;

public class LocalArticleDataSource implements ArticleDataSource {
  private static volatile LocalArticleDataSource sInstance = null;
  private final ArticlesDatabase articlesDatabase;

  private LocalArticleDataSource(ArticlesDatabase articlesDatabase) {
    this.articlesDatabase = articlesDatabase;
    if (sInstance != null) {
      throw new AssertionError(
          "Another instance of "
              + LocalArticleDataSource.class.getName()
              + " class already exists, Can't create a new instance.");
    }
  }

  public static LocalArticleDataSource getInstance(ArticlesDatabase articlesDatabase) {
    if (sInstance == null) {
      synchronized (LocalArticleDataSource.class) {
        if (sInstance == null) {
          sInstance = new LocalArticleDataSource(articlesDatabase);
        }
      }
    }
    return sInstance;
  }

  @Override public List<Article> getArticlesFromLocal() {
    return articlesDatabase.recipeDao().getAllArticles();
  }

  @Override public List<Article> getArticlesFromServer() {
    throw new UnsupportedOperationException("Method not implemented.");
  }

  @Override public void storeArticlesLocally(List<Article> articles) {
    int deletedRows = articlesDatabase.recipeDao().deleteAll();
    List<Long> rowsInserted = articlesDatabase.recipeDao().insertAll(articles);

    Timber.d("Delete %d rows, inserted %d rows.", deletedRows, rowsInserted.size());

    rowsInserted.size();
  }

  @Override public Article getArticleById(int articleId) {
    return articlesDatabase.recipeDao().getArticleById(articleId);
  }
}