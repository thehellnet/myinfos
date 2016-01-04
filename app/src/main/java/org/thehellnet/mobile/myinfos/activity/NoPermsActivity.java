package org.thehellnet.mobile.myinfos.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.thehellnet.mobile.myinfos.R;

/**
 * Created by sardylan on 04/01/16.
 */
public class NoPermsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_perms);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setOnclickListeners();
    }

    private void setOnclickListeners() {
        Button button = (Button) findViewById(R.id.button_close);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
