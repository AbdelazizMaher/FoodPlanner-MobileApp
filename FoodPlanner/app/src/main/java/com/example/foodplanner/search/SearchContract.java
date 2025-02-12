package com.example.foodplanner.search;

import com.example.foodplanner.model.AreaResponseModel;
import com.example.foodplanner.model.CategoryResponseModel;
import com.example.foodplanner.model.IngredientResponseModel;

import java.util.List;

public interface SearchContract {
    interface IView {
        void showIngredients(List<IngredientResponseModel.MealsDTO> ingredients);
        void showAreas(List<AreaResponseModel.MealsDTO> areas);
        void showCategories(List<CategoryResponseModel.CategoriesDTO> categories);
        void showError(String error);
    }

    interface IPresenter {
        void fetchIngredients();
        void fetchAreas();
        void fetchCategories();
    }
}
