package org.thehellnet.mobile.myinfos.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.thehellnet.mobile.myinfos.MyInfos;
import org.thehellnet.mobile.myinfos.R;
import org.thehellnet.mobile.myinfos.cell.CellData;
import org.thehellnet.mobile.myinfos.cell.CellDataFactory;
import org.thehellnet.mobile.myinfos.utility.AppUtils;

import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.READ_PHONE_STATE;

public class MainActivity extends Activity {

    private static final int REQUEST_CODE_READ_PHONE_STATE = 1;

    private static final String[] PERMISSIONS = new String[]{
            ACCESS_NETWORK_STATE,
            READ_PHONE_STATE,
            ACCESS_COARSE_LOCATION
    };

    private TelephonyManager telephonyManager;
    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPrivates();
        updateUiVersion();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_version) {
            showVersionToast();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (checkPermissions()) {
            updateUiValues();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_READ_PHONE_STATE) {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    startActivity(new Intent(MyInfos.getAppContext(), NoPermsActivity.class));
                    finish();
                }
            }

            updateUiValues();
        }
    }

    private void initPrivates() {
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    }

    private boolean checkPermissions() {
        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CODE_READ_PHONE_STATE);
                return false;
            }
        }

        return true;
    }

    private void updateUiVersion() {
        TextView version = (TextView) findViewById(R.id.version_value);
        version.setText(String.format("App version %s", AppUtils.getAppVersion()));
    }

    private void updateUiValues() {
        if (telephonyManager == null
                || connectivityManager == null) {
            return;
        }

        EditText imei = (EditText) findViewById(R.id.imei_value);
        EditText swver = (EditText) findViewById(R.id.swver_value);
        EditText deviceId = (EditText) findViewById(R.id.device_id_value);
        EditText simIccid = (EditText) findViewById(R.id.sim_iccid_value);
        EditText simCountry = (EditText) findViewById(R.id.sim_country_value);
        EditText simOperator = (EditText) findViewById(R.id.sim_operator_value);
        EditText number = (EditText) findViewById(R.id.number_value);
        EditText operator = (EditText) findViewById(R.id.operator_value);
        EditText subscriber = (EditText) findViewById(R.id.subscriber_value);
        EditText networkCount = (EditText) findViewById(R.id.network_count_value);
        EditText networkType = (EditText) findViewById(R.id.network_type_value);

        if (telephonyManager.getDeviceId() != null) {
            updateValue(imei, telephonyManager.getDeviceId());
        }

        if (telephonyManager.getDeviceSoftwareVersion() != null) {
            updateValue(swver, telephonyManager.getDeviceSoftwareVersion());
        }

        updateValue(deviceId, Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));

        if (telephonyManager.getSimSerialNumber() != null) {
            updateValue(simIccid, telephonyManager.getSimSerialNumber());
        }

        if (telephonyManager.getSimCountryIso() != null) {
            updateValue(simCountry, telephonyManager.getSimCountryIso());
        }

        if (telephonyManager.getSimOperatorName() != null) {
            updateValue(simOperator, telephonyManager.getSimOperatorName());
        }

        if (telephonyManager.getLine1Number() != null) {
            updateValue(number, telephonyManager.getLine1Number());
        }

        if (telephonyManager.getNetworkOperatorName() != null) {
            updateValue(operator, telephonyManager.getNetworkOperatorName());
        }

        if (telephonyManager.getSubscriberId() != null) {
            updateValue(subscriber, telephonyManager.getSubscriberId());
        }

        if (telephonyManager.getAllCellInfo() != null) {
            updateValue(networkCount, String.valueOf(telephonyManager.getAllCellInfo().size()));
            updateCellInfo(telephonyManager.getAllCellInfo());
        }

        if (connectivityManager.getActiveNetworkInfo() != null) {
            updateValue(networkType, String.valueOf(connectivityManager.getActiveNetworkInfo().getSubtypeName()));
        }
    }

    private void showVersionToast() {
        Toast.makeText(MyInfos.getAppContext(), AppUtils.getAppVersion(), Toast.LENGTH_LONG).show();
    }

    private void updateValue(EditText editText, String text) {
        if (text == null || text.length() == 0) {
            editText.setText(getString(R.string.ui_value_notdefined));
            editText.setTextColor(getResources().getColor(R.color.text_not_defined));
            editText.setTextIsSelectable(false);
            editText.setFocusable(false);
            return;
        }

        editText.setText(text);
    }

    private void updateCellInfo(List<CellInfo> cellInfos) {
        if (cellInfos == null) {
            return;
        }

        EditText cellList = (EditText) findViewById(R.id.network_celllist_value);
        cellList.setText("");

        for (int i = 0; i < cellInfos.size(); i++) {
            CellInfo cellInfo = cellInfos.get(i);
            CellData cellData = CellDataFactory.parseData(cellInfo);
            cellList.append(String.format(Locale.US, "%d. %s\n", i + 1, cellData.toString()));
        }
    }
}
