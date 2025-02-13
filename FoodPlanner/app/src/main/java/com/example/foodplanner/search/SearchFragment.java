package com.example.foodplanner.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foodplanner.R;
import com.example.foodplanner.database.MealLocalDataSource;
import com.example.foodplanner.home.HomeFragmentDirections;
import com.example.foodplanner.model.AreaResponseModel;
import com.example.foodplanner.model.CategoryResponseModel;
import com.example.foodplanner.model.IngredientResponseModel;
import com.example.foodplanner.model.MealRepository;
import com.example.foodplanner.network.MealRemoteDataSource;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment implements SearchContract.IView {

    TextView ingredientsTitle, areasTitle, categoriesTitle;
    ChipGroup chipGroup;
    SearchView searchView;
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
        chipGroup = view.findViewById(R.id.chipGroup);
        searchView = view.findViewById(R.id.searchView);
        ingredientsTitle = view.findViewById(R.id.ingredientsTitle);
        areasTitle = view.findViewById(R.id.areasTitle);
        categoriesTitle = view.findViewById(R.id.categoriesTitle);

        ingredientsAdapter = new IngredientRecyclerAdapter(new ArrayList<>());
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);
        ingredientsAdapter.setOnIngredientClickListener(ingredient -> {
            SearchFragmentDirections.ActionSearchFragmentToAllMealsFragment action =
                    SearchFragmentDirections.actionSearchFragmentToAllMealsFragment(ingredient.getStrIngredient(), 1);
            Navigation.findNavController(requireView()).navigate(action);
        });

        areasAdapter = new AreaRecyclerAdapter(new ArrayList<>());
        areasRecyclerView.setAdapter(areasAdapter);
        areasAdapter.setOnAreaClickListener(area -> {
            SearchFragmentDirections.ActionSearchFragmentToAllMealsFragment action =
                    SearchFragmentDirections.actionSearchFragmentToAllMealsFragment(area.getStrArea(), 2);
            Navigation.findNavController(requireView()).navigate(action);
        });

        categoriesAdapter = new CategoryRecyclerAdapter(new ArrayList<>());
        categoriesRecyclerView.setAdapter(categoriesAdapter);
        categoriesAdapter.setOnCategoryClickListener(category -> {
            SearchFragmentDirections.ActionSearchFragmentToAllMealsFragment action =
                    SearchFragmentDirections.actionSearchFragmentToAllMealsFragment(category.getStrCategory(), 3);
            Navigation.findNavController(requireView()).navigate(action);
        });

        presenter.fetchIngredients();
        presenter.fetchAreas();
        presenter.fetchCategories();

        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.chipIngredients) {
                ingredientsRecyclerView.setVisibility(View.VISIBLE);
                areasRecyclerView.setVisibility(View.GONE);
                categoriesRecyclerView.setVisibility(View.GONE);
                ingredientsTitle.setVisibility(View.VISIBLE);
                areasTitle.setVisibility(View.GONE);
                categoriesTitle.setVisibility(View.GONE);
            } else if (checkedId == R.id.chipAreas) {
                ingredientsRecyclerView.setVisibility(View.GONE);
                areasRecyclerView.setVisibility(View.VISIBLE);
                categoriesRecyclerView.setVisibility(View.GONE);
                ingredientsTitle.setVisibility(View.GONE);
                areasTitle.setVisibility(View.VISIBLE);
                categoriesTitle.setVisibility(View.GONE);
            } else if (checkedId == R.id.chipCategories) {
                ingredientsRecyclerView.setVisibility(View.GONE);
                areasRecyclerView.setVisibility(View.GONE);
                categoriesRecyclerView.setVisibility(View.VISIBLE);
                ingredientsTitle.setVisibility(View.GONE);
                areasTitle.setVisibility(View.GONE);
                categoriesTitle.setVisibility(View.VISIBLE);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterResults(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterResults(newText);
                return false;
            }
        });
    }

    private void filterResults(String query) {
        if (ingredientsRecyclerView.getVisibility() == View.VISIBLE) {
            ingredientsAdapter.filter(query);
        } else if (areasRecyclerView.getVisibility() == View.VISIBLE) {
            areasAdapter.filter(query);
        } else if (categoriesRecyclerView.getVisibility() == View.VISIBLE) {
            categoriesAdapter.filter(query);
        }
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