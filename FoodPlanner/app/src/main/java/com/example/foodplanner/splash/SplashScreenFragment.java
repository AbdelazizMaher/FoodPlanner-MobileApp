package com.example.foodplanner.splash;

import android.animation.Animator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.example.foodplanner.R;
import com.example.foodplanner.database.sharedpreference.SharedPreferenceCashing;


public class SplashScreenFragment extends Fragment {

    private LottieAnimationView lottieAnimationView;

    public SplashScreenFragment() {
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
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lottieAnimationView = view.findViewById(R.id.lottieAnimationView);

        lottieAnimationView.setAnimation(R.raw.a);
        lottieAnimationView.playAnimation();

        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                lottieAnimationView.setAnimation(R.raw.c);
                lottieAnimationView.playAnimation();
            }

            @Override
            public void onAnimationStart(Animator animation) {}
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferenceCashing.setInstance(requireContext());
                String userId = SharedPreferenceCashing.getInstance().getUserId();
                if (userId == null) {
                    Navigation.findNavController(view).navigate(R.id.action_splashScreenFragment_to_registrationFragment);
                } else {
                    Navigation.findNavController(view).navigate(R.id.action_splashScreenFragment_to_homeFragment2);
                }
            }
        }, 10000);
    }
}