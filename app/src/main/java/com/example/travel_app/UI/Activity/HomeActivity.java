package com.example.travel_app.UI.Activity;

import android.Manifest;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.travel_app.R;
import com.example.travel_app.UI.Fragment.CreateItineraryFragment;
import com.example.travel_app.UI.Fragment.HistoryBookingFragment;
import com.example.travel_app.UI.Fragment.HomeFragmentQ;
import com.example.travel_app.UI.Fragment.PersonFragment.PersonInfomationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        EdgeToEdge.enable(this);

        bottomNavigationView = findViewById(R.id.navigation);

        if (savedInstanceState == null) {
            loadFragment(new HomeFragmentQ());
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragmentQ();
            } else if (itemId == R.id.nav_list_saved) {
                selectedFragment = new HistoryBookingFragment();
            } else if (itemId == R.id.nav_create_itinerary) {
                selectedFragment = new CreateItineraryFragment();
            } else if (itemId == R.id.nav_account) {
                selectedFragment = new PersonInfomationFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });

        checkRequiredPermissions();
    }

    private void checkRequiredPermissions() {
        // Kiểm tra quyền POST_NOTIFICATIONS (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 100);
            }
        }

        // Kiểm tra quyền SCHEDULE_EXACT_ALARM (Android 12+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(this, "Vui lòng cấp quyền để lên lịch thông báo chính xác", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("PermissionDebug", "POST_NOTIFICATIONS permission granted");
            } else {
                Toast.makeText(this, "Vui lòng cấp quyền thông báo để nhận nhắc nhở!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_home, fragment)
                .commit();
    }
}
