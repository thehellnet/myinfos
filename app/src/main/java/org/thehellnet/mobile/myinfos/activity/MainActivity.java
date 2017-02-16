package org.thehellnet.mobile.myinfos.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;
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

import static org.thehellnet.mobile.myinfos.MyInfos.PERMISSIONS;

public class MainActivity extends Activity {

    private static final int REQUEST_CODE_READ_PHONE_STATE = 1;

    private TelephonyManager telephonyManager;
    private ConnectivityManager connectivityManager;

    private EditText imei;
    private EditText swver;
    private EditText deviceId;
    private EditText simIccid;
    private EditText simCountry;
    private EditText simOperator;
    private EditText number;
    private EditText operator;
    private EditText subscriber;
    private EditText networkCount;
    private EditText networkType;
    private EditText cellList;

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

        imei = (EditText) findViewById(R.id.imei_value);
        swver = (EditText) findViewById(R.id.swver_value);
        deviceId = (EditText) findViewById(R.id.device_id_value);
        simIccid = (EditText) findViewById(R.id.sim_iccid_value);
        simCountry = (EditText) findViewById(R.id.sim_country_value);
        simOperator = (EditText) findViewById(R.id.sim_operator_value);
        number = (EditText) findViewById(R.id.number_value);
        operator = (EditText) findViewById(R.id.operator_value);
        subscriber = (EditText) findViewById(R.id.subscriber_value);
        networkCount = (EditText) findViewById(R.id.network_count_value);
        networkType = (EditText) findViewById(R.id.network_type_value);
        cellList = (EditText) findViewById(R.id.network_celllist_value);
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

        updateValue(imei, telephonyManager.getDeviceId());
        updateValue(swver, telephonyManager.getDeviceSoftwareVersion());
        updateValue(deviceId, Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
        updateValue(simIccid, telephonyManager.getSimSerialNumber());
        updateValue(simCountry, telephonyManager.getSimCountryIso());
        updateValue(simOperator, telephonyManager.getSimOperatorName());
        updateValue(number, telephonyManager.getLine1Number());
        updateValue(operator, telephonyManager.getNetworkOperatorName());
        updateValue(subscriber, telephonyManager.getSubscriberId());

        if (telephonyManager.getAllCellInfo() != null) {
            updateValue(networkCount, String.valueOf(telephonyManager.getAllCellInfo().size()));
            updateCellInfo(telephonyManager.getAllCellInfo());
        }

        updateNetworkType(connectivityManager.getActiveNetworkInfo());
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
            updateValue(networkType, connectivityManager.getActiveNetworkInfo().getSubtypeName());
        } else {
            updateValue(networkType, connectivityManager.getActiveNetworkInfo().getTypeName());
        }
    }
}
