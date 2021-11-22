package com.example.bikewarningandroid;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class AppUtils {
    private static final String TAG = AppUtils.class.getSimpleName();
    public static final int PORT_NUMBER = 6000;
    public static final int ON = 1;
    public static final int OFF = 0;
    public static final String STOP_LISTENING = "Stop Listening";
    public static final String START_LISTENING = "Start Listening";

    public static String getIP(Context context) {
        String ip = "";
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if(wifiManager != null) {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                int ipInt = wifiInfo.getIpAddress();
                ip = InetAddress.getByAddress(
                        ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ipInt).array())
                        .getHostAddress();

            } else {
                Log.d(TAG, "getIP: wifiManager == null");
            }
        } catch (UnknownHostException e) {
            Log.d(TAG, "getIP: UnknownHostException = " + e.getMessage());
        }
        return ip;
    }
}
