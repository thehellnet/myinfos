package org.thehellnet.mobile.myinfos.fragment;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.thehellnet.mobile.myinfos.R;
import org.thehellnet.mobile.myinfos.cell.CellData;
import org.thehellnet.mobile.myinfos.cell.CellDataFactory;

import java.util.List;
import java.util.Locale;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.TELEPHONY_SERVICE;

public class NetworkFragment extends AbstractFragment {

    private static final String TAG = NetworkFragment.class.getName();

    private TelephonyManager telephonyManager;
    private ConnectivityManager connectivityManager;

    private EditText networkCount;
    private EditText networkType;
    private EditText cellList;

    @Override
    protected int getLayout() {
        return R.layout.fragment_network;
    }

    @Override
    protected void initPrivates(View view) {
        telephonyManager = (TelephonyManager) getActivity().getSystemService(TELEPHONY_SERVICE);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);

        networkCount = view.findViewById(R.id.network_count_value);
        networkType = view.findViewById(R.id.network_type_value);
        cellList = view.findViewById(R.id.network_celllist_value);
    }

    @Override
    protected void computeValues() throws SecurityException {
        try {
            if (telephonyManager.getAllCellInfo() != null) {
                updateEditTextValue(networkCount, String.valueOf(telephonyManager.getAllCellInfo().size()));
                updateCellInfo(telephonyManager.getAllCellInfo());
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
            cellList.append(String.format(Locale.US, "%d. %s\n", i + 1, cellData.toString()));
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
