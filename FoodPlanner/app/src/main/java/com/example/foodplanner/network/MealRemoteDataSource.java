package com.example.foodplanner.network;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealRemoteDataSource implements IMealRemoteDataSource {
    private static String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private MealApiService service;
    private static MealRemoteDataSource instance = null;

    private MealRemoteDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(MealApiService.class);
    }

    public static MealRemoteDataSource getInstance() {
        if (instance == null) {
            instance = new MealRemoteDataSource();
        }
        return instance;
    }

    public MealApiService getService() {
        return service;
    }

    @Override
    public <T> void makeNetworkCall(Call<T> call, final NetworkCallback<T> callback) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Response failed: " + response.message());
                }
            }
    
            @Override
            public void onFailure(Call<T> call, Throwable t) {
                callback.onFailure("Network Error: " + t.getMessage());
            }
        });
    }
}
