package com.example.xyzreader.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.ColorUtils;
import androidx.databinding.BindingAdapter;
import androidx.palette.graphics.Palette;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.xyzreader.R;
import timber.log.Timber;

public final class BindingAdapters {
  @BindingAdapter(value = { "imageUrl", "contentBackground" }, requireAll = false)
  public static void loadImage(ImageView imageView, String imageUrl, View infoViewBg) {

    Drawable placeholder =
        AppCompatResources.getDrawable(imageView.getContext(), R.drawable.generic_placeholder);

    if (imageUrl == null) {
      Timber.d("imageUrl == null");
      Glide.with(imageView)
          .load(placeholder)
          .into(imageView);
    } else if (infoViewBg == null) {
      Glide.with(imageView)
          .load(imageUrl)
          .transition(new DrawableTransitionOptions().crossFade(500))
          .apply(new RequestOptions().placeholder(placeholder)
              .diskCacheStrategy(DiskCacheStrategy.ALL))
          .into(imageView);
    } else {
      //noinspection deprecation
      Glide.with(imageView).asBitmap().load(imageUrl)
          .transition(new BitmapTransitionOptions().crossFade(500))
          .apply(new RequestOptions().placeholder(placeholder)
              .diskCacheStrategy(DiskCacheStrategy.ALL))
          .into(new SimpleTarget<Bitmap>() {
            @Override public void onResourceReady(@NonNull Bitmap resource,
                @Nullable Transition<? super Bitmap> transition) {
              imageView.setImageBitmap(resource);
              Palette.from(resource)
                  .generate(p -> {
                    if (p != null) {
                      infoViewBg.setBackground(
                          createGradientDrawable(p));
                    }
                  });
            }
          });
    }
  }

  private static GradientDrawable createGradientDrawable(Palette palette) {
    int darkVibrantColor =
        ColorUtils.setAlphaComponent(palette.getDarkVibrantColor(Color.BLACK),
            (int) (0xFF * 0.87f)); // 87% alpha
    int darkMutedColor =
        ColorUtils.setAlphaComponent(palette.getDarkMutedColor(Color.BLACK),
            (int) (0xFF * 0.87f)); // 87% alpha
    return new GradientDrawable(GradientDrawable.Orientation.BR_TL,
        new int[] { darkMutedColor, darkVibrantColor });
  }
}
