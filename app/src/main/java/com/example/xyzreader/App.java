package com.example.xyzreader;

import android.app.Application;
import com.facebook.stetho.Stetho;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import timber.log.Timber;

public class App extends Application {
  public static App getInstance() {
    return sINSTANCE;
  }

  public static App sINSTANCE;

  @Override public void onCreate() {
    super.onCreate();
    sINSTANCE = this;

    if(BuildConfig.DEBUG) {

      Stetho.initializeWithDefaults(this);

      Timber.plant(new Timber.DebugTree() {
        @Override
        protected @Nullable String createStackElementTag(@NotNull StackTraceElement element) {
          return "XYZ_" + super.createStackElementTag(element) + ":" + element.getLineNumber();
        }
      });
    }
  }
}
