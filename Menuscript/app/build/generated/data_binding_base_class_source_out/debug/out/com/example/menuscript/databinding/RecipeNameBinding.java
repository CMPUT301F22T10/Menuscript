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

public final class RecipeNameBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView recipeName;

  private RecipeNameBinding(@NonNull LinearLayout rootView, @NonNull TextView recipeName) {
    this.rootView = rootView;
    this.recipeName = recipeName;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static RecipeNameBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static RecipeNameBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.recipe_name, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static RecipeNameBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.recipeName;
      TextView recipeName = ViewBindings.findChildViewById(rootView, id);
      if (recipeName == null) {
        break missingId;
      }

      return new RecipeNameBinding((LinearLayout) rootView, recipeName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
