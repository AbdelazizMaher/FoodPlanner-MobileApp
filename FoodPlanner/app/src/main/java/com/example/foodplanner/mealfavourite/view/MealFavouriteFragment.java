package com.example.foodplanner.mealfavourite.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.foodplanner.R;
import com.example.foodplanner.database.sharedpreference.SharedPreferenceCashing;
import com.example.foodplanner.database.localdatabase.MealLocalDataSource;
import com.example.foodplanner.mealfavourite.MealFavouriteContract;
import com.example.foodplanner.mealfavourite.presenter.MealFavouritePresenter;
import com.example.foodplanner.model.MealDTO;
import com.example.foodplanner.utils.connectionutil.ConnectionUtil;
import com.example.foodplanner.repository.mealrepository.MealRepository;
import com.example.foodplanner.network.api.MealRemoteApiDataSource;
import com.example.foodplanner.network.sync.MealRemoteSyncDataSource;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MealFavouriteFragment extends Fragment implements MealFavouriteContract.IView {

    private RecyclerView mealFavouriteRecyclerView;
    private MealFavouriteRecyclerAdapter adapter;
    private MealFavouritePresenter presenter;
    private Group favouriteGroup;
    LottieAnimationView lottieAnimationView;
    TextView infoText;

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

        favouriteGroup = view.findViewById(R.id.favouriteGroup);
        infoText = view.findViewById(R.id.infoText);
        lottieAnimationView = view.findViewById(R.id.lottieAnimationView);
        mealFavouriteRecyclerView = view.findViewById(R.id.mealFavouriteRecyclerView);
        adapter = new MealFavouriteRecyclerAdapter(new ArrayList<>());
        mealFavouriteRecyclerView.setAdapter(adapter);

        if(SharedPreferenceCashing.getInstance().getUserId() == null) {
            mealFavouriteRecyclerView.setVisibility(View.GONE);
            favouriteGroup.setVisibility(View.VISIBLE);
            infoText.setText("You need to sign up or log in to access your favourite Meals");
        }else {
            mealFavouriteRecyclerView.setVisibility(View.VISIBLE);
            favouriteGroup.setVisibility(View.GONE);
            presenter.fetchFavouriteMeals(SharedPreferenceCashing.getInstance().getUserId());
        }

        adapter.setOnRemoveButtonClickListener(meal->{
            if(ConnectionUtil.isConnected(requireContext())) {
                presenter.removeMealFromFavourite(meal);
            }else{
                Snackbar.make(view, "No internet connection", Snackbar.LENGTH_SHORT).show();
            }
        });
        adapter.setOnMealClickListener(meal->{
            MealFavouriteFragmentDirections.ActionMealFavouriteFragmentToAboutMealFragment action = MealFavouriteFragmentDirections.actionMealFavouriteFragmentToAboutMealFragment(meal,0);
            Navigation.findNavController(requireView()).navigate(action);
        });
    }

    @Override
    public void showFavouriteMeals(List<MealDTO> meals) {
        adapter.setMeals(meals);
        adapter.notifyDataSetChanged();
        lottieAnimationView.setVisibility(View.GONE);
        infoText.setVisibility(View.GONE);
        mealFavouriteRecyclerView.setVisibility(View.VISIBLE);
        if(meals.isEmpty()) {
            mealFavouriteRecyclerView.setVisibility(View.GONE);
            lottieAnimationView.setVisibility(View.VISIBLE);
            infoText.setVisibility(View.VISIBLE);
            infoText.setText("You don't have any favourite meals");
        }
    }
}