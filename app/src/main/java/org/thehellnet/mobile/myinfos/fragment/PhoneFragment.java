package org.thehellnet.mobile.myinfos.fragment;

import static android.content.Context.TELEPHONY_SERVICE;

import android.annotation.SuppressLint;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.FragmentActivity;

import org.thehellnet.mobile.myinfos.R;

public class PhoneFragment extends AbstractFragment {

    private static final String TAG = PhoneFragment.class.getName();

    private TelephonyManager telephonyManager;

    private EditText imei1;
    private EditText imei2;
    private EditText swver;
    private EditText deviceId;

    public PhoneFragment() {
    }

    public PhoneFragment(int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_phone;
    }

    @Override
    protected void initVars(View view) {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            Log.e(TAG, "Activity null");
            return;
        }

        telephonyManager = (TelephonyManager) applicationContext.getSystemService(TELEPHONY_SERVICE);

        imei1 = view.findViewById(R.id.imei1_value);
        imei2 = view.findViewById(R.id.imei2_value);
        swver = view.findViewById(R.id.swver_value);
        deviceId = view.findViewById(R.id.device_id_value);
    }

    @SuppressLint("HardwareIds")
    @Override
    protected void computeValues() throws SecurityException {
        String imei1Value = "NOT AVAILABLE";
        String imei2Value = "NOT AVAILABLE";
        String softwareVersion = "NOT AVAILABLE";
        String androidId = "NOT AVAILABLE";

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                imei1Value = telephonyManager.getImei(0);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                imei1Value = telephonyManager.getDeviceId(0);
            } else {
                imei1Value = telephonyManager.getDeviceId();
            }
        } catch (SecurityException e) {
            Log.e(TAG, e.getMessage());
            imei1Value = "NO PERMISSION";
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                imei2Value = telephonyManager.getImei(1);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                imei2Value = telephonyManager.getDeviceId(1);
            }
        } catch (SecurityException e) {
            Log.e(TAG, e.getMessage());
            imei2Value = "NO PERMISSION";
        }

        try {
            softwareVersion = telephonyManager.getDeviceSoftwareVersion();
        } catch (SecurityException e) {
            Log.e(TAG, e.getMessage());
            softwareVersion = "NO PERMISSION";
        }

        try {
            androidId = Settings.Secure.getString(applicationContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (SecurityException e) {
            Log.e(TAG, e.getMessage());
            androidId = "NO PERMISSION";
        }

        updateEditTextValue(imei1, imei1Value);
        updateEditTextValue(imei2, imei2Value);
        updateEditTextValue(swver, softwareVersion);
        updateEditTextValue(deviceId, androidId);
    }
}
