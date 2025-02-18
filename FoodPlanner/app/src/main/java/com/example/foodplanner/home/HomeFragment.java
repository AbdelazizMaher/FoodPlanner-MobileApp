package com.example.foodplanner.home;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.foodplanner.R;
import com.example.foodplanner.authentication.sharedpreference.SharedPreferenceCashing;
import com.example.foodplanner.database.MealLocalDataSource;
import com.example.foodplanner.model.MealRepository;
import com.example.foodplanner.network.api.MealRemoteApiDataSource;
import com.example.foodplanner.model.MealResponseModel;
import com.example.foodplanner.network.sync.MealRemoteSyncDataSource;
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
    ImageView profileImage;
    TextView welcomeTxt;
    private LottieAnimationView lottieAnimationView;
    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;
    private ScrollView scrollView;

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
        presenter = new HomePresenter(this, MealRepository.getInstance(MealLocalDataSource.getInstance(requireContext()), MealRemoteApiDataSource.getInstance(), MealRemoteSyncDataSource.getInstance()));
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lottieAnimationView = view.findViewById(R.id.lottieAnimationView);
        carouselRecyclerview = view.findViewById(R.id.carouselRecyclerview);
        recommendedRecyclerView = view.findViewById(R.id.recommendedRecyclerView);
        breakfastRecyclerView = view.findViewById(R.id.breakfastRecyclerView);
        profileImage = view.findViewById(R.id.profileImage);
        welcomeTxt = view.findViewById(R.id.welcomeTxt);
        scrollView = view.findViewById(R.id.scrollView);
        requireActivity().findViewById(R.id.bottomNavBar).setVisibility(View.VISIBLE);


        registerNetworkCallback();

        if(!isConnected()) {
            lottieAnimationView.setAnimation(R.raw.offline);
            lottieAnimationView.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }else{
            lottieAnimationView.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }


        if(SharedPreferenceCashing.getInstance().getUserId() != null) {
            welcomeTxt.setText("Welcome " + SharedPreferenceCashing.getInstance().getUserName());
        }else {
            welcomeTxt.setText("Welcome Guest");
        }

        randomAdapter = new HomeRecyclerAdapter(mealList, HomeRecyclerAdapter.LAYOUT_TYPE_RANDOM);
        carouselRecyclerview.setAdapter(randomAdapter);

        recommendedAdapter = new HomeRecyclerAdapter(new ArrayList<>(), HomeRecyclerAdapter.LAYOUT_TYPE_NORMAL);
        recommendedRecyclerView.setAdapter(recommendedAdapter);

        breakfastAdapter = new HomeRecyclerAdapter(new ArrayList<>(), HomeRecyclerAdapter.LAYOUT_TYPE_NORMAL);
        breakfastRecyclerView.setAdapter(breakfastAdapter);

        randomAdapter.setOnMealClickListener(meal-> {
            HomeFragmentDirections.ActionHomeFragment2ToAboutMealFragment action = HomeFragmentDirections.actionHomeFragment2ToAboutMealFragment(meal, Integer.parseInt(meal.getIdMeal()));
            Navigation.findNavController(requireView()).navigate(action);
        });
        breakfastAdapter.setOnMealClickListener(meal-> {
            HomeFragmentDirections.ActionHomeFragment2ToAboutMealFragment action = HomeFragmentDirections.actionHomeFragment2ToAboutMealFragment(meal, Integer.parseInt(meal.getIdMeal()));
            Navigation.findNavController(requireView()).navigate(action);
        });
        recommendedAdapter.setOnMealClickListener(meal-> {
            HomeFragmentDirections.ActionHomeFragment2ToAboutMealFragment action = HomeFragmentDirections.actionHomeFragment2ToAboutMealFragment(meal, Integer.parseInt(meal.getIdMeal()));
            Navigation.findNavController(requireView()).navigate(action);
        });

        profileImage.setOnClickListener(v -> {
            if(SharedPreferenceCashing.getInstance().getUserId() != null) {
                Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment2_to_profileFragment);
            }else {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Sign Up Required")
                        .setMessage("You need to sign up or log in to access your profile.")
                        .setPositiveButton("Sign Up", (dialog, which) -> {
                            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment2_to_registrationFragment);
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            }
        });

        presenter.syncMeals();
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
        recommendedAdapter.setMeals(new ArrayList<>(meals));
        recommendedAdapter.notifyDataSetChanged();
    }

    @Override
    public void showDesserts(List<MealResponseModel.MealsDTO> meals) {
        breakfastAdapter.setMeals(new ArrayList<>(meals));
        breakfastAdapter.notifyDataSetChanged();
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

    private void registerNetworkCallback() {
        connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                requireActivity().runOnUiThread(() -> {
                    lottieAnimationView.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                    presenter.syncMeals();
                    presenter.fetchRandomMeals();
                    presenter.fetchRecommendedMeals();
                    presenter.fetchDesserts();
                });
            }

            @Override
            public void onLost(Network network) {
                requireActivity().runOnUiThread(() -> {
                    lottieAnimationView.setAnimation(R.raw.offline);
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.GONE);
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

    public void showProgress(boolean show) {
        if(show) {
            lottieAnimationView.setAnimation(R.raw.d);
            lottieAnimationView.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }else {
            lottieAnimationView.setAnimation(R.raw.offline);
            lottieAnimationView.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }
}