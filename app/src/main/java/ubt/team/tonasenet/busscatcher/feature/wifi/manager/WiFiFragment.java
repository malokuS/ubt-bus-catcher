package ubt.team.tonasenet.busscatcher.feature.wifi.manager;

import android.annotation.TargetApi;
import android.support.v4.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import ubt.team.tonasenet.busscatcher.R;

/**
 * Created by malokuselim on 1/5/16.
 */
@TargetApi(Build.VERSION_CODES.M)
public class WiFiFragment extends Fragment implements View.OnClickListener {

    private Switch wifiSwitch;
    private static WiFiController wiFiController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifi, container, false);

        wiFiController = new WiFiController(getActivity());

        wifiSwitch = (Switch) view.findViewById(R.id.wifiSwitch);
        wifiSwitch.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {

        if(wifiSwitch.isChecked())
        {
            wiFiController.turnWifiOn();
        }else {
            wiFiController.turnWifiOff();
        }
    }
}
