package com.example.travel_app.UI.Activity.Location;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Adapter.MediaAdapter;
import com.example.travel_app.Adapter.ReviewAdapter;
import com.example.travel_app.Adapter.listener.OnViewMoreClickListener;
import com.example.travel_app.Data.Model.Review;
import com.example.travel_app.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity {
    private ReviewAdapter reviewAdapter;
    private List<Review> reviews;
    private static final int REQUEST_CODE_REVIEW = 100;
    private MediaAdapter mediaAdapter;
    private TabLayout tabLayout;
    RecyclerView rvReviews;
    Button btnViewMoreReviews, btnAddReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        // Khởi tạo danh sách đánh giá (ví dụ)
        reviews = new ArrayList<>();
        reviews.add(new Review("Người dùng 1", 4, "Rất đẹp, không khí trong lành!"));
        reviews.add(new Review("Người dùng 2", 3, "Cảnh đẹp nhưng đường đi hơi khó."));
        reviews.add(new Review("Người dùng 3", 5.0f, "Tuyệt vời, đáng để ghé thăm!"));
        reviews.add(new Review("Người dùng 4", 4.0f, "Khá ổn, nhưng cần cải thiện dịch vụ."));
        initViews();
        // Thiết lập RecyclerView


        reviewAdapter = new ReviewAdapter(reviews, () -> btnViewMoreReviews.setVisibility(View.GONE));
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        rvReviews.setAdapter(reviewAdapter);

        // Hiển thị nút "Xem thêm" nếu có nhiều hơn 3 đánh giá
        if (reviews.size() > 3) {
            btnViewMoreReviews.setVisibility(View.VISIBLE);
        }

        // Xử lý nút "Xem thêm"
        btnViewMoreReviews.setOnClickListener(v -> reviewAdapter.showAllReviews());

        // Xử lý nút "Đánh giá của bạn"
        btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationActivity.this, ReviewActivity.class);
                startActivityForResult(intent, REQUEST_CODE_REVIEW);
            }
        });
    }

    private void initViews() {
        rvReviews = findViewById(R.id.rvReviews);
        btnViewMoreReviews = findViewById(R.id.btnViewMoreReviews);
        btnAddReview = findViewById(R.id.btnAddReview);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_REVIEW && resultCode == RESULT_OK) {
            float rating = data != null ? data.getFloatExtra("rating", 0f) : 0f;
            String comment = data != null ? data.getStringExtra("comment") : "";
            if (comment != null && !comment.isEmpty()) {
                reviews.add(new Review("Bạn", rating, comment));
                reviewAdapter.notifyDataSetChanged();
                Button btnViewMoreReviews = findViewById(R.id.btnViewMoreReviews);
                if (reviews.size() > 3) {
                    btnViewMoreReviews.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}