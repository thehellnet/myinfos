package org.thehellnet.mobile.myinfos.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.thehellnet.mobile.myinfos.R;

public abstract class AbstractFragment extends Fragment {

    public AbstractFragment() {
    }

    public AbstractFragment(int contentLayoutId) {
        super(contentLayoutId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        initPrivates(view);
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

    protected abstract void initPrivates(View view);

    protected abstract void computeValues();

}
