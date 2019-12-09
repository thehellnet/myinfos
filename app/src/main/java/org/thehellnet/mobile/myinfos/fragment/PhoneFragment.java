package org.thehellnet.mobile.myinfos.fragment;

import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.thehellnet.mobile.myinfos.R;

import static android.content.Context.TELEPHONY_SERVICE;

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
    protected void initPrivates(View view) {
        telephonyManager = (TelephonyManager) getActivity().getSystemService(TELEPHONY_SERVICE);

        imei1 = view.findViewById(R.id.imei1_value);
        imei2 = view.findViewById(R.id.imei2_value);
        swver = view.findViewById(R.id.swver_value);
        deviceId = view.findViewById(R.id.device_id_value);
    }

    @Override
    protected void computeValues() throws SecurityException {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                updateEditTextValue(imei1, telephonyManager.getImei(0));
                updateEditTextValue(imei2, telephonyManager.getImei(1));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                updateEditTextValue(imei1, telephonyManager.getDeviceId(0));
                updateEditTextValue(imei2, telephonyManager.getDeviceId(1));
            } else {
                updateEditTextValue(imei1, telephonyManager.getDeviceId());
            }

            updateEditTextValue(swver, telephonyManager.getDeviceSoftwareVersion());
            updateEditTextValue(deviceId, Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
        } catch (SecurityException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
