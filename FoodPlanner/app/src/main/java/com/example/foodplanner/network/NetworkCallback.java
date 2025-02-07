package com.example.foodplanner.network;


public interface NetworkCallback<T> {
    public void onSuccess(T response);
    public void onFailure(String errorMessage);

}
