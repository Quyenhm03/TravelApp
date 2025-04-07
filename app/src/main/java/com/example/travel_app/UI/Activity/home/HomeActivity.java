package com.example.travel_app.UI.Activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.travel_app.R;
import com.example.travel_app.UI.Activity.CreateItineraryActivity;
import com.example.travel_app.UI.Fragment.CreateNewItinerary.CreateItineraryFragment;
import com.example.travel_app.UI.Fragment.HomeFragment.HomeFragment;
import com.example.travel_app.UI.Fragment.PersonFragment.PersonInfomationFragment;
import com.example.travel_app.UI.Fragment.SaveFragment.SavedTravelFragment;

public class HomeActivity extends AppCompatActivity {

    private RelativeLayout navHome, navSave, navCreateItinerary, navPersonInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        replaceFragment(new HomeFragment());
        setupListenerSwitchFragment();
    }

    private void setupListenerSwitchFragment() {
        navHome.setOnClickListener(v -> replaceFragment(new HomeFragment()));
        navSave.setOnClickListener(v -> replaceFragment(new SavedTravelFragment()));
       // navCreateItinerary.setOnClickListener(v -> replaceFragment(new CreateItineraryFragment()));
        navCreateItinerary.setOnClickListener(v -> createItinerary());
        navPersonInformation.setOnClickListener(v -> replaceFragment(new PersonInfomationFragment()));
    }

    private void createItinerary() {
        Intent intent = new Intent(HomeActivity.this, CreateItineraryActivity.class);
        startActivity(intent);
    }

    private void initView(){
        navHome = findViewById(R.id.navHome);
        navSave = findViewById(R.id.navSave);
        navCreateItinerary = findViewById(R.id.navCreateItinerary);
        navPersonInformation = findViewById(R.id.navPersonInfomation);
    }
    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView2, fragment)
                .commit();
    }
}