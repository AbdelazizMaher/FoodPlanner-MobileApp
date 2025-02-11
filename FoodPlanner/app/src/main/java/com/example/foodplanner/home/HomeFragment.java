package com.example.foodplanner.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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


public class HomeFragment extends Fragment implements HomeContract.IView{
    private CarouselRecyclerview carouselRecyclerview;
    private RandomRecyclerAdapter adapter;
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
        adapter = new RandomRecyclerAdapter(mealList);
        carouselRecyclerview.setAdapter(adapter);

        presenter.fetchRandomMeals();
    }

    @Override
    public void showRandomMeals(MealResponseModel.MealsDTO meal) {
        mealList.add(meal);
        adapter.setMeals(mealList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(error).setTitle("An Error Occurred");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}