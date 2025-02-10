package com.example.foodplanner.network;

import retrofit2.Call;

public interface IMealRemoteDataSource {
    public <T> void makeNetworkCall(Call<T> call, final NetworkCallback<T> callback);
}
