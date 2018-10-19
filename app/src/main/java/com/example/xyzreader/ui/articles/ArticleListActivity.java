package com.example.xyzreader.ui.articles;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.SharedElementCallback;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.xyzreader.R;
import com.example.xyzreader.ui.ViewModelFactory;
import com.example.xyzreader.databinding.ActivityArticleListBinding;
import com.example.xyzreader.inject.Injection;
import com.example.xyzreader.utils.DebugUtil;
import com.example.xyzreader.utils.NetworkUtil;
import com.example.xyzreader.utils.ViewUtils;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;
import java.util.Map;
import timber.log.Timber;

import static com.example.xyzreader.ui.details.ArticleDetailsActivity.EXTRA_CURRENT_ITEM_POSITION;
import static com.example.xyzreader.ui.details.ArticleDetailsActivity.EXTRA_START_ITEM_POSITION;

public class ArticleListActivity extends AppCompatActivity {

  private ActivityArticleListBinding binding;
  private ArticleListViewModel viewModel;
  private ArticleListAdapter articleListAdapter;
  private Bundle reenterState;

  private SharedElementCallback sharedElementCallback = new SharedElementCallback() {
    @Override
    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
      Timber.d("onMapSharedElements() -> reenterState == null : %s ", reenterState == null);
      if (reenterState != null) {

        int startingPosition = reenterState.getInt(EXTRA_START_ITEM_POSITION);
        int currentPosition = reenterState.getInt(EXTRA_CURRENT_ITEM_POSITION);

        Timber.d("startingPosition: " + startingPosition + ", currentPosition: " + currentPosition);

        if (startingPosition != currentPosition) {
          String newTransitionName = getString(R.string.transition_thumb, currentPosition);
          View newSharedElement = binding.recyclerView.findViewWithTag(newTransitionName);
          if (newSharedElement != null) {
            Timber.d("View found with tag: %s", newTransitionName);
            Timber.d(newSharedElement.toString());

            names.clear();
            names.add(newTransitionName);
            sharedElements.clear();
            sharedElements.put(newTransitionName, newSharedElement);
          } else {
            Timber.d("No view found with tag: %s", newTransitionName);
          }
        }
        reenterState = null;
      } else {
        View navigationBar = findViewById(android.R.id.navigationBarBackground);
        View statusBar = findViewById(android.R.id.statusBarBackground);
        if (navigationBar != null) {
          names.add(navigationBar.getTransitionName());
          sharedElements.put(navigationBar.getTransitionName(), navigationBar);
        }
        if (statusBar != null) {
          names.add(statusBar.getTransitionName());
          sharedElements.put(statusBar.getTransitionName(), statusBar);
        }
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding =
        DataBindingUtil.setContentView(this, R.layout.activity_article_list);
    setExitSharedElementCallback(sharedElementCallback);

    viewModel = obtainViewModel(this);

    binding.setLifecycleOwner(this);
    binding.setViewModel(viewModel);

    setSupportActionBar(binding.toolbar);

    articleListAdapter = new ArticleListAdapter(this);
    articleListAdapter.setArticles(null);

    final GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

    binding.recyclerView.setLayoutManager(layoutManager);
    binding.recyclerView.setHasFixedSize(true);
    binding.recyclerView.setAdapter(articleListAdapter);
    binding.recyclerView.addOnScrollListener(
        new RecyclerView.OnScrollListener() {
          @Override public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
              if (binding.appBarLayout.getElevation() == 0f) return;
              animateToolbarElevation(true);
            } else {
              if (binding.appBarLayout.getElevation() > 0f) return;
              animateToolbarElevation(false);
            }
          }
        });

    viewModel.getObservableArticlesList().observe(this,
        listResult -> {
          if (listResult.loading()) {
            articleListAdapter.setArticles(null);
            articleListAdapter.notifyDataSetChanged();
          } else if (listResult.succeeded()) {
            binding.recyclerView.scheduleLayoutAnimation();
            articleListAdapter.setArticles(listResult.data);
            articleListAdapter.notifyDataSetChanged();
          } else {
            if (!NetworkUtil.isOnline()) {
              showErrorSnackbar(
                  getString(R.string.error_msg_no_internet));
            } else {
              showErrorSnackbar(getString(R.string.error_msg_enexpected));
            }
          }
        });
  }

  private void showErrorSnackbar(String message) {
    Snackbar snackbar =
        Snackbar.make(binding.coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE);
    snackbar.setAction(getString(R.string.retry), v -> {
      if (NetworkUtil.isOnline()) {
        viewModel.onSwipeRefresh();
      } else {
        showErrorSnackbar(message);
      }
    });

    snackbar.setActionTextColor(getResources().getColor(android.R.color.white));
    View sbView = snackbar.getView();
    TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
    textView.setTextColor(ContextCompat.getColor(this, android.R.color.white));
    snackbar.show();
  }

  private void animateToolbarElevation(boolean animateOut) {
    float elevation = getResources().getDimension(R.dimen.toolbar_elevation);
    final ValueAnimator animator =
        animateOut ? ValueAnimator.ofFloat(elevation, 0f) : ValueAnimator.ofFloat(0f, elevation);
    animator.setDuration(250L);
    animator.addUpdateListener(
        animation -> binding.appBarLayout.setElevation((float) animator.getAnimatedValue()));
    animator.start();
  }

  public static ArticleListViewModel obtainViewModel(FragmentActivity activity) {
    ViewModelFactory factory = ViewModelFactory.getInstance(Injection.provideScheduler());
    return ViewModelProviders.of(activity, factory).get(ArticleListViewModel.class);
  }

  @Override public void onActivityReenter(int resultCode, Intent data) {
    Timber.d("onActivityReenter() -> resultCode: %s, Extras: %s", resultCode,
        DebugUtil.intentToString(data));

    super.onActivityReenter(resultCode, data);
    reenterState = new Bundle(data.getExtras());
    final int startItemPosition = reenterState.getInt(EXTRA_START_ITEM_POSITION);
    final int selectedItemPosition = reenterState.getInt(EXTRA_CURRENT_ITEM_POSITION);
    if (startItemPosition != selectedItemPosition) {
      binding.recyclerView.scrollToPosition(selectedItemPosition);
    }
    postponeEnterTransition();
    ViewUtils.doOnPreDraw(binding.recyclerView,
        ArticleListActivity.this::startPostponedEnterTransition);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.refresh) {
      viewModel.onSwipeRefresh();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
