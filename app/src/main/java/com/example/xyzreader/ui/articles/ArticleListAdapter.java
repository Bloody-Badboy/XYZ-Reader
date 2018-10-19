package com.example.xyzreader.ui.articles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.xyzreader.R;
import com.example.xyzreader.data.model.Article;
import com.example.xyzreader.databinding.ListItemArticleBinding;
import com.example.xyzreader.databinding.ListItemArticlePlaceholderBinding;
import com.example.xyzreader.ui.details.ArticleDetailsActivity;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

import static com.example.xyzreader.ui.details.ArticleDetailsActivity.EXTRA_ARTICLE_IDS;
import static com.example.xyzreader.ui.details.ArticleDetailsActivity.EXTRA_START_ITEM_POSITION;

public class ArticleListAdapter
    extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static final int VIEW_TYPE_ARTICLE = 0;
  private static final int VIEW_TYPE_LOADING = 1;

  private static final int LOADING_PLACEHOLDER_SIZE = 8;

  private Activity activity;
  private List<Article> articles;
  private LayoutInflater inflater;

  public void setArticles(List<Article> articles) {
    this.articles = articles;
  }

  ArticleListAdapter(Activity activity) {
    this.activity = activity;
  }

  @NonNull @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
      int viewType) {
    if (inflater == null) {
      inflater = LayoutInflater.from(parent.getContext());
    }
    if (viewType == VIEW_TYPE_ARTICLE) {
      ListItemArticleBinding articleBinding =
          ListItemArticleBinding.inflate(inflater, parent, false);
      return new ArticleViewHolder(articleBinding);
    } else {
      ListItemArticlePlaceholderBinding articlePlaceholderBinding =
          ListItemArticlePlaceholderBinding.inflate(inflater, parent, false);
      return new LoadingViewHolder(articlePlaceholderBinding);
    }
  }

  @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof ArticleViewHolder) {
      ArticleViewHolder
          articleViewHolder = (ArticleViewHolder) holder;
      Article article = articles.get(position);

      String transitionName = activity.getString(R.string.transition_thumb, article.getId());

      Timber.d("transition name: %s", transitionName);

      articleViewHolder.articleBinding.setArticle(articles.get(position));
      articleViewHolder.articleBinding.articleTitle.setSelected(true);
      articleViewHolder.articleBinding.articleThumb.setTransitionName(transitionName);
      articleViewHolder.articleBinding.articleThumb.setTag(transitionName);
    } else {
      ((LoadingViewHolder) holder).articlePlaceholderBinding.shimmer.startShimmer();
    }
  }

  @Override public int getItemViewType(int position) {
    if (articles == null) {
      return VIEW_TYPE_LOADING;
    }
    return VIEW_TYPE_ARTICLE;
  }

  @Override public int getItemCount() {
    return articles == null ? LOADING_PLACEHOLDER_SIZE : articles.size();
  }

  class ArticleViewHolder extends RecyclerView.ViewHolder {
    ListItemArticleBinding articleBinding;

    ArticleViewHolder(@NonNull ListItemArticleBinding articleBinding) {
      super(articleBinding.getRoot());
      this.articleBinding = articleBinding;
      this.articleBinding.getRoot().setOnClickListener(v -> launchDetailsActivity());
    }

    void launchDetailsActivity() {
      ArrayList<Integer> articleIds = new ArrayList<>();
      for (Article article : articles) {
        articleIds.add(article.getId());
      }

      Intent intent = new Intent(activity, ArticleDetailsActivity.class);
      intent.putIntegerArrayListExtra(EXTRA_ARTICLE_IDS, articleIds);
      intent.putExtra(EXTRA_START_ITEM_POSITION, getAdapterPosition());

      String transitionName = articleBinding.articleThumb.getTransitionName();
      Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
          activity,
          articleBinding.articleThumb,
          transitionName
      ).toBundle();
      activity.startActivity(intent, bundle);
    }
  }

  class LoadingViewHolder extends RecyclerView.ViewHolder {

    ListItemArticlePlaceholderBinding articlePlaceholderBinding;

    LoadingViewHolder(@NonNull ListItemArticlePlaceholderBinding articleBinding) {
      super(articleBinding.getRoot());
      this.articlePlaceholderBinding = articleBinding;
    }
  }
}
