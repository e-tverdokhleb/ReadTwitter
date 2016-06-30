package com.example.hp.readtwitter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.hp.readtwitter.Engine.UserData;
import com.example.hp.readtwitter.TwitterServiceClass.TwitterPost;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static MainActivity instance;

    private EventBus bus = EventBus.getDefault();

    RecyclerView recyclerView;
    TwitterPostsAdapter twitterPostsAdapter;
    List<TwitterPost> twitterPosts;
    SwipeRefreshLayout swipeRefreshLayout;
    TwitterConnector getTwitterMessages;

    Handler handler = new Handler();
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    public static MainActivity getMainActivityInstance() {
        return instance;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent event) {
        swipeRefreshLayout.setRefreshing(false);
        Log.d(UserData.TAG, "event become!");
        twitterPostsAdapter = new TwitterPostsAdapter(event.getTwitterPosts());
        recyclerView.setAdapter(twitterPostsAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
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

        EventBus.getDefault().register(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Service.isConnection(getMainActivityInstance())) {
                   // swipeRefreshLayout.setRefreshing(true);
                    getTwitterMessages.getMessages();
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
                    getTwitterMessages.getMessages();
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


