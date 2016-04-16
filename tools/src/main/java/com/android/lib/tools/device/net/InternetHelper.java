package com.android.lib.tools.device.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.android.lib.tools.log.MyLog;

/**
 * Created by liutiantian on 2016-03-19.
 */
public class InternetHelper {
    private static final String TAG = "InternetHelper";

    public static final int TYPE_ACTIVE = -1;

    private static InternetHelper mInstance;

    public static InternetHelper getInstance() {
        if (mInstance == null) {
            synchronized (InternetHelper.class) {
                if (mInstance == null) {
                    mInstance = new InternetHelper();
                }
            }
        }
        return mInstance;
    }

    private WifiManager wifiManager;
    private ConnectivityManager connectivityManager;

    private InternetHelper() {

    }

    public boolean isWifiConnected(Context context) {
        return isNetworkConnected(context, ConnectivityManager.TYPE_WIFI);
    }

    public boolean isWiredConnected(Context context) {
        return isNetworkConnected(context, ConnectivityManager.TYPE_ETHERNET);
    }

    public boolean isNetworkConnected(Context context, int networkType) {
        ConnectivityManager manager = InternetHelper.getInstance().getConnectivityManager(context);
        if(manager == null) {
            MyLog.i(TAG, "isNetworkConnected", "connectivity manager is null, false returned.");
            return false;
        }

        NetworkInfo networkInfo = networkType == InternetHelper.TYPE_ACTIVE ? manager.getActiveNetworkInfo() : manager.getNetworkInfo(networkType);
        if(networkInfo == null) {
            MyLog.i(TAG, "isNetworkConnected", "network info for type: " + networkType + " is null, false returned.");
            return false;
        }
        MyLog.i(TAG, "isNetworkConnected", "network info connected checked for type: " + networkType + " is " + networkInfo.isConnected());
        return networkInfo.isConnected();
    }

    public String getWifiMacAddress(Context context) {
        if (context != null) {
            WifiManager manager = getWifiManager(context);
            if (manager != null) {
                WifiInfo wifiInfo = manager.getConnectionInfo();
                if (wifiInfo != null) {
                    return wifiInfo.getMacAddress();
                }
            }
        }
        return null;
    }

    /**
     * this method can not guarantee wifiManager is not null
     * @param context
     * @return
     */
    public WifiManager getWifiManager(Context context) {
        if(this.wifiManager != null) {
            return this.wifiManager;
        }
        if(context == null) {
            MyLog.i(TAG, "getWifiManager", "context is null, null returned.");
            return null;
        }
        this.wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if(this.wifiManager == null) {
            MyLog.i(TAG, "getWifiManager", "manager is null, null returned.");
            return null;
        }
        return this.wifiManager;
    }

    public ConnectivityManager getConnectivityManager(Context context) {
        if(this.connectivityManager != null) {
            return this.connectivityManager;
        }
        if(context == null) {
            MyLog.i(TAG, "getConnectivityManager", "context is null, null returned.");
            return null;
        }
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(this.connectivityManager == null) {
            MyLog.i(TAG, "getConnectivityManager", "manager is null, null returned.");
            return null;
        }
        return this.connectivityManager;
    }
}
