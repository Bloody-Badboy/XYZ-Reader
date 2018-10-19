package com.example.xyzreader.data.source.local.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.xyzreader.data.model.Article;

@Database(entities =
    {
        Article.class,
    },
    version = 1,
    exportSchema = false)
public abstract class ArticlesDatabase extends RoomDatabase {

  private static volatile ArticlesDatabase sInstance = null;

  public abstract ArticlesDao recipeDao();

  public static ArticlesDatabase getInstance(Context context) {
    if (sInstance == null) {
      synchronized (ArticlesDatabase.class) {
        if (sInstance == null) {
          sInstance = Room.databaseBuilder(context.getApplicationContext(), ArticlesDatabase.class,
              "article-database")
              .build();
        }
      }
    }
    return sInstance;
  }
}
