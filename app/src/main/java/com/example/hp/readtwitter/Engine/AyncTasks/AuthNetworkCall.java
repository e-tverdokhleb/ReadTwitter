package com.example.hp.readtwitter.Engine.AyncTasks;

import android.os.AsyncTask;

import com.example.hp.readtwitter.Engine.MessageEvent;
import com.example.hp.readtwitter.Engine.ResponseCode;
import com.example.hp.readtwitter.Engine.Service;
import com.example.hp.readtwitter.Engine.TwitterConnector;
import com.example.hp.readtwitter.MainActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import retrofit2.Call;

public class AuthNetworkCall extends AsyncTask<Call, Void, String> {
    @Override
    protected void onPreExecute() {
        if (!Service.isConnection(MainActivity.getMainActivityInstance())) {
            EventBus.getDefault().post(new MessageEvent(ResponseCode.NO_NETWORK_CONNECTION));
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
            EventBus.getDefault().post(new MessageEvent(ResponseCode.AUTHORIZATION_ERROR));
            return;
        } else {
            EventBus.getDefault().post(new MessageEvent(ResponseCode.AUTHORIZATION_PASSED));
            TwitterConnector.getMessages();
        }
    }

}
