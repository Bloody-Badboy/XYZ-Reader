package com.example.xyzreader.domain;

import androidx.lifecycle.MutableLiveData;
import com.example.xyzreader.result.Result;
import com.example.xyzreader.utils.scheduler.Scheduler;

public abstract class UseCase<T> {
  private final Scheduler scheduler;

  public UseCase(Scheduler scheduler) {
    this.scheduler = scheduler;
  }

  public void execute(
      final MutableLiveData<Result<T>> resultMutableLiveData) {
    resultMutableLiveData.postValue(Result.<T>Loading());
    scheduler.execute(new Runnable() {
      @Override public void run() {
        try {
          resultMutableLiveData.postValue(Result.Success(runOnBackground()));
        } catch (Exception e) {
          e.printStackTrace();
          resultMutableLiveData.postValue(Result.<T>Error(e));
        }
      }
    });
  }

  public MutableLiveData<Result<T>> execute() {
    final MutableLiveData<Result<T>> resultMutableLiveData = new MutableLiveData<>();
    execute(resultMutableLiveData);
    return resultMutableLiveData;
  }

  protected abstract T runOnBackground() throws Exception;
}
