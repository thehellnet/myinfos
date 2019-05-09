package org.thehellnet.mobile.myinfos.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.thehellnet.mobile.myinfos.MyInfos;
import org.thehellnet.mobile.myinfos.R;
import org.thehellnet.mobile.myinfos.adapter.TabAdapter;
import org.thehellnet.mobile.myinfos.fragment.NetworkFragment;
import org.thehellnet.mobile.myinfos.fragment.PhoneFragment;
import org.thehellnet.mobile.myinfos.fragment.SIMFragment;
import org.thehellnet.mobile.myinfos.utility.AppUtils;

import static org.thehellnet.mobile.myinfos.MyInfos.PERMISSIONS;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_READ_PHONE_STATE = 1;

    private TabAdapter tabAdapter;

    private TextView version;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        version = findViewById(R.id.version_value);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        version.setText(String.format("App version %s", AppUtils.getAppVersion()));

        tabAdapter = new TabAdapter(getSupportFragmentManager());
        tabAdapter.addFragment(new PhoneFragment(), "Phone");
        tabAdapter.addFragment(new SIMFragment(1), "SIM 1");
        tabAdapter.addFragment(new SIMFragment(2), "SIM 2");
        tabAdapter.addFragment(new NetworkFragment(), "Network");

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.icon_smartphone);
        tabLayout.getTabAt(1).setIcon(R.drawable.icon_sim);
        tabLayout.getTabAt(2).setIcon(R.drawable.icon_sim);
        tabLayout.getTabAt(3).setIcon(R.drawable.icon_network);
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPermissions();
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
        }
    }

    private void checkPermissions() {
        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CODE_READ_PHONE_STATE);
            }
        }
    }
}
