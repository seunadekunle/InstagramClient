package com.example.instagramclient.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.instagramclient.activities.MainActivity;
import com.example.instagramclient.R;
import com.example.instagramclient.classes.Post;
import com.parse.ParseFile;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    private ImageView ivImg;
    private TextView tvUsername;
    private TextView tvDesc;
    private TextView likeCount;
    private TextView tvStamp;
    private Toolbar detailBar;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private Post post;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment DetailFragment.
     */
    public static DetailFragment newInstance(Serializable param1) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            post = (Post) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // changes global toolbar
        detailBar = getActivity().findViewById(R.id.toolbar_detail);
        getActivity().findViewById(R.id.toolbar_main).setVisibility(View.INVISIBLE);
        detailBar.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).setSupportActionBar(detailBar);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        // bind post data to ui elements
        tvUsername = view.findViewById(R.id.tvUsername);
        tvDesc = view.findViewById(R.id.tvDesc);
        tvStamp = view.findViewById(R.id.tvStamp);
        likeCount = view.findViewById(R.id.likeCount);
        ivImg = view.findViewById(R.id.ivImg);

        // updates user elements
        tvUsername.setText(post.getUser().getUsername());
        tvDesc.setText(post.getDesc());
        tvStamp.setText(post.getTimeStamp());

        // check fo
        String likeText = "like";

        if (post.getLikes() != 1)
            likeText += "s";

        likeCount.setText(String.format("%d %s", post.getLikes(), likeText));
        ParseFile img = post.getImage();

        if (img != null)
            Glide.with(getContext()).load(img.getUrl()).into(ivImg);
        else
            ivImg.setVisibility(View.INVISIBLE);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }
}