// Generated by view binder compiler. Do not edit!
package com.example.menuscript.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.menuscript.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class RecipeMaincontextBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView recipeListcategory;

  @NonNull
  public final TextView recipeListservings;

  @NonNull
  public final TextView recipeListtitle;

  private RecipeMaincontextBinding(@NonNull LinearLayout rootView,
      @NonNull TextView recipeListcategory, @NonNull TextView recipeListservings,
      @NonNull TextView recipeListtitle) {
    this.rootView = rootView;
    this.recipeListcategory = recipeListcategory;
    this.recipeListservings = recipeListservings;
    this.recipeListtitle = recipeListtitle;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static RecipeMaincontextBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static RecipeMaincontextBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.recipe_maincontext, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static RecipeMaincontextBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.recipe_listcategory;
      TextView recipeListcategory = ViewBindings.findChildViewById(rootView, id);
      if (recipeListcategory == null) {
        break missingId;
      }

      id = R.id.recipe_listservings;
      TextView recipeListservings = ViewBindings.findChildViewById(rootView, id);
      if (recipeListservings == null) {
        break missingId;
      }

      id = R.id.recipe_listtitle;
      TextView recipeListtitle = ViewBindings.findChildViewById(rootView, id);
      if (recipeListtitle == null) {
        break missingId;
      }

      return new RecipeMaincontextBinding((LinearLayout) rootView, recipeListcategory,
          recipeListservings, recipeListtitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}