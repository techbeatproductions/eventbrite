package com.example.eventbrite;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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

    private ImageButton backImageButton;
    private TextView screenName;
    private TextInputLayout userName;
    private TextInputLayout confirmPassword;
    private Button authenticationBtn;
    private TextView alreadyHaveAccTextView;
    private TextView signInClickable;
    private RadioButton rememberMeRadioButton;
    private TextView forgotPasswordClickable;
    private TextInputEditText userFullName;
    private TextInputEditText userEmail;
    private TextInputEditText userPassword;

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

        return inflater.inflate(R.layout.fragment_authentication, container, false);
    }

    // Define the interface
    public interface AuthenticationFragListener {
        void onFragmentViewCreated(AuthenticationFrag fragment);
    }

    private AuthenticationFragListener listener;

    // Set the listener
    public void setListener(AuthenticationFragListener listener) {
        this.listener = listener;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Initialize the views
        backImageButton = (ImageButton) view.findViewById(R.id.backImageBtn);
        screenName = (TextView) view.findViewById(R.id.screenNameTV);
        userName= (TextInputLayout) view.findViewById(R.id.fullNameTextInputLayout);
        confirmPassword = (TextInputLayout) view.findViewById(R.id.confirmPasswordTextInputLayout);
        authenticationBtn = (Button) view.findViewById(R.id.authenticateBtn);
        alreadyHaveAccTextView = (TextView) view.findViewById(R.id.alreadyHaveAnAccountTV);
        signInClickable = (TextView) view.findViewById(R.id.signInClickableTV);
        rememberMeRadioButton = (RadioButton) view.findViewById(R.id.rememberMeRadioBtn);
        forgotPasswordClickable = (TextView) view.findViewById(R.id.forgotPasswordClicakbleTV);
        userFullName = (TextInputEditText) view.findViewById(R.id.FullNameTextInputEditText);
        userEmail = (TextInputEditText) view.findViewById(R.id.EmailTextInputEditText);
        userPassword = (TextInputEditText) view.findViewById(R.id.PasswordTextInputEditText);


        // Notify listener that the fragment's view is ready
        if (listener != null) {
            listener.onFragmentViewCreated(this);
        }

        signInClickable.setOnClickListener(v -> navigateToSignIn());
        authenticationBtn.setOnClickListener(v -> authenticateUser());
        alreadyHaveAccTextView.setOnClickListener(v -> navigateToSignIn());
        backImageButton.setOnClickListener(v -> navigateToOnboardingScreens());


    }

    private void authenticateUser() {
        String extractedUserFullName;
        String extractedUserEmail;
        String extractedUserPassword;
        String btnText;

        extractedUserFullName = userFullName.getText().toString().trim();
        extractedUserEmail = userEmail.getText().toString().trim();
        extractedUserPassword = userPassword.getText().toString().trim();
        btnText= authenticationBtn.getText().toString();

        if (btnText.equals("Sign up")){
            ((SignUp) getActivity()).createUser(extractedUserEmail, extractedUserPassword, extractedUserFullName);

        }else if(btnText.equals("Sign in")){

            ((SignIn) getActivity()).loginUser(extractedUserEmail, extractedUserPassword);
        }
    }

    private void navigateToOnboardingScreens() {
        Intent intent = new Intent(getActivity(), Onboarding1.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void navigateToSignIn() {
        String btnText = authenticationBtn.getText().toString();
        Intent intent;

        if(btnText.equals(getString(R.string.sign_up))){
            intent = new Intent(getActivity(), SignIn.class);

        }else{
            intent = new Intent(getActivity(), SignUp.class);
        }
        startActivity(intent);
        getActivity().finish();

    }

    //Methods to control the views
    public void showViews(){
        backImageButton.setVisibility(View.GONE);
        userName.setVisibility(View.GONE);
        confirmPassword.setVisibility(View.GONE);
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