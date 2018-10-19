package com.example.xyzreader.utils.scheduler;

public interface Scheduler {
  void execute(Runnable task);

  void postToMainThread(Runnable task);
}
