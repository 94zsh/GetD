package com.future.getd.net.help;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.util.Log;

/* loaded from: classes3.dex */
public class ConnectionStateMonitor extends ConnectivityManager.NetworkCallback {
    public static final String ACTION_NET_CHANGE = "com.watchfun.earjoy.net.change.action";
    private final String TAG;
    private NetworkRequest networkRequest;

    /* loaded from: classes3.dex */
    public static class ConnectionStateMonitorHolder {
        private static final ConnectionStateMonitor INSTANCE = new ConnectionStateMonitor();

        private ConnectionStateMonitorHolder() {
        }
    }

    public static ConnectionStateMonitor get() {
        return ConnectionStateMonitorHolder.INSTANCE;
    }

    public void disEnable(Context context) {
        ((ConnectivityManager) context.getSystemService("connectivity")).unregisterNetworkCallback(this);
        Log.i("ConnectionStateMonitor", "disEnable:call..");
    }

    public void enable(Context context) {
        try {
            ((ConnectivityManager) context.getSystemService("connectivity")).registerNetworkCallback(this.networkRequest, this);
            Log.i("ConnectionStateMonitor", "enable:register ok");
        } catch (Exception e) {
            Log.e("ConnectionStateMonitor", "enable:register error:" + e.getMessage());
        }
    }

    @Override // android.net.ConnectivityManager.NetworkCallback
    public void onAvailable(Network network) {
        super.onAvailable(network);
        Log.i("ConnectionStateMonitor", "onAvailable:network connection!");
//        Future3DApplication.getInstance().sendBroadcast(new Intent(ACTION_NET_CHANGE));
    }

    @Override // android.net.ConnectivityManager.NetworkCallback
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
    }

    @Override // android.net.ConnectivityManager.NetworkCallback
    public void onLost(Network network) {
        super.onLost(network);
        Log.i("ConnectionStateMonitor", "onLost:Network disconnection!");
    }

    private ConnectionStateMonitor() {
        this.TAG = "ConnectionStateMonitor";
        this.networkRequest = null;
        try {
            this.networkRequest = new NetworkRequest.Builder().addCapability(13).addTransportType(0).addTransportType(1).build();
            Log.i("ConnectionStateMonitor", "ConnectionStateMonitor:build ok!");
        } catch (Exception e) {
            Log.e("ConnectionStateMonitor", "ConnectionStateMonitor:error:" + e.getMessage());
        }
    }
}