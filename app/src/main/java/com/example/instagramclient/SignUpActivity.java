package com.example.instagramclient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.example.instagramclient.databinding.ActivitySignUpBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = SignUpActivity.class.getSimpleName();

    private ActivitySignUpBinding binding;

    EditText usernameEditText;
    EditText passwordEditText;
    Button signUpButton;
    ProgressBar loadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // creates binding variable from xml file and sets content view
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // hide action bar
        getSupportActionBar().hide();

        // bind ui elements to variables
        usernameEditText = binding.username;
        passwordEditText = binding.password;
        signUpButton = binding.signup;
        loadingProgressBar = binding.loading;


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                signUpUser(username, password);
                loadingProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void signUpUser(String username, String password) {
        // if username or password is empty show error meessage
        if (username.matches("") && password.matches(""))
            showSignUpState(R.string.empty_fields);
        else if(username.matches(""))
            showSignUpState(R.string.invalid_username);
        else if(username.matches(""))
            showSignUpState(R.string.invalid_password);
        else{
            // create a new parse user
            ParseUser user =  new ParseUser();
            user.setUsername(username);
            user.setPassword(password);

            // signs the user up in background
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        // goes to main activity
                        Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        // Sign up didn't succeed. stay on page and show message
                        showSignUpState(R.string.sign_up_failed);
                        Log.e(TAG, "Issue with signup", e);
                    }
                }
            });
        }
    }

    private void showSignUpState(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}