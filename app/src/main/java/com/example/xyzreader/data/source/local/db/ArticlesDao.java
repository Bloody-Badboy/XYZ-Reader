package com.example.xyzreader.data.source.local.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.xyzreader.data.model.Article;
import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ArticlesDao {
  @Query("SELECT * FROM articles")
  List<Article> getAllArticles();

  @Query("SELECT * FROM articles WHERE _id = :articleId")
  Article getArticleById(int articleId);

  @Insert(onConflict = REPLACE)
  List<Long> insertAll(List<Article> articles);

  @Query("DELETE FROM articles")
  int deleteAll();
}
