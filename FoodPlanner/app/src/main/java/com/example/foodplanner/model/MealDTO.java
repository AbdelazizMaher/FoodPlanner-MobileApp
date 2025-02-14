package com.example.foodplanner.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(
        tableName = "meals_table",
        primaryKeys = {"idUser", "idMeal", "date"}
)
public class MealDTO {
    @NonNull
    private String idMeal;
    @NonNull
    private String idUser;
    @NonNull
    private String date;
    private MealResponseModel.MealsDTO meal;
    private Boolean isFavorite;
    private Boolean isPlanned;

    public MealDTO(MealResponseModel.MealsDTO meal) {
        this.meal = meal;
    }

    @NonNull
    public String getIdMeal() {
        return idMeal;
    }

    public void setIdMeal(@NonNull String idMeal) {
        this.idMeal = idMeal;
    }

    @NonNull
    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(@NonNull String idUser) {
        this.idUser = idUser;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    public MealResponseModel.MealsDTO getMeal() {
        return meal;
    }

    public void setMeal(MealResponseModel.MealsDTO meal) {
        this.meal = meal;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public Boolean getPlanned() {
        return isPlanned;
    }

    public void setPlanned(Boolean planned) {
        isPlanned = planned;
    }
}
