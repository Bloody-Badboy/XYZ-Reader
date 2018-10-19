package com.example.xyzreader.domain.usecase;

import com.example.xyzreader.domain.UseCase;
import com.example.xyzreader.inject.Injection;
import com.example.xyzreader.data.model.Article;
import com.example.xyzreader.utils.scheduler.Scheduler;
import java.util.List;

public final class LoadArticlesUseCase extends UseCase<List<Article>> {

  public LoadArticlesUseCase(Scheduler scheduler) {
    super(scheduler);
  }

  @Override protected List<Article> runOnBackground() throws Exception {
    return Injection.provideArticlesDataRepository().getCachedArticlesOrFromServer();
  }
}
