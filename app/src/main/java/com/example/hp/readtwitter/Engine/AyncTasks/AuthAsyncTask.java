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

public class AuthAsyncTask extends AsyncTask<Call, Void, String> {
    @Override
    protected void onPreExecute() {
        if (!Service.isConnection(MainActivity.getMainActivityInstance())) {
            EventBus.getDefault().post(new MessageEvent(ResponseCode.NO_NETWORK_CONNECTION));
            return;
        }
    }

    @Override
    protected String doInBackground(Call... params) {
        retrofit2.Response response = null;
        try {
            response = params[0].execute();
            if (response.code() == 200) {
                String result = response.body().toString();
                String token = result.substring(result.indexOf(" ") + 1);
                TwitterConnector.authorizationHeader = "Bearer " + token;
                return TwitterConnector.authorizationHeader;
            }
            TwitterConnector.authorizationHeader = "";
            if (response != null) {
                return (String.valueOf(response.code()) + "_AUTHORIZATION_ERROR");
            } else return ("AUTHORIZATION_ERROR");

        } catch (IOException e) {
            e.printStackTrace();
        }
        TwitterConnector.authorizationHeader = "";
        if (response != null) {
            return (String.valueOf(response.code()) + "_AUTHORIZATION_ERROR");
        } else return ("AUTHORIZATION_ERROR");
    }

    @Override
    protected void onPostExecute(final String result) {
        if (result.contains("AUTHORIZATION_ERROR")) {
            EventBus.getDefault().post(new MessageEvent(ResponseCode.AUTHORIZATION_ERROR));
        } else {
            EventBus.getDefault().post(new MessageEvent(ResponseCode.AUTHORIZATION_PASSED));
        }
    }

}
