package com.example.foodplanner.home.presenter;

import com.example.foodplanner.home.HomeContract;
import com.example.foodplanner.repository.mealrepository.MealRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenter implements HomeContract.IPresenter {
    private HomeContract.IView view;
    private MealRepository repo;

    public HomePresenter(HomeContract.IView view, MealRepository repo) {
        this.view = view;
        this.repo = repo;
    }

    @Override
    public void fetchRandomMeals() {
        //view.showProgress(true);
        repo.getRandomMeal()
                .subscribeOn(Schedulers.io())
                .repeat(10)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            //view.showProgress(false);
                            view.showRandomMeals(response.getMeals().get(0));
                        },
                        error -> {
                           // view.showProgress(false);
                            view.showError(error.getMessage());
                        }
                );
    }

    @Override
    public void fetchRecommendedMeals() {
        //view.showProgress(true);
        repo.filterByArea("Egyptian")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                           // view.showProgress(false);
                            view.showRecommendedMeals(response.getMeals());
                        },
                        error -> {
                          //  view.showProgress(false);
                            view.showError(error.getMessage());
                        }
                );
    }

    @Override
    public void fetchDesserts() {
        repo.filterByCategory("Breakfast")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> view.showDesserts(response.getMeals()),
                        error -> view.showError(error.getMessage())
                );
    }

    public void syncMeals() {
        repo.syncMealsFromRemoteToLocal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {},
                        error -> view.showError(error.getMessage())
                );
    }
}
