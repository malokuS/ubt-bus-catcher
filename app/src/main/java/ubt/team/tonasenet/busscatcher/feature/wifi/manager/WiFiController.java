package ubt.team.tonasenet.busscatcher.feature.wifi.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * Created by malokuselim on 1/5/16.
 */
public class WiFiController
{

    WifiManager wifiManager;
    private final Context mContext;
    private List<ScanResult> wifiList;
    private BroadcastReceiver receiver;
    private static String BUS_WIFI  = ""; // Our desired wifi access point to connect to
    private static String BUS_WIFI_PASSWORD = ""; // The wifi Password

    public WiFiController(Context context)
    {
        this.mContext = context; // get the context from the main activity

        wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE); // Set up WiFi

        if(receiver == null)
        {
            receiver = new WifiReceiver();
        }

        mContext.registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)); // Register receiver to enable listening for scan results
    }

    public void setBusWifi(String ssid)
    {
        if(ssid != null && !ssid.trim().equals(""))
        {
            this.BUS_WIFI = ssid;
        }
    }

    public String getBusWifi()
    {
        return this.BUS_WIFI;
    }

    public void setBusWifiPassword(String password)
    {
        if(password != null && !password.trim().equals(""))
        {
            this.BUS_WIFI_PASSWORD = password;
        }
    }

    public boolean getWiFiStatus()
    {
        boolean wiFiStatus = wifiManager.isWifiEnabled();
        return wiFiStatus;
    }

    public void turnWifiOn()
    {

        wifiManager.setWifiEnabled(true);

        while (!wifiManager.isWifiEnabled())
        {
            //waiting for wifi to turn on
        }
    }

    public void turnWifiOff()
    {
        if(wifiManager.isWifiEnabled())
        {
            wifiManager.setWifiEnabled(false);
        }
    }

    public List<ScanResult> getWifiList()
    {
        return wifiList;
    }

    public void scanAvailableWifi()
    {
        if(wifiManager.isWifiEnabled())
        {
            wifiManager.startScan();
        }
    }

    public boolean connectToWifi()
    {
        boolean cont = false;
        for(ScanResult scan : wifiList)
        {
            if(scan.SSID.equals(BUS_WIFI))
            {
                cont = true;
                for(WifiConfiguration w: wifiManager.getConfiguredNetworks())
                {
                    // If wifi is already configured connect to it and skip adding network
                    String s = "\""+scan.SSID+"\"";
                    String bs = scan.BSSID;
                    if((w.SSID != null && w.SSID.equals(s))||(w.BSSID != null && w.BSSID.equals(bs)))
                    {
                        cont = false;
                        wifiManager.enableNetwork(w.networkId, true);
                        break;
                    }
                }
                if(cont)
                {
                    //Wifi isn't configured, add it.
                    WifiConfiguration config = new WifiConfiguration();
                    config.SSID = "\""+scan.SSID+"\"";
                    config.BSSID = scan.BSSID;
                    config.priority = 1;
                    config.preSharedKey = "\""+ BUS_WIFI_PASSWORD +"\"";

                    config.status = WifiConfiguration.Status.ENABLED;

                    config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);

                    config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);

                    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

                    config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);

                    config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                    config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);

                    int id = wifiManager.addNetwork(config);
                    wifiManager.enableNetwork(id, true);
                    wifiManager.saveConfiguration();
                }
            }
        }

        return cont;
    }


    class WifiReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            wifiList = wifiManager.getScanResults();
        }
    }
}
