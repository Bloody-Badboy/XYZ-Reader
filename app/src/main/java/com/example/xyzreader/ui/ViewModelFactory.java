package com.example.xyzreader.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.xyzreader.domain.usecase.GetArticleByIdUseCase;
import com.example.xyzreader.domain.usecase.LoadArticlesUseCase;
import com.example.xyzreader.domain.usecase.RefreshStoredArticlesUseCase;
import com.example.xyzreader.ui.articles.ArticleListViewModel;
import com.example.xyzreader.ui.details.ArticleDetailsViewModel;
import com.example.xyzreader.utils.scheduler.Scheduler;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

  private static volatile ViewModelFactory INSTANCE;

  private final Scheduler scheduler;

  private ViewModelFactory(Scheduler scheduler) {
    this.scheduler = scheduler;
  }

  public static ViewModelFactory getInstance(Scheduler scheduler) {

    if (INSTANCE == null) {
      synchronized (ViewModelFactory.class) {
        if (INSTANCE == null) {
          INSTANCE = new ViewModelFactory(scheduler);
        }
      }
    }
    return INSTANCE;
  }

  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    if (modelClass.isAssignableFrom(ArticleListViewModel.class)) {
      //noinspection unchecked
      return (T) new ArticleListViewModel(
          new LoadArticlesUseCase(scheduler),
          new RefreshStoredArticlesUseCase(scheduler)
      );
    } else if (modelClass.isAssignableFrom(ArticleDetailsViewModel.class)) {
      //noinspection unchecked
      return (T) new ArticleDetailsViewModel(
          new GetArticleByIdUseCase(scheduler)
      );
    }
    throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
  }
}
