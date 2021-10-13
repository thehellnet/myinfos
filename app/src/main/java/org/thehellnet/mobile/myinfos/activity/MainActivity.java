package org.thehellnet.mobile.myinfos.activity;

import static org.thehellnet.mobile.myinfos.MyInfos.PERMISSIONS;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.thehellnet.mobile.myinfos.R;
import org.thehellnet.mobile.myinfos.adapter.TabFragmentAdapter;
import org.thehellnet.mobile.myinfos.fragment.NetworkFragment;
import org.thehellnet.mobile.myinfos.fragment.PhoneFragment;
import org.thehellnet.mobile.myinfos.fragment.SIMFragment;
import org.thehellnet.mobile.myinfos.utility.AppUtility;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_READ_PHONE_STATE = 1;

    private TabFragmentAdapter tabFragmentAdapter;

    private TextView version;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVars();
        initUi();
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
                    startActivity(new Intent(this, NoPermsActivity.class));
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

    private void initVars() {
        version = findViewById(R.id.version_value);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Lifecycle lifecycle = getLifecycle();
        tabFragmentAdapter = new TabFragmentAdapter(fragmentManager, lifecycle);
    }

    private void initUi() {
        version.setText(String.format("App version %s", AppUtility.getAppVersion()));

        tabFragmentAdapter.addFragment(new PhoneFragment(), "Phone");
        tabFragmentAdapter.addFragment(new SIMFragment(1), "SIM 1");
        tabFragmentAdapter.addFragment(new SIMFragment(2), "SIM 2");
        tabFragmentAdapter.addFragment(new NetworkFragment(), "Network");

        viewPager.setAdapter(tabFragmentAdapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        String title = tabFragmentAdapter.getTitle(position);
                        tab.setText(title);
                    }
                });
        tabLayoutMediator.attach();

        setTabIcon(0, R.drawable.icon_smartphone);
        setTabIcon(1, R.drawable.icon_sim);
        setTabIcon(2, R.drawable.icon_sim);
        setTabIcon(3, R.drawable.icon_network);
    }

    private void setTabIcon(int index, int resId) {
        TabLayout.Tab tab = tabLayout.getTabAt(index);
        if (tab != null) {
            tab.setIcon(resId);
        }
    }
}
