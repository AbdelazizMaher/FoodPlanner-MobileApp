package com.example.foodplanner.mealfavourite;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodplanner.R;
import com.example.foodplanner.authentication.registration.RegistrationPresenter;
import com.example.foodplanner.authentication.sharedpreference.SharedPreferenceCashing;
import com.example.foodplanner.database.MealLocalDataSource;
import com.example.foodplanner.model.MealDTO;
import com.example.foodplanner.model.MealRepository;
import com.example.foodplanner.network.api.MealRemoteApiDataSource;
import com.example.foodplanner.network.sync.MealRemoteSyncDataSource;

import java.util.ArrayList;
import java.util.List;

public class MealFavouriteFragment extends Fragment implements MealFavouriteContract.IView {

    private RecyclerView mealFavouriteRecyclerView;
    private MealFavouriteRecyclerAdapter adapter;
    private MealFavouritePresenter presenter;

    public MealFavouriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        presenter = new MealFavouritePresenter(this, MealRepository.getInstance(MealLocalDataSource.getInstance(requireContext()), MealRemoteApiDataSource.getInstance(), MealRemoteSyncDataSource.getInstance()));
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal_favourite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mealFavouriteRecyclerView = view.findViewById(R.id.mealFavouriteRecyclerView);
        adapter = new MealFavouriteRecyclerAdapter(new ArrayList<>());
        mealFavouriteRecyclerView.setAdapter(adapter);
        adapter.setOnRemoveButtonClickListener(meal->{
            presenter.removeMealFromFavourite(meal);
        });

        presenter.fetchFavouriteMeals(SharedPreferenceCashing.getInstance().getUserId());

    }

    @Override
    public void showFavouriteMeals(List<MealDTO> meals) {
        adapter.setMeals(meals);
        adapter.notifyDataSetChanged();
    }
}