package com.example.minageorge.booklisting.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by mina george on 24-Apr-17.
 */

public class CheckConnection {
    private Context context;

    public CheckConnection(Context context) {
        this.context = context;
    }

    public boolean isconnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                return true;
            }
        }
        return false;
    }
}
