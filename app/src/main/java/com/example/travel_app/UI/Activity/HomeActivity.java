package com.example.travel_app.UI.Activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.travel_app.R;
import com.example.travel_app.UI.Fragment.CreateItineraryFragment;
import com.example.travel_app.UI.Fragment.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.navigation);

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
//            } else if (itemId == R.id.nav_list_saved) {
//                selectedFragment = new RecommendFragment();
            } else if (itemId == R.id.nav_create_itinerary) {
                selectedFragment = new CreateItineraryFragment();
            }
//            } else if (itemId == R.id.nav_account) {
//                selectedFragment = new AccountFragment();
//            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_home, fragment)
                .commit();
    }
}
