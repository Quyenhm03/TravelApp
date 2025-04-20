package com.example.travel_app.UI.Activity.Location;

import static android.webkit.URLUtil.isValidUrl;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.travel_app.Adapter.MediaAdapter;
import com.example.travel_app.Adapter.ReviewAdapter;
import com.example.travel_app.Adapter.WeatherAdapter;
import com.example.travel_app.Data.Model.GeoResponse;
import com.example.travel_app.Data.Model.Image;
import com.example.travel_app.Data.Model.Location;
import com.example.travel_app.Data.Model.Review;
import com.example.travel_app.Data.Model.ReviewWithUser;

import com.example.travel_app.Data.Model.WeatherItem;
import com.example.travel_app.Data.Repository.WeatherRepository;
import com.example.travel_app.R;
import com.example.travel_app.ViewModel.LocationSelectedViewModel;
import com.example.travel_app.ViewModel.LocationViewModel;
import com.example.travel_app.ViewModel.MediaViewModel;
import com.example.travel_app.ViewModel.ReviewViewModel;
import com.example.travel_app.ViewModel.Itinerary.ImageViewModel;
import com.example.travel_app.ViewModel.UserCurrentViewModel;

import com.example.travel_app.WeatherResponse;
import com.google.android.material.tabs.TabLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LocationActivity extends AppCompatActivity {

    private static final String TAG = "LocationActivity";

    private ReviewAdapter reviewAdapter;
    private List<ReviewWithUser> reviews;
    private ReviewViewModel reviewViewModel;
    private LocationViewModel locationViewModel;
    private UserCurrentViewModel userCurrentViewModel;
    private MediaViewModel mediaViewModel;
    private ImageViewModel imageViewModel;
    private static final int REQUEST_CODE_REVIEW = 100;
    private RatingBar rbAverageRating;
    private ViewPager2 viewPagerMedia;
    private TabLayout tabLayoutDots;
    private TextView tvDescription;
    private RecyclerView rvReviews;
    private Button btnViewMoreReviews;
    private Button btnAddReview;
    private ViewPager2 viewPagerWeather;
    private Button btnOpenMap;
    private ImageView ivFavorite;
    private TextView tvLocationTitle;
    private boolean isCurrentFavorite = false;
    private int locationId;
    private WeatherAdapter weatherAdapter;
    private WeatherRepository weatherRepository;
    private String locationName = "";
    private LocationSelectedViewModel locationSelectedViewModel;

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.orange));
        }
        locationSelectedViewModel = new ViewModelProvider(this).get(LocationSelectedViewModel.class);
        locationId = getIntent().getIntExtra("location_id", -1);
        if (locationId == -1) {
            locationId = 1; // Giá trị mặc định
            Toast.makeText(this, "Không tìm thấy địa điểm", Toast.LENGTH_SHORT).show();
        }

        reviews = new ArrayList<>();

        initViews();
        userCurrentViewModel = new ViewModelProvider(this).get(UserCurrentViewModel.class);
        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        mediaViewModel = new ViewModelProvider(this).get(MediaViewModel.class);
        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);
        locationSelectedViewModel.getLocation().observe(this, location -> {
            if (location != null) {
                setupWeatherViewPager(location);
            }
            else {
                Toast.makeText(this, "Không tìm thấy địa điểm", Toast.LENGTH_SHORT).show();
            }
        });
        setupReviewsRecyclerView();

        locationViewModel.getLocation(locationId).observe(this, location -> {
            if (location != null) {
                tvDescription.setText(location.getMoTa() != null ? location.getMoTa() : "Không có mô tả.");
                setupMapButton();
                tvLocationTitle.setText(location.getTenDiaDiem());
                // Đặt icon yêu thích ban đầu
                ImageView ivFavorite = findViewById(R.id.ivFavorite);
                if (location.isFavorite()) {
                    ivFavorite.setImageResource(R.drawable.ic_favorite_true);
                } else {
                    ivFavorite.setImageResource(R.drawable.ic_favorite_false);
                }
                // Gắn sự kiện khi nhấn icon yêu thích
                ivFavorite.setOnClickListener(v -> {
                    boolean newFavorite = !location.isFavorite(); // Đảo trạng thái

                    // Cập nhật icon
                    ivFavorite.setImageResource(
                            newFavorite ? R.drawable.ic_favorite_true : R.drawable.ic_favorite_false
                    );

                    // Cập nhật trạng thái yêu thích lên Firebase
                    location.setFavorite(newFavorite);
                    locationViewModel.updateLocationFavorite(locationId, newFavorite);

                    Toast.makeText(this, newFavorite ? "Đã thêm vào yêu thích" : "Đã bỏ yêu thích", Toast.LENGTH_SHORT).show();
                });
                locationName = location.getTenDiaDiem();
                setupWeatherViewPager(location);
                Log.d("WeatherDebug", "locationName được cập nhật: " + locationName);
            } else {
                Toast.makeText(this, "Không tìm thấy thông tin địa điểm!", Toast.LENGTH_SHORT).show();
                tvDescription.setText("Không có mô tả.");
            }
        });


        // Quan sát đánh giá
        reviewViewModel.getReviews(locationId).observe(this, newReviews -> {
            if (newReviews != null && !newReviews.isEmpty()) {

                reviews.clear();
                reviews.addAll(newReviews);
                reviewAdapter.updateReviews(reviews);
                btnViewMoreReviews.setVisibility(reviews.size() > 3 ? View.VISIBLE : View.GONE);
            } else {
                Toast.makeText(this, "Chưa có đánh giá nào cho địa điểm này!", Toast.LENGTH_SHORT).show();
                reviews.clear();
                reviewAdapter.updateReviews(reviews);
                btnViewMoreReviews.setVisibility(View.GONE);
            }
        });

        imageViewModel.getImageUrlMapLiveData().observe(this, imageUrlMap -> {
            String imageUrl = imageUrlMap.get(locationId);
            if (imageUrl != null && isValidUrl(imageUrl)) {

                List<String> mediaUrls = new ArrayList<>();
                mediaUrls.add(imageUrl);
                MediaAdapter mediaAdapter = new MediaAdapter(mediaUrls);
                viewPagerMedia.setAdapter(mediaAdapter);
//                new TabLayoutMediator(tabLayoutDots, viewPagerMedia, (tab, position) -> {
//                }).attach();
            } else {

                // Thay ảnh placeholder nếu URL không hợp lệ
                List<String> mediaUrls = new ArrayList<>();
                mediaUrls.add("https://example.com/placeholder_image.jpg");  // URL ảnh placeholder
                MediaAdapter mediaAdapter = new MediaAdapter(mediaUrls);
                viewPagerMedia.setAdapter(mediaAdapter);
//                new TabLayoutMediator(tabLayoutDots, viewPagerMedia, (tab, position) -> {
//                }).attach();
            }
        });


        // Load ảnh cho địa điểm
        imageViewModel.loadImageForLocation(locationId);
    }

    private void initViews() {
        viewPagerMedia = findViewById(R.id.viewPagerMedia);
        //tabLayoutDots = findViewById(R.id.tabLayoutDots);
        tvDescription = findViewById(R.id.tvDescription);
        rvReviews = findViewById(R.id.rvReviews);
        ivFavorite = findViewById(R.id.ivFavorite);
        btnViewMoreReviews = findViewById(R.id.btnViewMoreReviews);
        btnAddReview = findViewById(R.id.btnAddReview);
        viewPagerWeather = findViewById(R.id.viewPagerWeather);
        btnOpenMap = findViewById(R.id.btnOpenMap);
        rbAverageRating = findViewById(R.id.rbAverageRating);
        tvLocationTitle = findViewById(R.id.tvLocationTitle);
        LayerDrawable stars = (LayerDrawable) rbAverageRating.getProgressDrawable();

        stars.getDrawable(2).setColorFilter(Color.parseColor("#FFD700"), PorterDuff.Mode.SRC_ATOP); // full star
        stars.getDrawable(1).setColorFilter(Color.parseColor("#FFD700"), PorterDuff.Mode.SRC_ATOP); // half star
        stars.getDrawable(0).setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);

        btnViewMoreReviews.setOnClickListener(v -> reviewAdapter.showAllReviews());
        btnAddReview.setOnClickListener(v -> {

            // Kiểm tra thông tin người dùng từ UserCurrentViewModel
            userCurrentViewModel.getCurrentUser().observe(this, user -> {
                if (user != null && user.getUserId() != null) {
                    Intent intent = new Intent(LocationActivity.this, ReviewActivity.class);
                    intent.putExtra("user_id", user.getUserId()); // Truyền userId từ node User
                    intent.putExtra("location_id", locationId);
                    startActivityForResult(intent, REQUEST_CODE_REVIEW);
                } else {
                    Toast.makeText(this, "Vui lòng cập nhật thông tin người dùng để đánh giá!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void setupReviewsRecyclerView() {
        reviewAdapter = new ReviewAdapter(reviews, () -> btnViewMoreReviews.setVisibility(View.GONE));
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        rvReviews.setAdapter(reviewAdapter);
    }

//    private void setupWeatherViewPager() {
//        weatherRepository = new WeatherRepository();
//        weatherAdapter = new WeatherAdapter(new ArrayList<>()); // Khởi tạo adapter với danh sách rỗng
//        viewPagerWeather.setAdapter(weatherAdapter);
//        if (locationName != null && !locationName.isEmpty()) {
//            // Gọi API để lấy vị trí từ tên địa danh
//            weatherRepository.getLocationByName(locationName, new Callback<List<GeoResponse>>() {
//                @Override
//                public void onResponse(Call<List<GeoResponse>> call, Response<List<GeoResponse>> response) {
//                    if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
//                        GeoResponse geoResponse = response.body().get(0); // Lấy thông tin vị trí đầu tiên
//                        double lat = geoResponse.getLat(); // Vĩ độ
//                        double lon = geoResponse.getLon(); // Kinh độ
//
//                        // Gọi API thời tiết 7 ngày với lat và lon
//                        weatherRepository.get7DayWeather(lat, lon, new Callback<WeatherResponse>() {
//                            @Override
//                            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
//                                if (response.isSuccessful() && response.body() != null) {
//                                    WeatherResponse weatherResponse = response.body();
//                                    List<WeatherResponse.Daily> dailyList = weatherResponse.getDaily();
//                                    if (dailyList.size() > 7) {
//                                        dailyList = dailyList.subList(0, 7); // Lấy 7 ngày
//                                    }
//
//                                    // Tạo danh sách WeatherItem để hiển thị
//                                    List<WeatherItem> weatherItems = new ArrayList<>();
//                                    for (WeatherResponse.Daily daily : dailyList) {
//                                        String description = daily.getWeather().get(0).getDescription();
//                                        String temperature = String.valueOf(daily.getTemp().getDay());
//                                        String windSpeed = String.valueOf(daily.getWindSpeed());
//
//                                        WeatherItem weatherItem = new WeatherItem(description, temperature, windSpeed);
//                                        weatherItems.add(weatherItem);
//                                    }
//
//                                    // Cập nhật adapter với danh sách weatherItems
//                                    weatherAdapter.setWeatherList(weatherItems);
//                                } else {
//                                    Log.e("TAG", "Không thể tải dữ liệu thời tiết");
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
//                                Log.e("TAG", "Lỗi gọi API thời tiết", t);
//                            }
//                        });
//                    } else {
//                        Log.e("TAG", "Không tìm thấy vị trí cho địa danh: " + locationName);
//                    }
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<List<GeoResponse>> call, @NonNull Throwable t) {
//                    Log.e("TAG", "Lỗi gọi API địa lý", t);
//                }
//            });
//        }
//    }

    private void setupWeatherViewPager(Location location) {
        Log.d("WeatherDebug", "Bắt đầu setupWeatherViewPager, locationName: " + location.getTenDiaDiem());
        weatherRepository = new WeatherRepository();
        weatherAdapter = new WeatherAdapter(new ArrayList<>());
        Log.d("WeatherDebug", "weatherAdapter được khởi tạo: " + true);
        viewPagerWeather.setAdapter(weatherAdapter);
        Log.d("WeatherDebug", "Đã gán weatherAdapter cho viewPagerWeather");

        String tenDiaDiem = standardizeLocationName(location.getTenDiaDiem());
        if (tenDiaDiem != null && !tenDiaDiem.isEmpty()) {
            Log.d("WeatherDebug", "Lấy tọa độ cho tenDiaDiem: " + tenDiaDiem);
            weatherRepository.getCoordinatesByLocationName(tenDiaDiem, new Callback<List<GeoResponse>>() {
                @Override
                public void onResponse(Call<List<GeoResponse>> call, Response<List<GeoResponse>> response) {
                    if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                        GeoResponse geoResponse = response.body().get(0);
                        double lat = geoResponse.getLat();
                        double lon = geoResponse.getLon();
                        Log.d("WeatherDebug", "Tìm thấy vị trí: " + tenDiaDiem + " (lat: " + lat + ", lon: " + lon + ")");

                        weatherRepository.get7DayWeather(lat, lon, new Callback<WeatherResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    WeatherResponse weatherResponse = response.body();
                                    List<WeatherResponse.Daily> dailyList = weatherResponse.getDaily();
                                    Log.d("WeatherDebug", "Nhận được " + dailyList.size() + " ngày thời tiết");
                                    if (dailyList.size() > 7) {
                                        dailyList = dailyList.subList(0, 7);
                                    }

                                    List<WeatherItem> weatherItems = new ArrayList<>();
                                    for (WeatherResponse.Daily daily : dailyList) {
                                        String description = daily.getWeather().get(0).getDescription();
                                        String temperature = String.valueOf(daily.getTemp().getDay());
                                        String windSpeed = String.valueOf(daily.getWindSpeed());
                                        WeatherItem weatherItem = new WeatherItem(description, temperature, windSpeed);
                                        weatherItems.add(weatherItem);
                                    }
                                    Log.d("WeatherDebug", "Cập nhật adapter với " + weatherItems.size() + " items");
                                    weatherAdapter.setWeatherList(weatherItems);
                                    weatherAdapter.notifyDataSetChanged();
                                } else {
                                    Log.e("WeatherDebug", "Không thể tải dữ liệu thời tiết: " + response.message());
                                    Toast.makeText(LocationActivity.this, "Không thể tải dữ liệu thời tiết!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                                Log.e("WeatherDebug", "Lỗi gọi API thời tiết: " + t.getMessage());
                                Toast.makeText(LocationActivity.this, "Lỗi tải thời tiết: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Log.e("WeatherDebug", "Không tìm thấy vị trí cho địa danh: " + tenDiaDiem + ", mã trạng thái: " + response.code());
                        Toast.makeText(LocationActivity.this, "Không tìm thấy vị trí: " + tenDiaDiem, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<GeoResponse>> call, Throwable t) {
                    Log.e("WeatherDebug", "Lỗi lấy tọa độ: " + t.getMessage());
                    Toast.makeText(LocationActivity.this, "Lỗi tải vị trí: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e("WeatherDebug", "tenDiaDiem rỗng hoặc null");
            Toast.makeText(LocationActivity.this, "Tên địa điểm không hợp lệ!", Toast.LENGTH_SHORT).show();
        }
    }

    private String standardizeLocationName(String locationName) {
        if (locationName == null) return null;
        switch (locationName.toLowerCase()) {
            case "cố đô huế":
            case "thành phố huế":
                return "Hue";
            default:
                String normalized = java.text.Normalizer.normalize(locationName, java.text.Normalizer.Form.NFD);
                return normalized.replaceAll("[\\p{M}]", "");
        }
    }


    private void setupMapButton() {

        boolean hasGoogleMaps = getPackageManager().getLaunchIntentForPackage("com.google.android.apps.maps") != null;
        Log.d("LocationActivity", "Google Maps installed: " + hasGoogleMaps);

        btnOpenMap.setOnClickListener(v -> {
            if (locationName != null && !locationName.trim().isEmpty()) {
                String mapsUrl = "https://www.google.com/maps/search/?api=1&query=" + Uri.encode(locationName);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapsUrl));
                startActivity(browserIntent);
            } else {
                Toast.makeText(this, "Không có tên địa điểm để mở bản đồ!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private int generateReviewId() {
        return (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
    }
}
