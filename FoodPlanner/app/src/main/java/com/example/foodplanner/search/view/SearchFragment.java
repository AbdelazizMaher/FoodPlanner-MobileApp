package com.example.foodplanner.search.view;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.foodplanner.R;
import com.example.foodplanner.database.sharedpreference.SharedPreferenceCashing;
import com.example.foodplanner.database.localdatabase.MealLocalDataSource;
import com.example.foodplanner.model.AreaResponseModel;
import com.example.foodplanner.model.CategoryResponseModel;
import com.example.foodplanner.model.IngredientResponseModel;
import com.example.foodplanner.repository.mealrepository.MealRepository;
import com.example.foodplanner.network.api.MealRemoteApiDataSource;
import com.example.foodplanner.network.sync.MealRemoteSyncDataSource;
import com.example.foodplanner.search.SearchContract;
import com.example.foodplanner.search.view.SearchFragmentDirections;
import com.example.foodplanner.search.presenter.SearchPresenter;
import com.example.foodplanner.search.view.adapter.AreaRecyclerAdapter;
import com.example.foodplanner.search.view.adapter.CategoryRecyclerAdapter;
import com.example.foodplanner.search.view.adapter.IngredientRecyclerAdapter;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment implements SearchContract.IView {

    TextView ingredientsTitle, areasTitle, categoriesTitle;
    ImageView profileImage;
    ChipGroup chipGroup;
    SearchView searchView;
    RecyclerView ingredientsRecyclerView;
    RecyclerView areasRecyclerView;
    RecyclerView categoriesRecyclerView;
    IngredientRecyclerAdapter ingredientsAdapter;
    AreaRecyclerAdapter areasAdapter;
    CategoryRecyclerAdapter categoriesAdapter;
    SearchPresenter presenter;
    Chiptype type;
    TextView welcomeTxt;
    private LottieAnimationView lottieAnimationView;
    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;
    private LinearLayout topSection;
    private NestedScrollView nestedScrollView;

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
        presenter = new SearchPresenter(this, MealRepository.getInstance(MealLocalDataSource.getInstance(requireContext()), MealRemoteApiDataSource.getInstance(), MealRemoteSyncDataSource.getInstance()));
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);
        setupAdapters();
        registerNetworkCallback();

        if(!isConnected()) {
            lottieAnimationView.setVisibility(View.VISIBLE);
            topSection.setVisibility(View.GONE);
            nestedScrollView.setVisibility(View.GONE);
        }else{
            lottieAnimationView.setVisibility(View.GONE);
            topSection.setVisibility(View.VISIBLE);
            nestedScrollView.setVisibility(View.VISIBLE);
        }

        if(SharedPreferenceCashing.getInstance().getUserId() != null) {
            welcomeTxt.setText("Welcome " + SharedPreferenceCashing.getInstance().getUserName());
        }else {
            welcomeTxt.setText("Welcome Guest");
        }

        presenter.fetchIngredients();
        presenter.fetchAreas();
        presenter.fetchCategories();

        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.chipIngredients) {
                type = Chiptype.INGREDIENTS;
            } else if (checkedId == R.id.chipAreas) {
                type = Chiptype.AREAS;
            } else if (checkedId == R.id.chipCategories) {
                type = Chiptype.CATEGORIES;
            }
            updateViewVisibility(type);
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

        searchView.setOnSearchClickListener(v -> {
            chipGroup.setVisibility(View.VISIBLE);

        });

        searchView.setOnCloseListener(() -> {
            chipGroup.setVisibility(View.GONE);
            updateViewVisibility(null);
            return false;
        });

        profileImage.setOnClickListener(v -> {
            if(SharedPreferenceCashing.getInstance().getUserId() != null) {
                Navigation.findNavController(requireView()).navigate(R.id.action_searchFragment_to_profileFragment);
            }else {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Sign Up Required")
                        .setMessage("You need to sign up or log in to access your profile.")
                        .setPositiveButton("Sign Up", (dialog, which) -> {
                            Navigation.findNavController(requireView()).navigate(R.id.action_searchFragment_to_profileFragment);
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
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
//        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
//        builder.setMessage(error).setTitle("An Error Occurred");
//        AlertDialog dialog = builder.create();
//        dialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (connectivityManager != null && networkCallback != null) {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }

    }

    private void updateViewVisibility(Chiptype type) {
        boolean showAll = (type == null);

        ingredientsRecyclerView.setVisibility(showAll || type == Chiptype.INGREDIENTS ? View.VISIBLE : View.GONE);
        areasRecyclerView.setVisibility(showAll || type == Chiptype.AREAS ? View.VISIBLE : View.GONE);
        categoriesRecyclerView.setVisibility(showAll || type == Chiptype.CATEGORIES ? View.VISIBLE : View.GONE);

        ingredientsTitle.setVisibility(showAll || type == Chiptype.INGREDIENTS ? View.VISIBLE : View.GONE);
        areasTitle.setVisibility(showAll || type == Chiptype.AREAS ? View.VISIBLE : View.GONE);
        categoriesTitle.setVisibility(showAll || type == Chiptype.CATEGORIES ? View.VISIBLE : View.GONE);
    }

    private void initUI(View view) {
        ingredientsRecyclerView = view.findViewById(R.id.ingredientsRecyclerView);
        areasRecyclerView = view.findViewById(R.id.areasRecyclerView);
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
        chipGroup = view.findViewById(R.id.chipGroup);
        searchView = view.findViewById(R.id.searchView);
        ingredientsTitle = view.findViewById(R.id.ingredientsTitle);
        areasTitle = view.findViewById(R.id.areasTitle);
        categoriesTitle = view.findViewById(R.id.categoriesTitle);
        profileImage = view.findViewById(R.id.profileImage);
        welcomeTxt = view.findViewById(R.id.welcomeTxt);
        lottieAnimationView = view.findViewById(R.id.lottieAnimationView);
        topSection = view.findViewById(R.id.topSection);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);
    }

    private void setupAdapters() {
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
    }

    private enum Chiptype {
        INGREDIENTS,
        AREAS,
        CATEGORIES
    }

    private void registerNetworkCallback() {
        connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                requireActivity().runOnUiThread(() -> {
                    lottieAnimationView.setVisibility(View.GONE);
                    topSection.setVisibility(View.VISIBLE);
                    nestedScrollView.setVisibility(View.VISIBLE);
                    presenter.fetchIngredients();
                    presenter.fetchAreas();
                    presenter.fetchCategories();
                });
            }

            @Override
            public void onLost(Network network) {
                requireActivity().runOnUiThread(() -> {
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    topSection.setVisibility(View.GONE);
                    nestedScrollView.setVisibility(View.GONE);
                });
            }
        };
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            Network network = cm.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
                return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            }
        }
        return false;
    }

}