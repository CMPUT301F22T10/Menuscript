// Generated by view binder compiler. Do not edit!
package com.example.menuscript.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public final class MainMenuBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView appName;

  @NonNull
  public final Button ingredientButton;

  @NonNull
  public final ImageView logo;

  @NonNull
  public final Button mealPlanButton;

  @NonNull
  public final LinearLayout menuButtons;

  @NonNull
  public final Button recipeButton;

  @NonNull
  public final Button shoppingListButton;

  private MainMenuBinding(@NonNull ConstraintLayout rootView, @NonNull TextView appName,
      @NonNull Button ingredientButton, @NonNull ImageView logo, @NonNull Button mealPlanButton,
      @NonNull LinearLayout menuButtons, @NonNull Button recipeButton,
      @NonNull Button shoppingListButton) {
    this.rootView = rootView;
    this.appName = appName;
    this.ingredientButton = ingredientButton;
    this.logo = logo;
    this.mealPlanButton = mealPlanButton;
    this.menuButtons = menuButtons;
    this.recipeButton = recipeButton;
    this.shoppingListButton = shoppingListButton;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static MainMenuBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static MainMenuBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.main_menu, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static MainMenuBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.app_name;
      TextView appName = ViewBindings.findChildViewById(rootView, id);
      if (appName == null) {
        break missingId;
      }

      id = R.id.ingredient_button;
      Button ingredientButton = ViewBindings.findChildViewById(rootView, id);
      if (ingredientButton == null) {
        break missingId;
      }

      id = R.id.logo;
      ImageView logo = ViewBindings.findChildViewById(rootView, id);
      if (logo == null) {
        break missingId;
      }

      id = R.id.meal_plan_button;
      Button mealPlanButton = ViewBindings.findChildViewById(rootView, id);
      if (mealPlanButton == null) {
        break missingId;
      }

      id = R.id.menu_buttons;
      LinearLayout menuButtons = ViewBindings.findChildViewById(rootView, id);
      if (menuButtons == null) {
        break missingId;
      }

      id = R.id.recipe_button;
      Button recipeButton = ViewBindings.findChildViewById(rootView, id);
      if (recipeButton == null) {
        break missingId;
      }

      id = R.id.shopping_list_button;
      Button shoppingListButton = ViewBindings.findChildViewById(rootView, id);
      if (shoppingListButton == null) {
        break missingId;
      }

      return new MainMenuBinding((ConstraintLayout) rootView, appName, ingredientButton, logo,
          mealPlanButton, menuButtons, recipeButton, shoppingListButton);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
