package org.thehellnet.mobile.myinfos.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import org.thehellnet.mobile.myinfos.R;

public abstract class AbstractFragment extends Fragment {

    private static final String TAG = AbstractFragment.class.getName();

    protected Context applicationContext;

    public AbstractFragment() {
    }

    public AbstractFragment(int contentLayoutId) {
        super(contentLayoutId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            Log.e(TAG, "Activity null");
            return null;
        }

        applicationContext = activity.getApplicationContext();

        View view = inflater.inflate(getLayout(), container, false);
        initVars(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        preparePhone();
        computeValues();
    }

    protected void updateEditTextValue(EditText editText, String text) {
        if (text == null || text.length() == 0) {
            editText.setText(getString(R.string.ui_value_notdefined));
            editText.setTextColor(getResources().getColor(R.color.text_not_defined));
            editText.setTextIsSelectable(false);
            editText.setFocusable(false);
            return;
        }

        editText.setText(text);
    }

    protected void preparePhone() {
    }

    protected abstract int getLayout();

    protected abstract void initVars(View view);

    protected abstract void computeValues();

}
