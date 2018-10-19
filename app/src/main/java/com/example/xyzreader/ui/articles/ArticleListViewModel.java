package com.example.xyzreader.ui.articles;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import com.example.xyzreader.domain.usecase.LoadArticlesUseCase;
import com.example.xyzreader.domain.usecase.RefreshStoredArticlesUseCase;
import com.example.xyzreader.data.model.Article;
import com.example.xyzreader.result.Result;
import java.util.List;

public class ArticleListViewModel extends ViewModel {
  private final LoadArticlesUseCase loadArticlesUseCase;
  private final RefreshStoredArticlesUseCase refreshStoredArticlesUseCase;

  private final MutableLiveData<Result<List<Article>>> articlesList = new MutableLiveData<>();

  public final LiveData<Boolean> swipeRefreshing;

  public ArticleListViewModel(
      LoadArticlesUseCase loadArticlesUseCase,
      RefreshStoredArticlesUseCase refreshStoredArticlesUseCase) {
    this.loadArticlesUseCase = loadArticlesUseCase;
    this.refreshStoredArticlesUseCase = refreshStoredArticlesUseCase;

    // Whenever refresh finishes (listLoading == false) , stop the indicator regardless of result
    swipeRefreshing = Transformations.map(articlesList, Result::loading);

    loadArticlesUseCase.execute(articlesList);
  }

  MutableLiveData<Result<List<Article>>> getObservableArticlesList() {
    return articlesList;
  }

  public void onSwipeRefresh() {
    refreshStoredArticlesUseCase.execute(articlesList);
  }
}
