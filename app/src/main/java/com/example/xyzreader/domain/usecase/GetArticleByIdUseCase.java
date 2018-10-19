package com.example.xyzreader.domain.usecase;

import com.example.xyzreader.domain.UseCase;
import com.example.xyzreader.inject.Injection;
import com.example.xyzreader.data.model.Article;
import com.example.xyzreader.utils.scheduler.Scheduler;

public final class GetArticleByIdUseCase extends UseCase<Article> {

  private int articleId;

  public GetArticleByIdUseCase(Scheduler scheduler) {
    super(scheduler);
  }

  public GetArticleByIdUseCase setArticleId(int articleId) {
    this.articleId = articleId;
    return this;
  }

  @Override protected Article runOnBackground() {
    return Injection.provideArticlesDataRepository().getArticleById(articleId);
  }
}
