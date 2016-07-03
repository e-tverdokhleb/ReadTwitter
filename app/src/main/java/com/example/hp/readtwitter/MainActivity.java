package com.example.hp.readtwitter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.hp.readtwitter.Engine.MessageEvent;
import com.example.hp.readtwitter.Engine.Service;
import com.example.hp.readtwitter.Engine.TwitterConnector;
import com.example.hp.readtwitter.Engine.TwitterPostsAdapter;
import com.example.hp.readtwitter.TwitterServiceClass.TwitterPost;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static MainActivity instance;

    public static boolean isLoading = false;

    private EventBus bus = EventBus.getDefault();
    RecyclerView recyclerView;
    TwitterPostsAdapter twitterPostsAdapter;
    List<TwitterPost> twitterPosts;
    SwipeRefreshLayout swipeRefreshLayout;
    TwitterConnector getTwitterMessages;

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
            case AUTHORIZATION_ERROR:
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(this, "authorization error code - " + event.getErrorMessageCode().substring(3), Toast.LENGTH_SHORT).show();
                break;
            case AUTHORIZATION_PASSED:
                Toast.makeText(this, "authorization passed", Toast.LENGTH_SHORT).show();
                TwitterConnector.getMessages();
                break;
            case FETCHING_DATA:
                Toast.makeText(MainActivity.getMainActivityInstance(), "fetching data...", Toast.LENGTH_SHORT).show();
                break;
            case DATA_RECIVED:
                swipeRefreshLayout.setRefreshing(false);
                twitterPostsAdapter = new TwitterPostsAdapter(event.getTwitterPosts());
                recyclerView.setAdapter(twitterPostsAdapter);
                Toast.makeText(this, "data recived", Toast.LENGTH_SHORT).show();
                break;
            case CANNOT_FETCH_DATA:
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(this, "cannot fetch data", Toast.LENGTH_SHORT).show();
                break;
            case NO_NETWORK_CONNECTION:
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(this, "check Network connection", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        EventBus.getDefault().register(this);
        getTwitterMessages = new TwitterConnector();

        mContext = getApplicationContext();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        twitterPosts = new ArrayList<TwitterPost>();
        twitterPostsAdapter = new TwitterPostsAdapter(twitterPosts);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(twitterPostsAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(Service.TAG, String.valueOf(recyclerView.getAdapter().getItemCount()));
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Service.isConnection(getMainActivityInstance())) {
                    getTwitterMessages.run();
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
                    getTwitterMessages.run();
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


