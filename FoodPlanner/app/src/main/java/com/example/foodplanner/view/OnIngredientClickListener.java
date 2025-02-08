package com.example.foodplanner.view;

import com.example.foodplanner.model.IngredientResponseModel;

public interface OnIngredientClickListener {
    void onIngredientClick(IngredientResponseModel.MealsDTO ingredient);
}
