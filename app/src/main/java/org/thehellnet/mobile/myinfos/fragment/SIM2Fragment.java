package org.thehellnet.mobile.myinfos.fragment;

import android.content.Context;
import android.os.Build;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.thehellnet.mobile.myinfos.R;

import static android.content.Context.TELEPHONY_SERVICE;

public class SIM2Fragment extends AbstractFragment {

    private static final String TAG = SIM2Fragment.class.getName();

    private TelephonyManager telephonyManager;
    private SubscriptionManager subscriptionManager;

    private EditText iccid;
    private EditText country;
    private EditText operator;
    private EditText number;
    private EditText carrier;
    private EditText subscriber;

    @Override
    protected int getLayout() {
        return R.layout.fragment_sim2;
    }

    @Override
    protected void initPrivates(View view) {
        telephonyManager = (TelephonyManager) getActivity().getSystemService(TELEPHONY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            subscriptionManager = (SubscriptionManager) getActivity().getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        }

        iccid = view.findViewById(R.id.sim2_iccid_value);
        country = view.findViewById(R.id.sim2_country_value);
        operator = view.findViewById(R.id.sim2_operator_value);
        number = view.findViewById(R.id.number2_value);
        carrier = view.findViewById(R.id.carrier2_value);
        subscriber = view.findViewById(R.id.subscriber2_value);
    }

    @Override
    protected void computeValues() throws SecurityException {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                SubscriptionInfo activeSubscriptionInfo = subscriptionManager.getActiveSubscriptionInfo(2);
                if (activeSubscriptionInfo != null) {
                    updateEditTextValue(iccid, activeSubscriptionInfo.getIccId());
                    updateEditTextValue(country, activeSubscriptionInfo.getCountryIso());
                    updateEditTextValue(operator, activeSubscriptionInfo.getDisplayName().toString());
                    updateEditTextValue(number, activeSubscriptionInfo.getNumber());
                    updateEditTextValue(carrier, activeSubscriptionInfo.getCarrierName().toString());
                    updateEditTextValue(subscriber, String.valueOf(activeSubscriptionInfo.getSubscriptionId()));
                }
            } else {
                updateEditTextValue(iccid, telephonyManager.getSimSerialNumber());
                updateEditTextValue(country, telephonyManager.getSimCountryIso());
                updateEditTextValue(operator, telephonyManager.getSimOperatorName());
                updateEditTextValue(number, telephonyManager.getLine1Number());
                updateEditTextValue(carrier, telephonyManager.getNetworkOperatorName());
                updateEditTextValue(subscriber, telephonyManager.getSubscriberId());
            }
        } catch (SecurityException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
