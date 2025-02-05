package com.example.foodplanner;

import android.animation.Animator;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;

import jp.wasabeef.glide.transformations.BlurTransformation;


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


//        ImageView imageView = view.findViewById(R.id.backgroundImage);
//        Glide.with(this)
//                .load(R.drawable.background_image)
//                .transform(new BlurTransformation(25, 3))
//                .into(imageView);

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
                Navigation.findNavController(view).navigate(R.id.action_splashScreenFragment_to_registrationFragment);
            }
        }, 10000);
    }
}