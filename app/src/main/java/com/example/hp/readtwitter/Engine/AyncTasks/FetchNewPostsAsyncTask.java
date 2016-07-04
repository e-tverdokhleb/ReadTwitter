package com.example.hp.readtwitter.Engine.AyncTasks;

import android.os.AsyncTask;

import com.example.hp.readtwitter.Engine.MessageEvent;
import com.example.hp.readtwitter.Engine.ResponseCode;
import com.example.hp.readtwitter.Engine.Service;
import com.example.hp.readtwitter.MainActivity;
import com.example.hp.readtwitter.TwitterServiceClass.TwitterPost;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;

public class FetchNewPostsAsyncTask extends AsyncTask<Call, Void, List<TwitterPost>> {
    @Override
    protected void onPreExecute() {
        Service.isFetchNewPostsAsyncTaskExecute = true;
        if (!Service.isConnection(MainActivity.getMainActivityInstance())) {
            EventBus.getDefault().post(new MessageEvent(ResponseCode.NO_NETWORK_CONNECTION));
            return;
        }
    }

    @Override
    protected List<TwitterPost> doInBackground(Call... params) {
        try {
            retrofit2.Response<List<TwitterPost>> response = params[0].execute();
            if (response.code() == 200) {
                return response.body();
            } else return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<TwitterPost> result) {
        Service.isFetchNewPostsAsyncTaskExecute = false;
        if (result != null) {
            if (!result.isEmpty()) {
                MessageEvent newPostsRecivedData = new MessageEvent(ResponseCode.NEW_POSTS_RECIVED);
                newPostsRecivedData.setNewPosts(result);
                EventBus.getDefault().post(newPostsRecivedData);
            } else {
                EventBus.getDefault().post(new MessageEvent(ResponseCode.NEW_POSTS_NO_PRESENT));
            }
        } else {
            EventBus.getDefault().post(new MessageEvent(ResponseCode.FIRST_DATA_CANNOT_FETCH));
        }
    }
}
