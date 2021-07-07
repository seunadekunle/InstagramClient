package com.example.instagramclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.instagramclient.databinding.ActivityMainBinding;
import com.example.instagramclient.fragments.ComposeFragment;
import com.example.instagramclient.fragments.FeedFragment;
import com.example.instagramclient.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 45;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    public ActivityMainBinding binding;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // creates binding variable from xml file and sets content view
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // get bottom navigation view ui element and changes it
        bottomNavigationView = binding.bottomNavigation;
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.home_page:
                        fragment = new FeedFragment();
                        break;
                    case R.id.post_page:
                        fragment = new ComposeFragment();
                        break;
                    case R.id.profile_page:
                        fragment = new ProfileFragment();
                        break;
                    default:
                        fragment = new ComposeFragment();
                        break;

                }

                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;

            }
        });

        bottomNavigationView.setSelectedItemId(R.id.home_page);
//
//        logOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                logOutUser();
//            }
//        });
//
//        feedBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent toFeed = new Intent(MainActivity.this, FeedActivity.class);
//                startActivity(toFeed);
//            }
//        });

    }

    // log out the user
    private void logOutUser() {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null

        // go to login activity
        Intent toLogin = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(toLogin);
    }

}