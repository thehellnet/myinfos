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

        if (grantResults.length > 0) {
            switch (requestCode) {
                case REQUEST_CODE_READ_PHONE_STATE:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        updateUiValues();
                    } else {
                        startActivity(new Intent(MyInfos.getAppContext(), NoPermsActivity.class));
                        finish();
                    }
                    break;
            }
        }
    }

    private void initPrivates() {
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_CODE_READ_PHONE_STATE);
        }
        return false;
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
            imei.setText(telephonyManager.getDeviceId());
        }

        if (telephonyManager.getDeviceSoftwareVersion() != null) {
            EditText swver = (EditText) findViewById(R.id.swver_value);
            swver.setText(telephonyManager.getDeviceSoftwareVersion());
        }

        if (telephonyManager.getSimSerialNumber() != null) {
            EditText iccid = (EditText) findViewById(R.id.iccid_value);
            iccid.setText(telephonyManager.getSimSerialNumber());
        }

        if (telephonyManager.getLine1Number() != null) {
            EditText number = (EditText) findViewById(R.id.number_value);
            number.setText(telephonyManager.getLine1Number().length() > 0
                    ? telephonyManager.getLine1Number()
                    : getString(R.string.ui_value_notdefined));
        }

        if (telephonyManager.getNetworkOperatorName() != null) {
            EditText operator = (EditText) findViewById(R.id.operator_value);
            operator.setText(telephonyManager.getNetworkOperatorName());
        }

        if (telephonyManager.getSubscriberId() != null) {
            EditText subscriber = (EditText) findViewById(R.id.subscriber_value);
            subscriber.setText(telephonyManager.getSubscriberId());
        }
    }

    private void showVersionToast() {
        Toast.makeText(MyInfos.getAppContext(), AppUtils.getAppVersion(), Toast.LENGTH_LONG).show();
    }
}
