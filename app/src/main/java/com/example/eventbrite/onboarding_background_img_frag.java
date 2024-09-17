package com.example.eventbrite;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link onboarding_background_img_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class onboarding_background_img_frag extends Fragment {
    private ImageView onboardingImageView;
    private ImageView onboardingFragScreenPositionImageView;
    private TextView onboardingFragCardTv1;
    private TextView onboardingFragSkipClickableTv;
    private TextView onboardingFragNextClickableTv;
    private int currentStep = 0;

    private int[] bgImageResource = {
            R.drawable.onboarding1_bg,
            R.drawable.onboarding2_bg,
            R.drawable.onboarding3_bg

    };

    private int[] screenPositionImageResource = {
            R.drawable.dots1,
            R.drawable.dot2,
            R.drawable.dot3

    };

    private int[] textSteps = {
            R.string.explore_upcoming_and_nearby_events,
            R.string.we_have_modern_events_calendar_feature,
            R.string.to_look_up_more_events
    };


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public onboarding_background_img_frag() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment onboarding_background_img_frag.
     */
    // TODO: Rename and change types and number of parameters
    public static onboarding_background_img_frag newInstance(String param1, String param2) {
        onboarding_background_img_frag fragment = new onboarding_background_img_frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_onboarding_background_img_frag, container, false);

        //Initialize the views
        onboardingImageView = view.findViewById(R.id.onboarding_img_view);
        onboardingFragCardTv1 = view.findViewById(R.id.onboardingFragCardTv1);
        onboardingFragSkipClickableTv = view.findViewById(R.id.onboardingFragSkipClickableTv);
        onboardingFragNextClickableTv = view.findViewById(R.id.onboardingFragNextClickableTv);
        onboardingFragScreenPositionImageView = view.findViewById(R.id.onboardingFragScreenPositionIV);
        
        //Set the initial content
        updateOnboardingContent();

        //Set click listeners for the skip and next button
        onboardingFragNextClickableTv.setOnClickListener(v -> nextOnBoardingStep());
        onboardingFragSkipClickableTv.setOnClickListener(v -> skipOnBoarding());


        return view;
    }

    private void nextOnBoardingStep() {
        if (currentStep < textSteps.length - 1) {
            // Move to the next step
            currentStep++;
            updateOnboardingContent();
        } else {
            // Optionally handle the case when the user reaches the last step
            // For example, finish the onboarding process or navigate to another activity
            Intent intent = new Intent(getActivity(), SignUp.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    private void skipOnBoarding() {
        Intent intent = new Intent(getActivity(), SignUp.class );
        startActivity(intent);

        getActivity().finish();
    }

    private void updateOnboardingContent() {
        onboardingImageView.setImageResource(bgImageResource[currentStep]);
        onboardingFragScreenPositionImageView.setImageResource(screenPositionImageResource[currentStep]);
        onboardingFragCardTv1.setText(getString(textSteps[currentStep]));
    }
}