package com.example.foodplanner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodplanner.network.MealClient;
import com.example.foodplanner.network.MealResponseModel;
import com.example.foodplanner.network.NetworkCallback;
import com.example.foodplanner.view.RandomRecyclerAdapter;
import com.jackandphantom.carouselrecyclerview.CarouselRecyclerview;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private CarouselRecyclerview carouselRecyclerview;
    private RandomRecyclerAdapter adapter;
    private ArrayList<MealResponseModel> mealList = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        carouselRecyclerview = view.findViewById(R.id.carouselRecyclerview);
        adapter = new RandomRecyclerAdapter(getContext(), mealList);
        carouselRecyclerview.setAdapter(adapter);

        fetchRandomMeals();
    }

    private void fetchRandomMeals() {
        mealList.clear();
        adapter.notifyDataSetChanged();

        for (int i = 0; i < 3; i++) {
            MealClient.getInstance().makeNetworkCall(
                    MealClient.getInstance().getService().getRandomMeal(),
                    new NetworkCallback<MealResponseModel>() {
                        @Override
                        public void onSuccess(MealResponseModel response) {
                            if (response != null && response.getMeals() != null) {
                                mealList.add(response);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        @Override
                        public void onFailure(String errorMessage) {
                        }
                    }
            );
        }
    }

}