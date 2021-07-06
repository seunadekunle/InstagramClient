package com.example.instagramclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.instagramclient.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding binding;
    private FloatingActionButton logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // creates binding variable from xml file and sets content view
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // binds ui to variables
        logOut = binding.logOut;

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutUser();
            }
        });
    }

    private void logOutUser() {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
        Intent toLogin = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(toLogin);
    }
}