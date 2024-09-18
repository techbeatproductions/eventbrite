package com.example.eventbrite;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AuthenticationFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AuthenticationFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View backImageButton;
    private TextView screenName;
    private View userName;
    private View confirmPassword;
    private Button authenticationBtn;
    private TextView alreadyHaveAccTextView;
    private TextView signInClickable;
    private View rememberMeRadioButton;
    private TextView forgotPasswordClickable;

    public AuthenticationFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AuthenticationFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static AuthenticationFrag newInstance(String param1, String param2) {
        AuthenticationFrag fragment = new AuthenticationFrag();
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

        View view = inflater.inflate(R.layout.fragment_authentication,container, false);

        //Initialize the views
        backImageButton =  view.findViewById(R.id.backImageBtn);
        screenName = (TextView) view.findViewById(R.id.screenNameTV);
        userName= view.findViewById(R.id.fullNameTextInputLayout);
        confirmPassword = view.findViewById(R.id.confirmPasswordTextInputLayout);
        authenticationBtn = (Button) view.findViewById(R.id.authenticateBtn);
        alreadyHaveAccTextView = (TextView) view.findViewById(R.id.alreadyHaveAnAccountTV);
        signInClickable = (TextView) view.findViewById(R.id.signInClickableTV);
        rememberMeRadioButton = view.findViewById(R.id.rememberMeRadioBtn);
        forgotPasswordClickable = (TextView) view.findViewById(R.id.forgotPasswordClicakbleTV);

        signInClickable.setOnClickListener(v -> navigateToSignIn());
        authenticationBtn.setOnClickListener(v -> navigateToSignIn());
        alreadyHaveAccTextView.setOnClickListener(v -> navigateToSignIn());
        backImageButton.setOnClickListener(v -> navigateToOnboardingScreens());


        return view;
    }

    private void navigateToOnboardingScreens() {
        Intent intent = new Intent(getActivity(), Onboarding1.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void navigateToSignIn() {
        Intent intent = new Intent(getActivity(), SignIn.class);
        startActivity(intent);
        getActivity().finish();
    }

    //Methods to control the views
    public void showViews(){
        backImageButton.setVisibility(View.INVISIBLE);
        userName.setVisibility(View.INVISIBLE);
        confirmPassword.setVisibility(View.INVISIBLE);
        forgotPasswordClickable.setVisibility(View.VISIBLE);
        rememberMeRadioButton.setVisibility(View.VISIBLE);
    }

    public void setStrings(int screenNameResId,int alreadyHaveAccResId, int signInClickableResId , int authenticationBtnResId){
        screenName.setText(getString(screenNameResId));
        alreadyHaveAccTextView.setText(getString(alreadyHaveAccResId));
        signInClickable.setText(getString(signInClickableResId));
        authenticationBtn.setText(getString(authenticationBtnResId));


    }


}