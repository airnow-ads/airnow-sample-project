package airnow.demo.ads;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnectivityManager {
    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE = 2;
    public static final int TYPE_NOT_CONNECTED = 0;
    private BroadcastReceiver networkChangeReceiver;
    private Runnable onAvailable;
    private boolean isReceiverRegistred = false;

    public NetworkConnectivityManager() {
        this.networkChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int networkState = getConnectivityStatus(context);
                if (networkState != TYPE_NOT_CONNECTED) {
                    onAvailable.run();
                }
            }
        };
    }

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }

        return TYPE_NOT_CONNECTED;
    }

    public void subscribeOnConnectionAvailable(Context context, Runnable onAvailable) {
        this.onAvailable = onAvailable;
        context.registerReceiver(networkChangeReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        isReceiverRegistred = true;
    }

    public void unregister(Context context) {
        if (isReceiverRegistred) {
            context.unregisterReceiver(networkChangeReceiver);
            isReceiverRegistred = false;
        }
    }
}