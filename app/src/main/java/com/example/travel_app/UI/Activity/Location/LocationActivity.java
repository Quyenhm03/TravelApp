package com.example.travel_app.UI.Activity.Location;

import static android.webkit.URLUtil.isValidUrl;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.travel_app.Adapter.MediaAdapter;
import com.example.travel_app.Adapter.ReviewAdapter;
import com.example.travel_app.Adapter.WeatherAdapter;
import com.example.travel_app.Data.Model.Image;
import com.example.travel_app.Data.Model.Review;
import com.example.travel_app.Data.Model.ReviewWithUser;
import com.example.travel_app.R;
import com.example.travel_app.ViewModel.LocationViewModel;
import com.example.travel_app.ViewModel.MediaViewModel;
import com.example.travel_app.ViewModel.ReviewViewModel;
import com.example.travel_app.ViewModel.Itinerary.ImageViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity {

    private ReviewAdapter reviewAdapter;
    private List<ReviewWithUser> reviews;
    private ReviewViewModel reviewViewModel;
    private LocationViewModel locationViewModel;
    private MediaViewModel mediaViewModel;
    private ImageViewModel imageViewModel;
    private static final int REQUEST_CODE_REVIEW = 100;

    private ViewPager2 viewPagerMedia;
    private TabLayout tabLayoutDots;
    private TextView tvDescription;
    private RecyclerView rvReviews;
    private Button btnViewMoreReviews;
    private Button btnAddReview;
    private ViewPager2 viewPagerWeather;
    private Button btnOpenMap;

    private int locationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        locationId = getIntent().getIntExtra("location_id", -1);
        if (locationId == -1) {
            locationId = 1; // Giá trị mặc định
            Toast.makeText(this, "Không tìm thấy địa điểm", Toast.LENGTH_SHORT).show();
        }

        reviews = new ArrayList<>();

        initViews();
        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        mediaViewModel = new ViewModelProvider(this).get(MediaViewModel.class);
        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);

        setupMediaViewPager();
        setupReviewsRecyclerView();
        setupWeatherViewPager();

        // Quan sát thông tin địa điểm
        locationViewModel.getLocation(locationId).observe(this, location -> {
            if (location != null) {
                Log.d("LocationActivity", "Dữ liệu địa điểm: " + location.getTenDiaDiem());
                tvDescription.setText(location.getMoTa() != null ? location.getMoTa() : "Không có mô tả.");
                setupMapButton();
            } else {
                Log.e("LocationActivity", "Không tìm thấy dữ liệu địa điểm cho locationId: " + locationId);
                Toast.makeText(this, "Không tìm thấy thông tin địa điểm!", Toast.LENGTH_SHORT).show();
                tvDescription.setText("Không có mô tả.");
            }
        });

        // Quan sát đánh giá
        reviewViewModel.getReviews(locationId).observe(this, newReviews -> {
            if (newReviews != null && !newReviews.isEmpty()) {
                Log.d("LocationActivity", "Số đánh giá: " + newReviews.size());
                reviews.clear();
                reviews.addAll(newReviews);
                reviewAdapter.updateReviews(reviews);
                btnViewMoreReviews.setVisibility(reviews.size() > 3 ? View.VISIBLE : View.GONE);
            } else {
                Log.w("LocationActivity", "Không tìm thấy đánh giá cho locationId: " + locationId);
                Toast.makeText(this, "Chưa có đánh giá nào cho địa điểm này!", Toast.LENGTH_SHORT).show();
                reviews.clear();
                reviewAdapter.updateReviews(reviews);
                btnViewMoreReviews.setVisibility(View.GONE);
            }
        });

        imageViewModel.getImageUrlMapLiveData().observe(this, imageUrlMap -> {
            String imageUrl = imageUrlMap.get(locationId);
            if (imageUrl != null && isValidUrl(imageUrl)) {
                Log.d("LocationActivityIMAGE", "Ảnh cho locationId = " + locationId + " đã được load: " + imageUrl);
                List<String> mediaUrls = new ArrayList<>();
                mediaUrls.add(imageUrl);
                MediaAdapter mediaAdapter = new MediaAdapter(mediaUrls);
                viewPagerMedia.setAdapter(mediaAdapter);
                new TabLayoutMediator(tabLayoutDots, viewPagerMedia, (tab, position) -> {}).attach();
            } else {
                Log.w("LocationActivityIMAGE", "Không tìm thấy ảnh hợp lệ cho locationId = " + locationId);
                // Thay ảnh placeholder nếu URL không hợp lệ
                List<String> mediaUrls = new ArrayList<>();
                mediaUrls.add("https://example.com/placeholder_image.jpg");  // URL ảnh placeholder
                MediaAdapter mediaAdapter = new MediaAdapter(mediaUrls);
                viewPagerMedia.setAdapter(mediaAdapter);
                new TabLayoutMediator(tabLayoutDots, viewPagerMedia, (tab, position) -> {}).attach();
            }
        });


        // Load ảnh cho địa điểm
        imageViewModel.loadImageForLocation(locationId);
    }

    private void initViews() {
        viewPagerMedia = findViewById(R.id.viewPagerMedia);
        tabLayoutDots = findViewById(R.id.tabLayoutDots);
        tvDescription = findViewById(R.id.tvDescription);
        rvReviews = findViewById(R.id.rvReviews);
        btnViewMoreReviews = findViewById(R.id.btnViewMoreReviews);
        btnAddReview = findViewById(R.id.btnAddReview);
        viewPagerWeather = findViewById(R.id.viewPagerWeather);
        btnOpenMap = findViewById(R.id.btnOpenMap);

        btnViewMoreReviews.setOnClickListener(v -> reviewAdapter.showAllReviews());
        btnAddReview.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                Intent intent = new Intent(LocationActivity.this, ReviewActivity.class);
                intent.putExtra("user_id", user.getUid());
                intent.putExtra("location_id", locationId);
                startActivityForResult(intent, REQUEST_CODE_REVIEW);
            } else {
                Toast.makeText(this, "Vui lòng đăng nhập để đánh giá!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupMediaViewPager() {
        // Đã chuyển sang sử dụng dữ liệu từ imageViewModel
    }

    private void setupReviewsRecyclerView() {
        reviewAdapter = new ReviewAdapter(reviews, () -> btnViewMoreReviews.setVisibility(View.GONE));
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        rvReviews.setAdapter(reviewAdapter);
    }

    private void setupWeatherViewPager() {
        // Tạo danh sách các dự báo thời tiết
        List<String> weatherList = new ArrayList<>();
        weatherList.add("Ngày 1: 25°C, Nắng");
        weatherList.add("Ngày 2: 24°C, Mưa nhẹ");
        weatherList.add("Ngày 3: 26°C, Nhiều mây");

        // Tạo và thiết lập adapter cho ViewPager2 sử dụng WeatherAdapter
        WeatherAdapter weatherAdapter = new WeatherAdapter(weatherList); // Sử dụng WeatherAdapter thay vì MediaAdapter
        viewPagerWeather.setAdapter(weatherAdapter);

        // Nếu bạn muốn hiển thị các dots tương tự như phần Media
        new TabLayoutMediator(tabLayoutDots, viewPagerWeather, (tab, position) -> {
            // Bạn có thể tùy chỉnh cách hiển thị của TabLayout ở đây nếu cần
        }).attach();
    }


    private void setupMapButton() {
        btnOpenMap.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng bản đồ chưa được triển khai!", Toast.LENGTH_SHORT).show();
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_REVIEW && resultCode == RESULT_OK && data != null) {
            float rating = data.getFloatExtra("rating", 0f);
            String comment = data.getStringExtra("comment");
            Log.d("LocationActivity", "Nhận đánh giá: rating=" + rating + ", comment=" + comment);

            if (comment != null && !comment.trim().isEmpty() && rating >= 0 && rating <= 5) {
                String createAt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                        .format(new Date());
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Review newReview = new Review();
                    newReview.setComment(comment);
                    newReview.setCreateAt(createAt);
                    newReview.setLocationId(locationId);
                    newReview.setUserId(user.getUid());
                    newReview.setRating(rating);

                    reviewViewModel.addReview(newReview);
                    Toast.makeText(this, "Đã gửi đánh giá, đang xử lý!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.w("LocationActivity", "Người dùng chưa đăng nhập khi thêm đánh giá");
                    Toast.makeText(this, "Vui lòng đăng nhập để đánh giá!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.w("LocationActivity", "Dữ liệu đánh giá không hợp lệ: rating=" + rating + ", comment=" + comment);
                Toast.makeText(this, "Vui lòng nhập đầy đủ và đúng thông tin đánh giá!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.w("LocationActivity", "Nhận kết quả không hợp lệ: requestCode=" + requestCode + ", resultCode=" + resultCode);
        }
    }

}
