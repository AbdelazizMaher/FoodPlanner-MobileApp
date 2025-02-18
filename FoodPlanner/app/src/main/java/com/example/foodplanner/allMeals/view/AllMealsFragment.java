package com.example.foodplanner.allMeals.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodplanner.R;
import com.example.foodplanner.allMeals.AllMealsContract;
import com.example.foodplanner.allMeals.view.AllMealsFragmentArgs;
import com.example.foodplanner.allMeals.view.AllMealsFragmentDirections;
import com.example.foodplanner.allMeals.presenter.AllMealsPresenter;
import com.example.foodplanner.database.localdatabase.MealLocalDataSource;
import com.example.foodplanner.repository.mealrepository.MealRepository;
import com.example.foodplanner.model.MealResponseModel;
import com.example.foodplanner.network.api.MealRemoteApiDataSource;
import com.example.foodplanner.network.sync.MealRemoteSyncDataSource;

import java.util.ArrayList;
import java.util.List;

public class AllMealsFragment extends Fragment implements AllMealsContract.IView {

    private TextView typeMeals;
    SearchView searchView;
    ImageView backArrow, backToSearch;
    private RecyclerView mealsRecyclerView;
    private AllMealsRecyclerAdapter adapter;
    private List<MealResponseModel.MealsDTO> sharedMeals = new ArrayList<>();
    private AllMealsPresenter presenter;
    private String type;
    private int typeID;

    public AllMealsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        presenter = new AllMealsPresenter(this, MealRepository.getInstance(MealLocalDataSource.getInstance(requireContext()), MealRemoteApiDataSource.getInstance(), MealRemoteSyncDataSource.getInstance()));
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_meals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        typeMeals = view.findViewById(R.id.typeMeals);
        mealsRecyclerView = view.findViewById(R.id.rvMeals);
        searchView = view.findViewById(R.id.searchView);
        backArrow = view.findViewById(R.id.ivBackArrow);
        backToSearch = view.findViewById(R.id.backToSearch);

        backToSearch.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigateUp();
        });

        adapter = new AllMealsRecyclerAdapter(new ArrayList<>());
        mealsRecyclerView.setAdapter(adapter);
        adapter.setOnMealClickListener(meal -> {
            AllMealsFragmentDirections.ActionAllMealsFragmentToAboutMealFragment action = AllMealsFragmentDirections.actionAllMealsFragmentToAboutMealFragment(meal, Integer.parseInt(meal.getIdMeal()));
            Navigation.findNavController(requireView()).navigate(action);
        });

        backArrow.setOnClickListener(v -> {
            searchView.setQuery("", false);
            backArrow.setVisibility(View.GONE);
            adapter.setMeals(sharedMeals);
            adapter.notifyDataSetChanged();
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterMeals(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterMeals(newText);

                if (!TextUtils.isEmpty(newText)) {
                    backArrow.setVisibility(View.VISIBLE);
                } else {
                    backArrow.setVisibility(View.GONE);
                }
                return true;
            }
        });

        if (getArguments() != null) {
            AllMealsFragmentArgs args = AllMealsFragmentArgs.fromBundle(getArguments());
            type = args.getSearchID();
            typeID = args.getType();
        }
        if (typeID == 1) {
            typeMeals.setText("Ingredients");
            presenter.fetchMealsByIngredients(type);
        }else if (typeID == 2) {
            typeMeals.setText("Areas");
            presenter.fetchMealsByAreas(type);
        }else if (typeID == 3) {
            typeMeals.setText("Categories");
            presenter.fetchMealsByCategories(type);
        }

    }

    @Override
    public void showMeals(List<MealResponseModel.MealsDTO> meals) {
        sharedMeals.addAll(meals);
        adapter.setMeals(meals);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(error).setTitle("An Error Occurred");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void filterMeals(String query) {
        List<MealResponseModel.MealsDTO> filteredList = new ArrayList<>();
        for (MealResponseModel.MealsDTO meal : sharedMeals) {
            if (meal.getStrMeal().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(meal);
            }
        }
        adapter.setMeals(filteredList);
        adapter.notifyDataSetChanged();
    }
}