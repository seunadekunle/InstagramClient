package com.example.instagramclient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.example.instagramclient.databinding.ActivityLoginBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = LoginActivity.class.getSimpleName();

    private ActivityLoginBinding binding;

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ProgressBar loadingProgressBar;
    private TextView signUp;

    String username, password;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide action bar
//        getSupportActionBar().hide();

        // if user is logged in already go to login activity
        if (ParseUser.getCurrentUser() != null){
            goToMainActivity();
        }

        // inflating content view for particular activity
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // binds ui elements to variables
        usernameEditText = binding.username;
        passwordEditText = binding.password;
        loginButton = binding.login;
        loadingProgressBar = binding.loading;
        signUp = binding.signUp;

        // sets on click listener for button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // makes progress bar visible
                loadingProgressBar.setVisibility(View.VISIBLE);

                // gets username and password string and calls login function
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                loginUser(username, password);
                loadingProgressBar.setVisibility(View.INVISIBLE);
            }
        });

        // sets on click listener for sign up text
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignUpActivity();
            }
        });
    }

    // logs in the user using Parse
    private void loginUser(String username, String password) {
        Log.i(TAG, "username" + username);
        Log.i(TAG, "password" + password);
;        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    // TODO: better error handling
                    showLoginState(R.string.login_failed);
                    Log.e(TAG, "Issue with login", e);
                    return;
                }
                goToMainActivity();
            }
        });
    }

    private void goToMainActivity() {
        // goes to MainActivity
        // launches new intent and
        Intent toMain = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(toMain);
        finish();
    }

    // goes to SignUpActivity
    private void goToSignUpActivity() {
        // launches new intent
        Intent toSignUp = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(toSignUp);
    }

    // prints out toast that shows text parameter
    private void showLoginState(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

}