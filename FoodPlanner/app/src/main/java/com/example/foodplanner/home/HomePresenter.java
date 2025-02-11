package com.example.foodplanner.home;

import com.example.foodplanner.model.MealRepository;
import com.example.foodplanner.model.MealResponseModel;
import com.example.foodplanner.network.MealRemoteDataSource;
import com.example.foodplanner.network.NetworkCallback;

public class HomePresenter implements HomeContract.IPresenter {
    private HomeContract.IView view;
    private MealRepository repo;

    public HomePresenter(HomeContract.IView view, MealRepository repo) {
        this.view = view;
        this.repo = repo;
    }

    @Override
    public void fetchRandomMeals() {

        for (int i = 0; i < 10; i++) {
            repo.makeNetworkCall(MealRemoteDataSource.getInstance().getService().getRandomMeal(),new NetworkCallback<MealResponseModel>() {
                @Override
                public void onSuccess(MealResponseModel response) {
                    if (response != null && response.getMeals() != null) {
                        view.showRandomMeals(response.getMeals().get(0));
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                }
            });
        }
    }

    @Override
    public void fetchRecommendedMeals() {
        repo.makeNetworkCall(MealRemoteDataSource.getInstance().getService().filterByArea("Egyptian"),new NetworkCallback<MealResponseModel>() {
            @Override
            public void onSuccess(MealResponseModel response) {
                if (response != null && response.getMeals() != null) {
                    view.showRecommendedMeals(response.getMeals());
                }
            }

            @Override
            public void onFailure(String errorMessage) {
            }
        });
    }

    @Override
    public void fetchDesserts() {
        repo.makeNetworkCall(MealRemoteDataSource.getInstance().getService().filterByCategory("Breakfast"),new NetworkCallback<MealResponseModel>() {
            @Override
            public void onSuccess(MealResponseModel response) {
                if (response != null && response.getMeals() != null) {
                    view.showDesserts(response.getMeals());
                }
            }

            @Override
            public void onFailure(String errorMessage) {
            }
        });
    }
}
