package com.example.instagramclient.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagramclient.R;
import com.example.instagramclient.classes.Post;
import com.example.instagramclient.fragments.DetailFragment;
import com.parse.ParseFile;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.ViewHolder> {

    public Context context;
    public List<Post> posts;
    public FragmentManager fragmentManager;

    // default constructor
    public ProfilePostAdapter(Context context, List<Post> posts, FragmentManager fragmentManager) {
        this.context = context;
        this.posts = posts;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        // use LayoutInflater to pass ui arrangement we want and wrap it in a ViewHolder
        View view = LayoutInflater.from(context).inflate(R.layout.profile_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }
    
    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return posts.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout parent;
        private ImageView ivImg;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parentLayout);
            ivImg = itemView.findViewById(R.id.ivImg);
        }


        public void bind(Post post) {

            ParseFile img = post.getImage();
            // handles if image gotten from database
            if (img != null)
                Glide.with(context).load(img.getUrl()).into(ivImg);
            else
                ivImg.setVisibility(View.INVISIBLE);

            // set cn click listener if the item clicked
            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get current position
                    int position = getAdapterPosition();

                    Log.i("adapter", "position: " + String.valueOf(position));
                    // if position is valid
                    if (position != RecyclerView.NO_POSITION) {
                        Post selectedPost = posts.get(position);

                        // Create the transaction
                        FragmentTransaction fts = fragmentManager.beginTransaction();
                        // Replace the content of the container
                        fts.replace(R.id.flContainer, DetailFragment.newInstance(selectedPost));
                        // Append this transaction to the backstack
                        fts.addToBackStack("DetailFragment");
                        // Commit the changes
                        fts.commit();
                    }
                }
            });
        }
    }


}
