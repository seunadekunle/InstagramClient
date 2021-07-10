package com.example.instagramclient.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.instagramclient.R;
import com.example.instagramclient.activities.LoginActivity;
import com.example.instagramclient.adapters.ProfilePostAdapter;
import com.example.instagramclient.classes.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends FeedFragment {

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 65;

    protected TextView tvName;
    protected Button logOutBtn;
    protected Button profileImgBtn;
    protected ImageView ivImg;
    protected ProfilePostAdapter adapter;

    private File photoFile;
    public String photoFileName = "profilePhoto.jpg";


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // binds variables to ui elements
        tvName = view.findViewById(R.id.tvName);
        logOutBtn = view.findViewById(R.id.logOutBtn);
        profileImgBtn = view.findViewById(R.id.profileImgBtn);
        ivImg = view.findViewById(R.id.profileImg);

        // fill out user elements
        tvName.setText(ParseUser.getCurrentUser().getUsername());
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutUser();
            }
        });

        ParseFile profileImg = ParseUser.getCurrentUser().getParseFile("profileImg");
        if (profileImg != null)
            Glide.with(getContext()).load(profileImg.getUrl()).centerCrop().transform(new RoundedCornersTransformation(300, 0)).override(200,100).into(ivImg);
        else
            ivImg.setVisibility(View.INVISIBLE);

        // sets on click listener for profile photo
        profileImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create Intent to take a picture and return control to the calling application
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Create a File reference for future access
                photoFile = getPhotoFileUri(photoFileName);

                // wrap File object into a content provider
                // required for API >= 24
                // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
                Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.instagramclient.fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

                // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
                // So as long as the result is not null, it's safe to use the intent.
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    // Start the image capture intent to take photo
                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                }

                // get current user and save post
                ParseUser currentUser = ParseUser.getCurrentUser();
                currentUser.put("profileImg", new ParseFile(photoFile));

                currentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error while saving " + e.getMessage());
                            return;
                        }

                        Log.i(TAG, "Post save was successful");

                        // updates profile photo
                        ParseFile profileImg = ParseUser.getCurrentUser().getParseFile("profileImg");
                        if (profileImg != null)
                            Glide.with(getContext()).load(profileImg.getUrl()).centerCrop().transform(new RoundedCornersTransformation(300, 0)).override(200,100).into(ivImg);
                        else
                            ivImg.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        adapter = new ProfilePostAdapter(getContext(), allPosts, getActivity().getSupportFragmentManager());
        rvPosts.setAdapter(adapter);
        // Attach the layout manager to the recycler view
        rvPosts.setLayoutManager(gridLayoutManager);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    protected void queryPosts() {
        // specify what type of data we want to query - Post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include data referred by user key
        query.include(Post.getKeyUser());
        // limit query to latest 20 items
        query.setLimit(20);
        // get posts that were created by the user
        query.whereEqualTo(Post.getKeyUser(), ParseUser.getCurrentUser());
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                // for debugging purposes let's print every post description to logcat
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDesc() + ", username: " + post.getUser().getUsername());
                }

                // clears the adapter
                adapter.clear();
                // save received posts to list and notify adapter of new data
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();

                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    // log out the user
    private void logOutUser() {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null

        // go to login activity
        Intent toLogin = new Intent(getActivity(), LoginActivity.class);
        startActivity(toLogin);
    }
}