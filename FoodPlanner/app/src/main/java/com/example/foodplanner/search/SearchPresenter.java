package com.example.foodplanner.search;

import com.example.foodplanner.model.AreaResponseModel;
import com.example.foodplanner.model.CategoryResponseModel;
import com.example.foodplanner.model.IngredientResponseModel;
import com.example.foodplanner.model.MealRepository;
import com.example.foodplanner.network.MealRemoteDataSource;
import com.example.foodplanner.network.NetworkCallback;

public class SearchPresenter implements SearchContract.IPresenter {

    private SearchContract.IView view;
    private MealRepository repo;

    public SearchPresenter(SearchContract.IView view, MealRepository repo) {
        this.view = view;
        this.repo = repo;
    }

    @Override
    public void fetchIngredients() {
        repo.makeNetworkCall(MealRemoteDataSource.getInstance().getService().getIngredients("list"), new NetworkCallback<IngredientResponseModel>() {
            @Override
            public void onSuccess(IngredientResponseModel response) {
                if (response != null && response.getMeals() != null) {
                    view.showIngredients(response.getMeals());
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    @Override
    public void fetchAreas() {
        repo.makeNetworkCall(MealRemoteDataSource.getInstance().getService().getAreas("list"), new NetworkCallback<AreaResponseModel>() {

            @Override
            public void onSuccess(AreaResponseModel response) {
                if (response != null && response.getMeals() != null) {
                    view.showAreas(response.getMeals());
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    @Override
    public void fetchCategories() {
        repo.makeNetworkCall(MealRemoteDataSource.getInstance().getService().getMealCategories(), new NetworkCallback<CategoryResponseModel>() {
            @Override
            public void onSuccess(CategoryResponseModel response) {
                if (response != null && response.getCategories() != null) {
                    view.showCategories(response.getCategories());
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
}
