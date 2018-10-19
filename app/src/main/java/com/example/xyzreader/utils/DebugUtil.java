package com.example.xyzreader.utils;

import android.content.Intent;
import android.os.Bundle;

public class DebugUtil {

  public static String intentToString(Intent intent) {
    if (intent != null) {
      return bundleToString(intent.getExtras());
    }
    return null;
  }

  public static String bundleToString(Bundle bundle) {
    if (bundle != null) {
      StringBuilder stringBuilder = new StringBuilder();
      for (String key : bundle.keySet()) {
        Object value = bundle.get(key);
        if (value != null) {
          stringBuilder.append("[");
          stringBuilder.append(
              String.format("%s : %s (%s)", key, value.toString(), value.getClass().getName()));
          stringBuilder.append("] ");
        }
      }
      return stringBuilder.toString();
    }
    return null;
  }
}
