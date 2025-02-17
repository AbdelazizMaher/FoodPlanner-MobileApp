package com.example.foodplanner.home;

import com.example.foodplanner.model.MealRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
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
        repo.getRandomMeal()
                .subscribeOn(Schedulers.io())
                .repeat(10)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> view.showRandomMeals(response.getMeals().get(0)),
                        error -> view.showError(error.getMessage())
                );
    }

    @Override
    public void fetchRecommendedMeals() {
        repo.filterByArea("Egyptian")
                .subscribeOn(Schedulers.io())
                .flatMapObservable(response -> Observable.fromIterable(response.getMeals()))
                .flatMapSingle(meal -> repo.getMealDetailsById(Integer.parseInt(meal.getIdMeal()))
                        .map(detailsResponse -> detailsResponse.getMeals().get(0))
                )
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        detailedMeals -> view.showRecommendedMeals(detailedMeals),
                        error -> view.showError(error.getMessage())
                );
    }

    @Override
    public void fetchDesserts() {
        repo.filterByCategory("Breakfast")
                .subscribeOn(Schedulers.io())
                .flatMapObservable(response -> Observable.fromIterable(response.getMeals()))
                .flatMapSingle(meal -> repo.getMealDetailsById(Integer.parseInt(meal.getIdMeal()))
                        .map(detailsResponse -> detailsResponse.getMeals().get(0))
                )
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        detailedMeals -> view.showDesserts(detailedMeals),
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
