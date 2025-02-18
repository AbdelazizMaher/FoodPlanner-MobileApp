package com.example.foodplanner.search.view.adapter;

import com.example.foodplanner.model.IngredientResponseModel;

public interface OnIngredientClickListener {
    void onIngredientClick(IngredientResponseModel.MealsDTO ingredient);
}
