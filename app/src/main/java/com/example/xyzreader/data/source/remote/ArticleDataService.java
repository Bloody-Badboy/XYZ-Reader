package com.example.xyzreader.data.source.remote;

import com.example.xyzreader.data.model.Article;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.util.List;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.ByteString;

public class ArticleDataService {

  private static final String SERVICE_END_POINT = "https://go.udacity.com/xyz-reader-json";

  private static final ByteString UTF8_BOM = ByteString.decodeHex("EFBBBF");

  private static volatile ArticleDataService sInstance = null;
  private OkHttpClient okHttpClient;

  private ArticleDataService(OkHttpClient okHttpClient) {
    this.okHttpClient = okHttpClient;
    if (sInstance != null) {
      throw new AssertionError(
          "Another instance of "
              + ArticleDataService.class.getName()
              + " class already exists, Can't create a new instance.");
    }
  }

  public static ArticleDataService getInstance(OkHttpClient okHttpClient) {
    if (sInstance == null) {
      synchronized (ArticleDataService.class) {
        if (sInstance == null) {
          sInstance = new ArticleDataService(okHttpClient);
        }
      }
    }
    return sInstance;
  }

  public List<Article> getArticles() throws IOException {

    Request request = new Request.Builder()
        .url(SERVICE_END_POINT)
        .build();

    final Call call = okHttpClient.newCall(request);

    Response response = call.execute();

    ResponseBody responseBody = response.body();

    BufferedSource source;
    if (responseBody != null) {
      source = responseBody.source();

      if (source.rangeEquals(0, UTF8_BOM)) {
        source.skip(UTF8_BOM.size());
      }
      JsonReader reader = JsonReader.of(source);

      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<List<Article>> recipesJsonAdapter =
          moshi.adapter(Types.newParameterizedType(List.class, Article.class));

      List<Article> result = recipesJsonAdapter.fromJson(reader);
      if (reader.peek() != JsonReader.Token.END_DOCUMENT) {
        throw new JsonDataException("JSON document was not fully consumed.");
      }

      return result;
    } else {
      throw new NullPointerException("responseBody == null");
    }
  }
}
