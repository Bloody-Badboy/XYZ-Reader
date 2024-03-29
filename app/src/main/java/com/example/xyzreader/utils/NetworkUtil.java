package com.example.xyzreader.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.example.xyzreader.inject.Injection;

public final class NetworkUtil {

  private NetworkUtil() {
    throw new AssertionError("Can't create instance of a utility class.");
  }

  public static boolean isOnline() {

    ConnectivityManager connectivityManager =
        (ConnectivityManager) Injection.provideApplicationContext()
            .getSystemService(Context.CONNECTIVITY_SERVICE);

    if (connectivityManager != null) {
      NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
      return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    return false;
  }
}
