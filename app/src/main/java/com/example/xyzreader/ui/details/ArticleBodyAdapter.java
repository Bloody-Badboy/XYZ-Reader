package com.example.xyzreader.ui.details;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.xyzreader.databinding.ListItemArticleBodyBinding;
import java.util.List;

public class ArticleBodyAdapter
    extends RecyclerView.Adapter<ArticleBodyAdapter.ViewHolder> {

  private List<String> articleBody;
  private LayoutInflater inflater;

  void setArticleBody(List<String> body) {
    this.articleBody = body;
  }

  @NonNull @Override
  public ArticleBodyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
      int viewType) {
    if (inflater == null) {
      inflater = LayoutInflater.from(parent.getContext());
    }
    return new ViewHolder(ListItemArticleBodyBinding.inflate(inflater, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ArticleBodyAdapter.ViewHolder holder, int position) {
    ((TextView)holder.bodyBinding.getRoot()).setText(
        Html.fromHtml(articleBody.get(position).replaceAll("(\r\n|\n)", "")));
  }

  @Override public int getItemCount() {
    return articleBody == null ? 0 : articleBody.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    ListItemArticleBodyBinding bodyBinding;

    ViewHolder(@NonNull ListItemArticleBodyBinding bodyBinding) {
      super(bodyBinding.getRoot());
      this.bodyBinding = bodyBinding;
    }
  }
}