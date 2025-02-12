package com.example.foodplanner.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodplanner.R;
import com.example.foodplanner.database.MealLocalDataSource;
import com.example.foodplanner.model.AreaResponseModel;
import com.example.foodplanner.model.CategoryResponseModel;
import com.example.foodplanner.model.IngredientResponseModel;
import com.example.foodplanner.model.MealRepository;
import com.example.foodplanner.network.MealRemoteDataSource;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment implements SearchContract.IView {

    RecyclerView ingredientsRecyclerView;
    RecyclerView areasRecyclerView;
    RecyclerView categoriesRecyclerView;
    IngredientRecyclerAdapter ingredientsAdapter;
    AreaRecyclerAdapter areasAdapter;
    CategoryRecyclerAdapter categoriesAdapter;
    SearchPresenter presenter;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        presenter = new SearchPresenter(this, MealRepository.getInstance(MealLocalDataSource.getInstance(requireContext()), MealRemoteDataSource.getInstance()));
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ingredientsRecyclerView = view.findViewById(R.id.ingredientsRecyclerView);
        areasRecyclerView = view.findViewById(R.id.areasRecyclerView);
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView);

        ingredientsAdapter = new IngredientRecyclerAdapter(new ArrayList<>());
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);

        areasAdapter = new AreaRecyclerAdapter(new ArrayList<>());
        areasRecyclerView.setAdapter(areasAdapter);

        categoriesAdapter = new CategoryRecyclerAdapter(new ArrayList<>());
        categoriesRecyclerView.setAdapter(categoriesAdapter);

        presenter.fetchIngredients();
        presenter.fetchAreas();
        presenter.fetchCategories();

    }

    @Override
    public void showIngredients(List<IngredientResponseModel.MealsDTO> ingredients) {
        ingredientsAdapter.setIngredients(new ArrayList<>(ingredients));
        ingredientsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showAreas(List<AreaResponseModel.MealsDTO> areas) {
        areasAdapter.setCountries(new ArrayList<>(areas));
        areasAdapter.notifyDataSetChanged();
    }

    @Override
    public void showCategories(List<CategoryResponseModel.CategoriesDTO> categories) {
        categoriesAdapter.setCategories(new ArrayList<>(categories));
        categoriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(error).setTitle("An Error Occurred");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}