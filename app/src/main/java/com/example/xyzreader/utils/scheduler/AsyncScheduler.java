package com.example.xyzreader.utils.scheduler;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class AsyncScheduler implements Scheduler {
  private static volatile AsyncScheduler sInstance = null;

  private static final int NUMBER_OF_THREADS = 4;

  private final ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

  private AsyncScheduler() {
    if (sInstance != null) {
      throw new AssertionError(
          "Another instance of "
              + AsyncScheduler.class.getName()
              + " class already exists, Can't create a new instance.");
    }
  }

  public static AsyncScheduler getInstance() {
    if (sInstance == null) {
      synchronized (AsyncScheduler.class) {
        if (sInstance == null) {
          sInstance = new AsyncScheduler();
        }
      }
    }
    return sInstance;
  }

  @Override public void execute(Runnable task) {
    executorService.execute(task);
  }

  @Override public void postToMainThread(Runnable task) {
    if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
      task.run();
    } else {
      Handler mainThreadHandler = new Handler(Looper.getMainLooper());
      mainThreadHandler.post(task);
    }
  }
}
