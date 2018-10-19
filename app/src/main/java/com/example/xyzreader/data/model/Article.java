package com.example.xyzreader.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.squareup.moshi.Json;

@Entity(tableName = "articles")
public class Article {

  @ColumnInfo(name = "_id")
  @PrimaryKey
  @Json(name = "id")
  private int id;

  @ColumnInfo(name = "title")
  @Json(name = "title")
  private String title;

  @ColumnInfo(name = "author")
  @Json(name = "author")
  private String author;

  @ColumnInfo(name = "body")
  @Json(name = "body")
  private String body;

  @ColumnInfo(name = "photo")
  @Json(name = "photo")
  private String photo;

  @ColumnInfo(name = "thumb")
  @Json(name = "thumb")
  private String thumb;

  @ColumnInfo(name = "aspect_ratio")
  @Json(name = "aspect_ratio")
  private double aspectRatio;

  @ColumnInfo(name = "published_date")
  @Json(name = "published_date")
  private String publishedDate;

  @Override public String toString() {
    return "Article{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", author='" + author + '\'' +
        ", body='" + body.length() +" chars"+ '\'' +
        ", photo='" + photo + '\'' +
        ", thumb='" + thumb + '\'' +
        ", aspectRatio=" + aspectRatio +
        ", publishedDate='" + publishedDate + '\'' +
        '}';
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }

  public String getThumb() {
    return thumb;
  }

  public void setThumb(String thumb) {
    this.thumb = thumb;
  }

  public double getAspectRatio() {
    return aspectRatio;
  }

  public void setAspectRatio(double aspectRatio) {
    this.aspectRatio = aspectRatio;
  }

  public String getPublishedDate() {
    return publishedDate;
  }

  public void setPublishedDate(String publishedDate) {
    this.publishedDate = publishedDate;
  }
}