package com.example.instagramclient.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagramclient.R;
import com.example.instagramclient.classes.Post;
import com.example.instagramclient.fragments.DetailFragment;
import com.example.instagramclient.fragments.FeedFragment;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    public Context context;
    public List<Post> posts;
    public FragmentManager fragmentManager;

    // default constructor
    public PostAdapter(Context context, List<Post> posts, FragmentManager fragmentManager) {
        this.context = context;
        this.posts = posts;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @NotNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        // use LayoutInflater to pass ui arrangement we want and wrap it in a ViewHolder
        View view = LayoutInflater.from(context).inflate(R.layout.feed_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PostAdapter.ViewHolder holder, int position) {
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
        private ImageView ivThumb;
        private TextView tvUsername;
        private TextView tvDesc;
        private TextView tvStamp;
        private TextView tvLikes;
        private ImageButton likeBtn;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            // connect ui elements to data variables
            parent = itemView.findViewById(R.id.parentLayout);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvStamp = itemView.findViewById(R.id.tvStamp);
            ivImg = itemView.findViewById(R.id.ivImg);
            ivThumb = itemView.findViewById(R.id.ivThumb);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            likeBtn = itemView.findViewById(R.id.likeBtn);
        }

        public void bind(Post post) {

            // bind post data to ui elements
            tvUsername.setText(post.getUser().getUsername());
            tvDesc.setText(post.getDesc());
            tvStamp.setText(post.getTimeStamp());
            tvLikes.setText(String.format("%d", post.getLikes()));
            ParseFile img = post.getImage();

            // handles if image gotten from database
            if (img != null)
                Glide.with(context).load(img.getUrl()).into(ivImg);
            else
                ivImg.setVisibility(View.INVISIBLE);

            // loads user profile image on timeline
            ParseFile profileImg = post.getUser().getParseFile("profileImg");
            // handles if image gotten from database
            if (profileImg != null)
                Glide.with(context).load(profileImg.getUrl()).fitCenter().transform(new RoundedCornersTransformation(50, 0)).override(100,50).into(ivThumb);
            else
                ivThumb.setVisibility(View.INVISIBLE);

            // handles like button onClick
            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean liked = post.isLiked(ParseUser.getCurrentUser());

                    // sets the like count based on if the button is clicked
                    if (liked) {
                        if (post.getLikes() > 0){
                            post.setLikes(post.getLikes() - 1);
                            post.removeUser(ParseUser.getCurrentUser());
                        }
                    }
                    else {
                        post.setLikes(post.getLikes() + 1);
                        post.addUser(ParseUser.getCurrentUser());
                    }

                    // save it in background and change button state and text state
                    post.saveInBackground();
                    tvLikes.setText(String.format("%d", post.getLikes()));
                    updateLikeBtn(likeBtn, post, ParseUser.getCurrentUser());
                }
            });

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

            // update like button state
            updateLikeBtn(likeBtn, post, ParseUser.getCurrentUser());
        }

        // updates the like button based on if the user like the post
        private void updateLikeBtn(ImageButton btn, Post post, ParseUser user){
            btn.setSelected(post.isLiked(user));
        }
    }
}
