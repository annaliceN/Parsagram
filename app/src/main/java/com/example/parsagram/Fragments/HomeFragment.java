package com.example.parsagram.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.parsagram.Model.Post;
import com.example.parsagram.PostAdapter;
import com.example.parsagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public final static String TAG = "HomeFragment";

    public RecyclerView rvPosts;
    protected List<Post> mPosts;
    protected PostAdapter postAdapter;

    private SwipeRefreshLayout swipeContainer;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPosts = view.findViewById(R.id.rvPosts);
        mPosts = new ArrayList<>();
        // create the adapter
        postAdapter = new PostAdapter(getContext(), mPosts);
        // set the adapter
        rvPosts.setAdapter(postAdapter);
        // set the layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(linearLayoutManager);

        queryPosts();

        // Refresh timeline
        // Lookup the swipe container view
        swipeContainer = view.findViewById(R.id.swipe_refresh_layout);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                fetchTimelineAsync();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    public void fetchTimelineAsync() {
        final Post.Query postQuery = new Post.Query();
        postQuery.orderByDescending("updatedAt");
        postQuery.getTop().withUser();
        postQuery.setLimit(20);
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    postAdapter.clear();
                    for (int i = 0; i < objects.size(); i++) {
                        Log.d("MainActivity", "Post[" + i + "]=" + objects.get(i).getDescription() + "\nusername = " + objects.get(i).getUser().getUsername());
                        Post post = objects.get(i);
                        mPosts.add(post);
                        postAdapter.notifyItemInserted(0);
                    }
                } else {
                    e.printStackTrace();
                }
                swipeContainer.setRefreshing(false);
            }
        });
    }

    private void queryPosts() {
        final ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);
        //final Post.Query postQuery = new Post.Query();
        postQuery.include(Post.KEY_USER);
        postQuery.setLimit(20);
        postQuery.addDescendingOrder(Post.KEY_CREATED_AT);
        // postQuery.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error with query");
                    e.printStackTrace();
                    return;
                }
                mPosts.addAll(posts);
                postAdapter.notifyDataSetChanged();
                rvPosts.scrollToPosition(0);
                swipeContainer.setRefreshing(false);
                for (int i = 0; i < posts.size(); i++) {
                    Log.d(TAG, "Post: " + posts.get(i).getDescription() + "username: " + posts.get(i).getUser().getUsername());
                }
            }
        });
    }
}
