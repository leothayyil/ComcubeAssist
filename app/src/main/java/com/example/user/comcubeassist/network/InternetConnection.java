package com.example.user.comcubeassist.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by USER on 05-12-2017.
 */

public class InternetConnection {
    public static boolean checkConnection(Context context){
        final ConnectivityManager conMgr=(ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=conMgr.getActiveNetworkInfo();
        if (networkInfo != null) {

//            Toast.makeText(context, networkInfo.getTypeName()+" connected", Toast.LENGTH_SHORT).show();

            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {

                return true;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {

                return true;
            }
        }
        return false;
    }
}
