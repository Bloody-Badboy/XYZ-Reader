package com.example.xyzreader.data.source;

import com.example.xyzreader.data.model.Article;
import java.util.List;

public interface ArticleDataSource {
  List<Article> getArticlesFromLocal();

  List<Article> getArticlesFromServer() throws Exception;

  void storeArticlesLocally(List<Article> articles);

  Article getArticleById(int articleId);

}
