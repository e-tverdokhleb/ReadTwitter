package com.example.hp.readtwitter.Engine;

import android.os.AsyncTask;
import android.widget.Toast;

import com.example.hp.readtwitter.MainActivity;
import com.example.hp.readtwitter.TwitterServiceClass.TwitterPost;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;

public class AsyncTasks {
    static class AuthNetworkCall extends AsyncTask<Call, Void, String> {
        @Override
        protected void onPreExecute() {
            if (!Service.isConnection(MainActivity.getMainActivityInstance())) {
                EventBus.getDefault().post(new MessageEvent(MessageEvent.ResponseCode.NO_NETWORK_CONNECTION));
                return;
            }
        }

        @Override
        protected String doInBackground(Call... params) {
            try {
                retrofit2.Response response = params[0].execute();
                if (response.code() == 200) {
                    String result = response.body().toString();
                    String token = result.substring(result.indexOf(" ") + 1);
                    TwitterConnector.authorizationHeader = "Bearer " + token;
                    return TwitterConnector.authorizationHeader;
                }
                TwitterConnector.authorizationHeader = "";
                return "AUTHORIZATION_ERROR";
            } catch (IOException e) {
                e.printStackTrace();
            }
            TwitterConnector.authorizationHeader = "";
            return "AUTHORIZATION_ERROR";
        }

        @Override
        protected void onPostExecute(final String result) {
            if (result == "AUTHORIZATION_ERROR") {
                EventBus.getDefault().post(new MessageEvent(MessageEvent.ResponseCode.AUTHORIZATION_ERROR));
                return;
            } else {
                EventBus.getDefault().post(new MessageEvent(MessageEvent.ResponseCode.AUTHORIZATION_PASSED));
                TwitterConnector.getMessages();
            }
        }

    }

    static class Stream extends AsyncTask<Call, Void, List<TwitterPost>> {
        @Override
        protected void onPreExecute() {
            if (!Service.isConnection(MainActivity.getMainActivityInstance())) {
                EventBus.getDefault().post(new MessageEvent(MessageEvent.ResponseCode.NO_NETWORK_CONNECTION));
                return;
            }
            Toast.makeText(MainActivity.getMainActivityInstance(), "fetching data...", Toast.LENGTH_SHORT).show();
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
            if (result != null) {
                EventBus.getDefault().post(new MessageEvent(MessageEvent.ResponseCode.DATA_RECIVED, result));
            } else {
                EventBus.getDefault().post(new MessageEvent(MessageEvent.ResponseCode.CANNOT_FETCH_DATA));
            }
        }
    }

}
