package org.thehellnet.mobile.myinfos.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.thehellnet.mobile.myinfos.MyInfos;
import org.thehellnet.mobile.myinfos.R;
import org.thehellnet.mobile.myinfos.utility.AppUtils;

public class MainActivity extends AppCompatActivity {

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
        updateUiValues();
    }

    private void initPrivates() {
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
    }

    private void updateUiVersion() {
        TextView version = (TextView) findViewById(R.id.version_value);
        version.setText(String.format("App version %s", AppUtils.getAppVersion()));
    }

    private void updateUiValues() {
        EditText imei = (EditText) findViewById(R.id.imei_value);
        imei.setText(telephonyManager.getDeviceId());

        EditText iccid = (EditText) findViewById(R.id.iccid_value);
        iccid.setText(telephonyManager.getSimSerialNumber());
        EditText number = (EditText) findViewById(R.id.number_value);
        number.setText(telephonyManager.getLine1Number().length() > 0
                ? telephonyManager.getLine1Number()
                : getString(R.string.ui_value_notdefined));
        EditText operator = (EditText) findViewById(R.id.operator_value);
        operator.setText(telephonyManager.getNetworkOperatorName());
        EditText subscriber = (EditText) findViewById(R.id.subscriber_value);
        subscriber.setText(telephonyManager.getSubscriberId());
    }

    private void showVersionToast() {
        Toast.makeText(MyInfos.getAppContext(), AppUtils.getAppVersion(), Toast.LENGTH_LONG).show();
    }
}
