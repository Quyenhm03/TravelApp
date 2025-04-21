package com.example.travel_app.UI.Activity;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Adapter.FavoriteLocationAdapter;
import com.example.travel_app.R;
import com.example.travel_app.ViewModel.LocationViewModel;
import com.example.travel_app.ViewModel.UserCurrentViewModel;

public class FavoriteLocationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FavoriteLocationAdapter adapter;
    private LocationViewModel locationViewModel;
    private UserCurrentViewModel userCurrentViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        androidx.core.view.WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_favorite_location);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.orange));
        }
        // Khởi tạo ViewModel
        userCurrentViewModel = new ViewModelProvider(this).get(UserCurrentViewModel.class);
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);

        // Khởi tạo RecyclerView và Adapter
        recyclerView = findViewById(R.id.recyclerFavorite);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new FavoriteLocationAdapter(this,this);
        recyclerView.setAdapter(adapter);

        // Quan sát dữ liệu người dùng
        userCurrentViewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
                loadFavoriteLocations(user.getUserId());
            }
        });
    }

    private void loadFavoriteLocations(String userId) {
        locationViewModel.getFavoriteLocationsByUserId(userId).observe(this, locations -> {
            if (locations != null && !locations.isEmpty()) {
                adapter.setLocationList(locations);
            } else {
                Toast.makeText(this, "Không có địa điểm yêu thích nào", Toast.LENGTH_SHORT).show();
            }
        });

//        locationViewModel.getAllLocations().observe(this, locations -> {
//            if (locations != null && !locations.isEmpty()) {
//                adapter.setLocationList(locations);
//            } else {
//                Toast.makeText(this, "Không có địa điểm yêu thích nào", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
