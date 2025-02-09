package com.example.foodplanner.database;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.model.MealResponseModel;
import java.util.List;

public interface IMealLocalDataSource {
    public LiveData<List<MealResponseModel.MealsDTO>> getObservableProducts();
    public void insertMeal(MealResponseModel.MealsDTO product);
    public void deleteMeal(MealResponseModel.MealsDTO product);
}
