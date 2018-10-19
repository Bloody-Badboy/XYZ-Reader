package com.example.xyzreader.ui.details;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.xyzreader.R;
import com.example.xyzreader.ui.ViewModelFactory;
import com.example.xyzreader.data.model.Article;
import com.example.xyzreader.databinding.FragmentArticleDetailsBinding;
import com.example.xyzreader.inject.Injection;
import com.example.xyzreader.utils.ViewUtils;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import timber.log.Timber;

public class ArticleDetailsFragment extends Fragment {

  private static final String EXTRA_ARTICLE_ID = "article_id";

  private int articleId;
  private FragmentArticleDetailsBinding binding;
  private Article article;

  public ArticleDetailsFragment() {
    // Required empty public constructor
  }

  static ArticleDetailsFragment newInstance(int articleId) {
    ArticleDetailsFragment fragment = new ArticleDetailsFragment();
    Bundle args = new Bundle();
    args.putInt(EXTRA_ARTICLE_ID, articleId);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      articleId = getArguments().getInt(EXTRA_ARTICLE_ID);
    }

    Timber.d("articleId: %s", articleId);
  }

  @Override
  public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    ArticleDetailsViewModel viewModel = obtainViewModel(this);

    binding = FragmentArticleDetailsBinding.inflate(inflater, container, false);

    Timber.d("transition name: %s", getString(R.string.transition_thumb, articleId));

    binding.articleThumb.setTransitionName(getString(R.string.transition_thumb, articleId));

    ArticleBodyAdapter articleBodyAdapter = new ArticleBodyAdapter();

    binding.recyclerView.setHasFixedSize(true);
    binding.recyclerView.setLayoutManager(
        new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
    binding.recyclerView.setAdapter(articleBodyAdapter);
    binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dy > 0 && binding.shareFab.getVisibility() == View.VISIBLE) {
          binding.shareFab.hide();
        } else if (dy < 0 && binding.shareFab.getVisibility() != View.VISIBLE) {
          binding.shareFab.show();
        }
      }
    });

    binding.shareFab.setOnClickListener(
        v -> {
          String text = "";
          if (article != null) {
            text = String.format("%s by %s", article.getTitle(), article.getAuthor());
          }
          startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivityCast())
              .setType("text/plain")
              .setChooserTitle("Share via...")
              .setText(text)
              .getIntent(), getString(R.string.action_share)));
        });

    CollapsingToolbarLayout.LayoutParams layoutParams =
        (CollapsingToolbarLayout.LayoutParams) binding.toolbar.getLayoutParams();
    layoutParams.setMargins(0, getStatusBarHeight(getActivityCast()), 0, 0);
    binding.toolbar.setLayoutParams(layoutParams);
    binding.toolbar.setNavigationOnClickListener(v -> getActivityCast().onBackPressed());

    getActivityCast().setSupportActionBar(binding.toolbar);

    ActionBar actionBar = getActivityCast().getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    viewModel.getObservableArticle()
        .observe(this, articleResult -> {
          if (articleResult.succeeded()) {
            article = articleResult.data;

            binding.setArticle(article);

            ViewUtils.doOnPreDraw(binding.articleThumb,
                () -> getActivityCast().startPostponedEnterTransition());

            String parts[] = article.getBody().split("\r\n\r\n");
            List<String> articleBody = Arrays.asList(parts);

            articleBodyAdapter.setArticleBody(articleBody);
            articleBodyAdapter.notifyDataSetChanged();
          }
        });

    viewModel.getArticleById(articleId);

    return binding.getRoot();
  }

  private static ArticleDetailsViewModel obtainViewModel(Fragment fragment) {
    ViewModelFactory factory = ViewModelFactory.getInstance(Injection.provideScheduler());
    return ViewModelProviders.of(fragment, factory).get(ArticleDetailsViewModel.class);
  }

  View getPhotoImageView() {
    if (isViewInBounds(getActivityCast().getWindow().getDecorView(), binding.articleThumb)) {
      return binding.articleThumb;
    }
    return null;
  }

  private static boolean isViewInBounds(@NonNull View container, @NonNull View view) {
    Rect containerBounds = new Rect();
    container.getHitRect(containerBounds);
    return view.getLocalVisibleRect(containerBounds);
  }

  private ArticleDetailsActivity getActivityCast() {
    return ((ArticleDetailsActivity) getActivity());
  }

  private int getStatusBarHeight(@NonNull Context context) {
    int result = 0;
    int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
      result = context.getResources().getDimensionPixelSize(resourceId);
    }
    return result;
  }
}
