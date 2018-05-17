package com.example.hole1.mynewsagg.util;

import android.content.Context;
import android.net.ConnectivityManager;

import com.example.hole1.mynewsagg.MyTimesApplication;


public class UtilityMethods {
    /**
     * Method to detect network connection on the device
     */
    public static boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) MyTimesApplication.getMyTimesApplicationInstance()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
