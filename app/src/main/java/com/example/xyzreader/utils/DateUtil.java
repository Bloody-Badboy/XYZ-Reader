package com.example.xyzreader.utils;

import android.text.TextUtils;
import android.text.format.DateUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public final class DateUtil {
  // Most time functions can only handle 1902 - 2037
  private static GregorianCalendar START_OF_EPOCH = new GregorianCalendar(2, 1, 1);

  private static final SimpleDateFormat sourceDateFormat =
      new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss", Locale.ENGLISH);
  private static final SimpleDateFormat targetDateFormat =
      new SimpleDateFormat("MMMM dd, YYYY", Locale.ENGLISH);

  public static String getHumanReadableDate(String source) {
    if (!TextUtils.isEmpty(source)) {
      try {
        Date date = sourceDateFormat.parse(source);
        if (!(date.before(START_OF_EPOCH.getTime()))) {
          return DateUtils.getRelativeTimeSpanString(
              date.getTime(),
              System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
              DateUtils.FORMAT_ABBREV_ALL).toString();
        } else {
          return targetDateFormat.format(date);
        }
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
    return "N/A";
  }
}
