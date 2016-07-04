package com.example.hp.readtwitter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.hp.readtwitter.Engine.MessageEvent;
import com.example.hp.readtwitter.Engine.ResponseCode;
import com.example.hp.readtwitter.Engine.Service;
import com.example.hp.readtwitter.Engine.TwitterConnector;
import com.example.hp.readtwitter.Engine.TwitterPostsAdapter;
import com.example.hp.readtwitter.TwitterServiceClass.TwitterPost;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static MainActivity instance;

    RecyclerView recyclerView;
    public static TwitterPostsAdapter twitterPostsAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    TwitterConnector twitterConnector;

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    public static MainActivity getMainActivityInstance() {
        return instance;
    }

    @Subscribe
    public void onEvent(MessageEvent event) {
        switch (event.getResponseCode()) {
            case NO_NETWORK_CONNECTION:
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(this, "check Network connection", Toast.LENGTH_SHORT).show();
                break;

            case CANNOT_FETCH_DATA:
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(this, "cannot fetch data", Toast.LENGTH_SHORT).show();
                break;

            case AUTHORIZATION_ERROR:
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(this, "authorization error code - " + event.getErrorMessageCode().substring(3), Toast.LENGTH_SHORT).show();
                //403 Forbidden, 401 Unauthorized
                break;


            case AUTHORIZATION_PASSED:
                Toast.makeText(this, "authorization passed", Toast.LENGTH_SHORT).show();
                twitterConnector.loadPosts();
                break;


            case FETCHING_DATA:
                break;

            case DATA_RECIVED:
                swipeRefreshLayout.setRefreshing(false);
                twitterPostsAdapter.updateData(event.getTwitterPosts());
                twitterPostsAdapter.notifyDataSetChanged();
                recyclerView.refreshDrawableState();
                break;

            case NEW_POSTS_RECIVED:
                swipeRefreshLayout.setRefreshing(false);
                twitterPostsAdapter.addAll(0, event.getNewPosts());
                twitterPostsAdapter.notifyDataSetChanged();
                recyclerView.refreshDrawableState();
                break;

            case NEW_POSTS_NO_RECIVED:
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(this, "no new posts", Toast.LENGTH_SHORT).show();
                break;


            case FETCH_PREV_POSTS:
                twitterConnector.fetchPreviouslyPosts();
                break;

            case PREV_POSTS_RECIVED:
                twitterPostsAdapter.addAll(event.getPrevPosts());
                twitterPostsAdapter.notifyDataSetChanged();
                recyclerView.refreshDrawableState();
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        EventBus.getDefault().register(this);
        twitterConnector = new TwitterConnector();

        mContext = getApplicationContext();

        Service.isFetchPrevPostsAsyncTaskExecute = false;
        Service.isFetchNewPostsAsyncTaskExecute = false;
        Service.isDataLoading = false; // - TODO

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        twitterPostsAdapter = new TwitterPostsAdapter(new ArrayList<TwitterPost>());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(twitterPostsAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    EventBus.getDefault().post(new MessageEvent(ResponseCode.FETCH_PREV_POSTS));
                }
            }

            public void onLoadMore(int current_page) {
                // do something...
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Service.isConnection(getMainActivityInstance())) {
                    twitterConnector.loadPosts();
                } else {
                    Toast.makeText(getContext(), "No network conntection", Toast.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        if (Service.isConnection(getMainActivityInstance())) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    twitterConnector.loadPosts();
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
        } else {
            Toast.makeText(this, "No network conntection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}


