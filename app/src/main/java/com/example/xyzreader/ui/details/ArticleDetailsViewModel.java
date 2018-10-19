package com.example.xyzreader.ui.details;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.xyzreader.data.model.Article;
import com.example.xyzreader.domain.usecase.GetArticleByIdUseCase;
import com.example.xyzreader.result.Result;
import timber.log.Timber;

public class ArticleDetailsViewModel extends ViewModel {

  //public LiveData<Boolean> articleLoading;

  private final GetArticleByIdUseCase getArticleByIdUseCase;
  private final MutableLiveData<Result<Article>> article = new MutableLiveData<>();

  public ArticleDetailsViewModel(GetArticleByIdUseCase getArticleByIdUseCase) {
    this.getArticleByIdUseCase = getArticleByIdUseCase;
    //articleLoading = Transformations.map(article, Result::loading);
  }

  void getArticleById(int articleId) {
    getArticleByIdUseCase.setArticleId(articleId);
    getArticleByIdUseCase.execute(article);
  }

  MutableLiveData<Result<Article>> getObservableArticle() {
    return article;
  }
}
