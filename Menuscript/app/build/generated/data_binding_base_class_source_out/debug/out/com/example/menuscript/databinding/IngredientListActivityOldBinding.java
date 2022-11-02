// Generated by view binder compiler. Do not edit!
package com.example.menuscript.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.menuscript.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class IngredientListActivityOldBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView amountHeader;

  @NonNull
  public final TextView categoryHeader;

  @NonNull
  public final TextView descriptionHeader;

  @NonNull
  public final TextView header;

  @NonNull
  public final LinearLayout headerBar;

  @NonNull
  public final ListView itemList;

  @NonNull
  public final FrameLayout listFrame;

  @NonNull
  public final Spinner sortButton;

  private IngredientListActivityOldBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextView amountHeader, @NonNull TextView categoryHeader,
      @NonNull TextView descriptionHeader, @NonNull TextView header,
      @NonNull LinearLayout headerBar, @NonNull ListView itemList, @NonNull FrameLayout listFrame,
      @NonNull Spinner sortButton) {
    this.rootView = rootView;
    this.amountHeader = amountHeader;
    this.categoryHeader = categoryHeader;
    this.descriptionHeader = descriptionHeader;
    this.header = header;
    this.headerBar = headerBar;
    this.itemList = itemList;
    this.listFrame = listFrame;
    this.sortButton = sortButton;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static IngredientListActivityOldBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static IngredientListActivityOldBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.ingredient_list_activity_old, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static IngredientListActivityOldBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.amount_header;
      TextView amountHeader = ViewBindings.findChildViewById(rootView, id);
      if (amountHeader == null) {
        break missingId;
      }

      id = R.id.category_header;
      TextView categoryHeader = ViewBindings.findChildViewById(rootView, id);
      if (categoryHeader == null) {
        break missingId;
      }

      id = R.id.description_header;
      TextView descriptionHeader = ViewBindings.findChildViewById(rootView, id);
      if (descriptionHeader == null) {
        break missingId;
      }

      id = R.id.header;
      TextView header = ViewBindings.findChildViewById(rootView, id);
      if (header == null) {
        break missingId;
      }

      id = R.id.header_bar;
      LinearLayout headerBar = ViewBindings.findChildViewById(rootView, id);
      if (headerBar == null) {
        break missingId;
      }

      id = R.id.item_list;
      ListView itemList = ViewBindings.findChildViewById(rootView, id);
      if (itemList == null) {
        break missingId;
      }

      id = R.id.list_frame;
      FrameLayout listFrame = ViewBindings.findChildViewById(rootView, id);
      if (listFrame == null) {
        break missingId;
      }

      id = R.id.sort_button;
      Spinner sortButton = ViewBindings.findChildViewById(rootView, id);
      if (sortButton == null) {
        break missingId;
      }

      return new IngredientListActivityOldBinding((ConstraintLayout) rootView, amountHeader,
          categoryHeader, descriptionHeader, header, headerBar, itemList, listFrame, sortButton);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
