package com.app.popularmovies.utility;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.app.popularmovies.ApplicationController;


/**
 * Created by shubham on 30/9/15.
 * Class to manage network change, right now we are not using this class.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (com.app.popularmovies.utility.Utility.getNetworkState(context)) {
            ApplicationController.getApplicationInstance().setIsNetworkConnected(true);
            Lg.i("Network Receiver", "connected");
        } else {
            ApplicationController.getApplicationInstance().setIsNetworkConnected(false);
            Lg.i("Network Receiver", "disconnected");
        }
    }
}
