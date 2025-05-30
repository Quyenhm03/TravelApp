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
import com.example.travel_app.ForecastResponse;
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

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    private TextView tvWeatherDay;
    private Button btnOpenMap;
    private ImageView ivFavorite;
    private TextView tvLocationTitle;
    private boolean isCurrentFavorite = false;
    private int locationId;
    private WeatherAdapter weatherAdapter;
    private WeatherRepository weatherRepository;
    private String locationName = "";
    private LocationSelectedViewModel locationSelectedViewModel;
    private final String[] dayLabels = {"Hôm nay", "Ngày mai", "Ngày kia", "Ngày 3", "Ngày 4"};


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
//        locationSelectedViewModel.getLocation().observe(this, location -> {
//            if (location != null) {
//                setupWeatherViewPager(location.getViTri());
//            } else {
//                Toast.makeText(this, "Không tìm thấy địa điểm", Toast.LENGTH_SHORT).show();
//            }
//        });
        setupReviewsRecyclerView();
        userCurrentViewModel.user.observe(this, user -> {
            if (user != null) {
                locationViewModel.getLocation(locationId).observe(this, location -> {
                    if (location != null) {
                        tvDescription.setText(location.getMoTa() != null ? location.getMoTa() : "Không có mô tả.");
                        setupMapButton();
                        tvLocationTitle.setText(location.getViTri());
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
                            locationViewModel.updateLocationFavorite(user.getUserId(), String.valueOf(locationId), newFavorite);

                            Toast.makeText(this, newFavorite ? "Đã thêm vào yêu thích" : "Đã bỏ yêu thích", Toast.LENGTH_SHORT).show();
                        });
                        locationName = location.getViTri();
                        setupWeatherViewPager(locationName);
                        Log.d("WeatherDebug", "locationName được cập nhật: " + locationName);
                    } else {
                        Toast.makeText(this, "Không tìm thấy thông tin địa điểm!", Toast.LENGTH_SHORT).show();
                        tvDescription.setText("Không có mô tả.");
                    }
                });

//                locationSelectedViewModel.getLocation().observe(this, location -> {
//                    if (location != null) {
//                        // Cập nhật thời tiết
//                        setupWeatherViewPager(location.getViTri());
//
//                        setupMapButton();
//                    }
//                });

            }});


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
        tvWeatherDay = findViewById(R.id.tvWeatherDay);
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


    private void setupWeatherViewPager(String tenDiaDiem) {
        if (tenDiaDiem == null || tenDiaDiem.isEmpty()) {
            Log.e("WeatherDebug", "Tên địa điểm rỗng hoặc null");
            Toast.makeText(this, "Không có tên địa điểm để tải thời tiết", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("WeatherDebug", "Bắt đầu setupWeatherViewPager, locationName: " + tenDiaDiem);
        weatherRepository = new WeatherRepository();
        weatherAdapter = new WeatherAdapter(new ArrayList<>());
        Log.d("WeatherDebug", "weatherAdapter được khởi tạo: " + true);
        viewPagerWeather.setAdapter(weatherAdapter);
        Log.d("WeatherDebug", "Đã gán weatherAdapter cho viewPagerWeather");

        Log.d("WeatherDebug", "Bắt đầu lấy thời tiết cho: " + tenDiaDiem);
        viewPagerWeather.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tvWeatherDay.setText(position < dayLabels.length ? dayLabels[position] : "Ngày " + (position + 1));
            }
        });
        String locationName = standardizeLocationName(tenDiaDiem);
        weatherRepository.getCoordinatesByLocationName(locationName, new Callback<List<GeoResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<GeoResponse>> call, @NonNull Response<List<GeoResponse>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    GeoResponse geoResponse = response.body().get(0);
                    double lat = geoResponse.getLat();
                    double lon = geoResponse.getLon();
                    Log.d("WeatherDebug", "Tìm thấy vị trí: " + tenDiaDiem + " (lat: " + lat + ", lon: " + lon + ")");

                    weatherRepository.get5DayWeather(lat, lon, new Callback<ForecastResponse>() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onResponse(@NonNull Call<ForecastResponse> call, @NonNull Response<ForecastResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                ForecastResponse forecastResponse = response.body();
                                List<ForecastResponse.ForecastItem> forecastList = forecastResponse.getList();
                                Log.d("WeatherDebug", "Nhận được " + forecastList.size() + " khoảng thời tiết");

                                // Nhóm dữ liệu theo ngày
                                List<WeatherItem> weatherItems = new ArrayList<>();
                                Map<String, List<ForecastResponse.ForecastItem>> dailyMap = new HashMap<>();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                                for (ForecastResponse.ForecastItem item : forecastList) {
                                    String date = dateFormat.format(new Date(item.getDt() * 1000));
                                    dailyMap.computeIfAbsent(date, k -> new ArrayList<>()).add(item);
                                }

                                // Tổng hợp dữ liệu cho mỗi ngày
                                for (List<ForecastResponse.ForecastItem> dailyItems : dailyMap.values()) {
                                    ForecastResponse.ForecastItem representative = null;
                                    float tempSum = 0;
                                    for (ForecastResponse.ForecastItem item : dailyItems) {
                                        tempSum += item.getMain().getTemp();
                                        String time = item.getDtTxt().substring(11, 13);
                                        if (time.equals("12")) {
                                            representative = item;
                                        }
                                    }
                                    if (representative == null) {
                                        representative = dailyItems.get(dailyItems.size() / 2);
                                    }

                                    String description = representative.getWeather().get(0).getDescription();
                                    @SuppressLint("DefaultLocale") String temperature = String.format("%.1f", tempSum / dailyItems.size());
                                    String windSpeed = String.valueOf(representative.getWind().getSpeed());
                                    WeatherItem weatherItem = new WeatherItem(description, temperature, windSpeed);
                                    weatherItems.add(weatherItem);
                                }

                                Log.d("WeatherDebug", "Cập nhật adapter với " + weatherItems.size() + " items");
                                weatherAdapter.setWeatherList(weatherItems);
                                weatherAdapter.notifyDataSetChanged();
                            } else {
                                String errorMessage = response.message();
                                Log.e("WeatherDebug", "Không thể tải dữ liệu thời tiết: " + errorMessage + ", mã trạng thái: " + response.code() + ", URL: " + call.request().url());
                                Toast.makeText(LocationActivity.this, "Không thể tải dữ liệu thời tiết: " + errorMessage, Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ForecastResponse> call, @NonNull Throwable t) {
                            Log.e("WeatherDebug", "Lỗi gọi API thời tiết: " + t.getMessage() + ", URL: " + call.request().url());
                            Toast.makeText(LocationActivity.this, "Lỗi tải thời tiết: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    String errorMessage = response.message();
                    Log.e("WeatherDebug", "Không tìm thấy vị trí cho địa danh: " + tenDiaDiem + ", mã trạng thái: " + response.code() + ", message: " + errorMessage + ", URL: " + call.request().url());
                    Toast.makeText(LocationActivity.this, "Không tìm thấy vị trí: " + tenDiaDiem + " (" + errorMessage + ")", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<GeoResponse>> call, @NonNull Throwable t) {
                Log.e("WeatherDebug", "Lỗi lấy tọa độ: " + t.getMessage() + ", URL: " + call.request().url());
                Toast.makeText(LocationActivity.this, "Lỗi tải vị trí: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private String standardizeLocationName(String locationName) {
        if (locationName == null) return null;
        switch (locationName.toLowerCase()) {
            case "cố đô huế":
            case "thành phố huế", "Thừa Thiên Huế", "huế":
                return "Thua Thien Hue";
            case "phố cổ hội an":
                return "Hoi An";
            case "đà nẵng":
                return "Da Nang";
            case "hà nội":
                return "Hanoi";
            case "thành phố hồ chí minh":
            case "sài gòn":
                return "Ho Chi Minh City";
            case "hải phòng":
                return "Hai Phong";
            case "quảng ninh":
                return "Quang Ninh";
            case "quảng nam":
                return "Quang Nam";
            case "quảng ngãi":
                return "Quang Ngai";
            case "lào cai":
                return "Lao Cai";
            case "kiên giang":
                return "Kien Giang";
            case "thái bình":
                return "Thai Binh";
            case "ninh bình":
                return "Ninh Binh";
            case "phan thiết":
                return "Phan Thiet";
            case "phú yên":
                return "Phu Yen";
            case "bến tre":
                return "Ben Tre";
            case "long an":
                return "Long An";
            case "cần thơ":
                return "Can Tho";
            case "thái nguyên":
                return "Thai Nguyen";
            case "cao bằng":
                return "Cao Bang";
            case "bắc kạn":
                return "Bac Kan";
            case "khánh hoà":
                return "Khanh Hoa";
            case "lâm đồng":
                return "Lam Dong";
            case "an giang":
                return "An Giang";
            case "bạc liêu":
                return "Bac Lieu";
            case "sóc trăng":
                return "Soc Trang";
            case "vĩnh phúc":
                return "Vinh Phuc";
            case "đồng tháp":
                return "Dong Thap";
            case "đồng nai":
                return "Dong Nai";

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