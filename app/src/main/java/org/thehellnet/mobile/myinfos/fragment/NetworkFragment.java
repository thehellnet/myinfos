package org.thehellnet.mobile.myinfos.fragment;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.TELEPHONY_SERVICE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.FragmentActivity;

import org.thehellnet.mobile.myinfos.R;
import org.thehellnet.mobile.myinfos.cell.CellData;
import org.thehellnet.mobile.myinfos.cell.CellDataFactory;

import java.util.List;
import java.util.Locale;

public class NetworkFragment extends AbstractFragment {

    private static final String TAG = NetworkFragment.class.getName();

    private TelephonyManager telephonyManager;
    private ConnectivityManager connectivityManager;
    private LocationManager locationManager;

    private EditText networkCount;
    private EditText networkType;
    private EditText cellList;

    public NetworkFragment() {
    }

    public NetworkFragment(int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_network;
    }

    @Override
    protected void initVars(View view) {
        telephonyManager = (TelephonyManager) applicationContext.getSystemService(TELEPHONY_SERVICE);
        connectivityManager = (ConnectivityManager) applicationContext.getSystemService(CONNECTIVITY_SERVICE);
        locationManager = (LocationManager) applicationContext.getSystemService(LOCATION_SERVICE);

        networkCount = view.findViewById(R.id.network_count_value);
        networkType = view.findViewById(R.id.network_type_value);
        cellList = view.findViewById(R.id.network_celllist_value);
    }

    @Override
    protected void preparePhone() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            displayNoGPSAlert();
        }
    }

    private void displayNoGPSAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.activity_no_gps_message);
        builder.setCancelable(false);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });

        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {
                dialog.cancel();
            }
        });

        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void computeValues() throws SecurityException {
        try {
            if (telephonyManager.getAllCellInfo() != null) {
                updateEditTextValue(networkCount, String.valueOf(telephonyManager.getAllCellInfo().size()));
                updateCellInfo(telephonyManager.getAllCellInfo());
            } else {
                Log.i(TAG, "getAllCellInfo is null");
            }

            updateNetworkType(connectivityManager.getActiveNetworkInfo());
        } catch (SecurityException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void updateCellInfo(List<CellInfo> cellInfos) {
        if (cellInfos == null) {
            return;
        }

        cellList.setText("");

        for (int i = 0; i < cellInfos.size(); i++) {
            CellInfo cellInfo = cellInfos.get(i);
            CellData cellData = CellDataFactory.parseData(cellInfo);
            String cellDataString = "NONE";
            if (cellData != null) {
                cellDataString = cellData.toString();
            }
            cellList.append(String.format(Locale.US, "%d. %s\n", i + 1, cellDataString));
        }

        cellList.setKeyListener(null);
    }

    private void updateNetworkType(NetworkInfo networkInfo) {
        if (networkInfo.getSubtypeName() != null && networkInfo.getSubtypeName().length() > 0) {
            updateEditTextValue(networkType, connectivityManager.getActiveNetworkInfo().getSubtypeName());
        } else {
            updateEditTextValue(networkType, connectivityManager.getActiveNetworkInfo().getTypeName());
        }
    }
}
