package com.example.foodplanner.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodplanner.R;
import com.example.foodplanner.database.MealLocalDataSource;
import com.example.foodplanner.model.MealRepository;
import com.example.foodplanner.network.MealRemoteDataSource;
import com.example.foodplanner.model.MealResponseModel;
import com.example.foodplanner.network.NetworkCallback;
import com.jackandphantom.carouselrecyclerview.CarouselRecyclerview;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements HomeContract.IView{
    private CarouselRecyclerview carouselRecyclerview;
    private RecyclerView recommendedRecyclerView;
    private RecyclerView breakfastRecyclerView;
    private HomeRecyclerAdapter randomAdapter;
    private HomeRecyclerAdapter recommendedAdapter;
    private HomeRecyclerAdapter breakfastAdapter;
    private ArrayList<MealResponseModel.MealsDTO> mealList = new ArrayList<>();
    private HomePresenter presenter;

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
        presenter = new HomePresenter(this, MealRepository.getInstance(MealLocalDataSource.getInstance(requireContext()), MealRemoteDataSource.getInstance()));
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        carouselRecyclerview = view.findViewById(R.id.carouselRecyclerview);
        recommendedRecyclerView = view.findViewById(R.id.recommendedRecyclerView);
        breakfastRecyclerView = view.findViewById(R.id.breakfastRecyclerView);

        randomAdapter = new HomeRecyclerAdapter(mealList, HomeRecyclerAdapter.LAYOUT_TYPE_RANDOM);
        carouselRecyclerview.setAdapter(randomAdapter);

        recommendedAdapter = new HomeRecyclerAdapter(new ArrayList<>(), HomeRecyclerAdapter.LAYOUT_TYPE_NORMAL);
        recommendedRecyclerView.setAdapter(recommendedAdapter);

        breakfastAdapter = new HomeRecyclerAdapter(new ArrayList<>(), HomeRecyclerAdapter.LAYOUT_TYPE_NORMAL);
        breakfastRecyclerView.setAdapter(breakfastAdapter);

        randomAdapter.setOnMealClickListener(meal-> {
            HomeFragmentDirections.ActionHomeFragment2ToAboutMealFragment action = HomeFragmentDirections.actionHomeFragment2ToAboutMealFragment(meal);
            Navigation.findNavController(requireView()).navigate(action);
        });
        breakfastAdapter.setOnMealClickListener(meal-> {
            HomeFragmentDirections.ActionHomeFragment2ToAboutMealFragment action = HomeFragmentDirections.actionHomeFragment2ToAboutMealFragment(meal);
            Navigation.findNavController(requireView()).navigate(action);
        });
        recommendedAdapter.setOnMealClickListener(meal-> {
            HomeFragmentDirections.ActionHomeFragment2ToAboutMealFragment action = HomeFragmentDirections.actionHomeFragment2ToAboutMealFragment(meal);
            Navigation.findNavController(requireView()).navigate(action);
        });

        presenter.fetchRandomMeals();
        presenter.fetchRecommendedMeals();
        presenter.fetchDesserts();
    }

    @Override
    public void showRandomMeals(MealResponseModel.MealsDTO meal) {
        mealList.add(meal);
        randomAdapter.setMeals(mealList);
        randomAdapter.notifyDataSetChanged();
    }

    @Override
    public void showRecommendedMeals(List<MealResponseModel.MealsDTO> meals) {
        for (MealResponseModel.MealsDTO meal : meals) {
            Log.d("API Response", "Recommended Meal: " + meal.getStrMeal() + ", Instructions: " + meal.getStrInstructions());
        }
        recommendedAdapter.setMeals(new ArrayList<>(meals));
        recommendedAdapter.notifyDataSetChanged();
    }

    @Override
    public void showDesserts(List<MealResponseModel.MealsDTO> meals) {
        for (MealResponseModel.MealsDTO meal : meals) {
            Log.d("API Response", "Dessert Meal: " + meal.getStrMeal() + ", Instructions: " + meal.getStrInstructions());
        }
        breakfastAdapter.setMeals(new ArrayList<>(meals));
        breakfastAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(error).setTitle("An Error Occurred");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}