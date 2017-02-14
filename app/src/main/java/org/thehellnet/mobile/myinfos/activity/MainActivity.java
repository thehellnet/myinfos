package org.thehellnet.mobile.myinfos.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.thehellnet.mobile.myinfos.MyInfos;
import org.thehellnet.mobile.myinfos.R;
import org.thehellnet.mobile.myinfos.utility.AppUtils;

public class MainActivity extends Activity {

    private static final int REQUEST_CODE_READ_PHONE_STATE = 1;

    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE,
    };

    private TelephonyManager telephonyManager;

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
        if (telephonyManager == null) {
            return;
        }

        if (telephonyManager.getDeviceId() != null) {
            EditText imei = (EditText) findViewById(R.id.imei_value);
            imei.setText(formatEmpty(telephonyManager.getDeviceId()));
        }

        if (telephonyManager.getDeviceSoftwareVersion() != null) {
            EditText swver = (EditText) findViewById(R.id.swver_value);
            swver.setText(formatEmpty(telephonyManager.getDeviceSoftwareVersion()));
        }

        if (telephonyManager.getSimSerialNumber() != null) {
            EditText simIccid = (EditText) findViewById(R.id.sim_iccid_value);
            simIccid.setText(formatEmpty(telephonyManager.getSimSerialNumber()));
        }

        if (telephonyManager.getSimCountryIso() != null) {
            EditText simCountry = (EditText) findViewById(R.id.sim_country_value);
            simCountry.setText(formatEmpty(telephonyManager.getSimCountryIso()));
        }

        if (telephonyManager.getSimOperatorName() != null) {
            EditText simOperator = (EditText) findViewById(R.id.sim_operator_value);
            simOperator.setText(formatEmpty(telephonyManager.getSimOperatorName()));
        }

        if (telephonyManager.getLine1Number() != null) {
            EditText number = (EditText) findViewById(R.id.number_value);
            number.setText(formatEmpty(telephonyManager.getLine1Number()));
        }

        if (telephonyManager.getNetworkOperatorName() != null) {
            EditText operator = (EditText) findViewById(R.id.operator_value);
            operator.setText(formatEmpty(telephonyManager.getNetworkOperatorName()));
        }

        if (telephonyManager.getSubscriberId() != null) {
            EditText subscriber = (EditText) findViewById(R.id.subscriber_value);
            subscriber.setText(formatEmpty(telephonyManager.getSubscriberId()));
        }
    }

    private void showVersionToast() {
        Toast.makeText(MyInfos.getAppContext(), AppUtils.getAppVersion(), Toast.LENGTH_LONG).show();
    }

    private String formatEmpty(String input) {
        if (input == null || input.length() == 0) {
            return getString(R.string.ui_value_notdefined);
        }

        return input;
    }
}
