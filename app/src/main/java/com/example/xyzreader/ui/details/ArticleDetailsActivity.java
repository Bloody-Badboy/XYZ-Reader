package com.example.xyzreader.ui.details;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.SharedElementCallback;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.example.xyzreader.R;
import com.example.xyzreader.databinding.ActivityArticleDetailsBinding;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import timber.log.Timber;

public class ArticleDetailsActivity extends AppCompatActivity {

  public static final String EXTRA_START_ITEM_POSITION = "start_item_position";
  public static final String EXTRA_CURRENT_ITEM_POSITION = "current_item_position";
  public static final String EXTRA_ARTICLE_IDS = "article_ids";

  private int startItemPosition;
  private int currentItemPosition;
  private List<Integer> articleIds;
  private ArticleDetailsFragment currentDetailsFragment;
  private boolean isReturning;

  private SharedElementCallback sharedElementCallback = new SharedElementCallback() {
    @Override
    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
      Timber.d("isReturning: %s", isReturning);
      if (isReturning) {
        View sharedElement = currentDetailsFragment.getPhotoImageView();
        Timber.d("sharedElement == null : %s", sharedElement == null);
        if (sharedElement != null) {
          if (startItemPosition != currentItemPosition) {
            Timber.d("transition name: %s", sharedElement.getTransitionName());
            names.clear();
            names.add(sharedElement.getTransitionName());

            sharedElements.clear();
            sharedElements.put(sharedElement.getTransitionName(), sharedElement);
          }
        } else {
          names.clear();
          sharedElements.clear();
        }
      }
    }
  };

  @Override

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityArticleDetailsBinding binding =
        DataBindingUtil.setContentView(this, R.layout.activity_article_details);
    postponeEnterTransition();
    setEnterSharedElementCallback(sharedElementCallback);

    Intent intent = getIntent();
    if (intent != null &&
        intent.hasExtra(EXTRA_START_ITEM_POSITION) &&
        intent.hasExtra(EXTRA_ARTICLE_IDS)) {
      articleIds = intent.getIntegerArrayListExtra(EXTRA_ARTICLE_IDS);
      startItemPosition = intent.getIntExtra(EXTRA_START_ITEM_POSITION, 0);
    } else {
      Toast.makeText(this, "No data!", Toast.LENGTH_SHORT).show();
    }

    if (savedInstanceState == null) {
      currentItemPosition = startItemPosition;
    } else {
      currentItemPosition = savedInstanceState.getInt(EXTRA_CURRENT_ITEM_POSITION);
    }

    ArticlePagerAdapter articlePagerAdapter = new ArticlePagerAdapter(getSupportFragmentManager());

    Timber.d("Item Position: %s, Article IDs: %s", currentItemPosition, articleIds);
    for (Integer articleId : articleIds) {
      articlePagerAdapter.addFragment(ArticleDetailsFragment.newInstance(articleId));
    }
    supportPostponeEnterTransition();

    binding.pager.setAdapter(articlePagerAdapter);
    binding.pager.setCurrentItem(currentItemPosition);
    binding.pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
      @Override public void onPageSelected(int position) {
        Timber.d("onPageSelected() -> position: %s", position);
        currentItemPosition = position;
      }
    });
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(EXTRA_CURRENT_ITEM_POSITION, currentItemPosition);
  }

  @Override public void finishAfterTransition() {
    Timber.d("finishAfterTransition() -> startItemPosition: %s, currentItemPosition: %s",
        startItemPosition, currentItemPosition);
    isReturning = true;
    Intent data = new Intent();
    data.putExtra(EXTRA_START_ITEM_POSITION, startItemPosition);
    data.putExtra(EXTRA_CURRENT_ITEM_POSITION, currentItemPosition);
    setResult(RESULT_OK, data);
    super.finishAfterTransition();
  }

  public final class ArticlePagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> fragments = new ArrayList<>();

    ArticlePagerAdapter(FragmentManager fm) {
      super(fm);
    }

    void addFragment(Fragment fragment) {
      fragments.add(fragment);
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
      super.setPrimaryItem(container, position, object);
      Timber.d("current details fragment position: %s", position);
      currentDetailsFragment = (ArticleDetailsFragment) object;
    }

    @Override public Fragment getItem(int position) {
      return fragments.get(position);
    }

    @Override public int getCount() {
      return fragments.size();
    }
  }
}
