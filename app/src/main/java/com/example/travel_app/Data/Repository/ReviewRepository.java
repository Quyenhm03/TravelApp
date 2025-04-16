package com.example.travel_app.Data.Repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.travel_app.Data.Model.Review;
import com.example.travel_app.Data.Model.ReviewWithUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReviewRepository {
    private static final String TAG = "ReviewRepository";

    private DatabaseReference mReviewsRef;
    private DatabaseReference mUsersRef;
    private MutableLiveData<List<ReviewWithUser>> reviewsLiveData;

    public ReviewRepository() {
        mReviewsRef = FirebaseDatabase.getInstance().getReference("Review");
        mUsersRef = FirebaseDatabase.getInstance().getReference("User");
        reviewsLiveData = new MutableLiveData<>();
        fetchAllReviews();
    }

    private void fetchAllReviews() {
        mReviewsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot reviewSnapshot) {
                List<ReviewWithUser> reviewList = new ArrayList<>();
                long totalReviews = reviewSnapshot.getChildrenCount();

                Log.d(TAG, "Dữ liệu JSON thô cho tất cả đánh giá: " + reviewSnapshot.getValue());
                if (totalReviews == 0) {
                    Log.d(TAG, "Không tìm thấy đánh giá nào");
                    reviewsLiveData.setValue(reviewList);
                    return;
                }

                for (DataSnapshot snapshot : reviewSnapshot.getChildren()) {
                    Log.d(TAG, "Dữ liệu JSON thô cho đánh giá: " + snapshot.getValue());
                    // Kiểm tra xem snapshot có chứa dữ liệu hợp lệ không
                    if (!snapshot.exists() || snapshot.getValue() == null) {
                        Log.w(TAG, "Snapshot không hợp lệ hoặc rỗng: " + snapshot.getKey());
                        continue;
                    }

                    Review review = snapshot.getValue(Review.class);
                    if (review == null) {
                        Log.w(TAG, "Không thể phân tích đánh giá cho snapshot: " + snapshot.getKey());
                        continue;
                    }

                    // Log chi tiết giá trị của review
                    Log.d(TAG, "Review parsed: reviewId=" + review.getReviewId() +
                            ", userId=" + review.getUserId() +
                            ", locationId=" + review.getLocationId() +
                            ", comment=" + review.getComment() +
                            ", rating=" + review.getRating() +
                            ", createAt=" + review.getCreateAt());

                    // Kiểm tra tính hợp lệ của review
                    if (review.getLocationId() == 0) {
                        Log.e(TAG, "Dữ liệu đánh giá không hợp lệ cho snapshot: " + snapshot.getKey());
                        continue;
                    }

                    ReviewWithUser reviewWithUser = new ReviewWithUser();
                    reviewWithUser.setReviewId(review.getReviewId());
                    reviewWithUser.setComment(review.getComment());
                    reviewWithUser.setCreateAt(review.getCreateAt());
                    reviewWithUser.setRating(review.getRating());
                    reviewWithUser.setLocationId(review.getLocationId());
                    reviewWithUser.setUserId(review.getUserId());

                    String userId = review.getUserId();
                    if (userId == null || userId.isEmpty()) {
                        Log.w(TAG, "userId không hợp lệ (null hoặc rỗng) cho reviewId: " + review.getReviewId());
                        reviewWithUser.setFullName("Ẩn danh");
                        reviewList.add(reviewWithUser);
                        if (reviewList.size() == totalReviews) {
                            reviewsLiveData.setValue(reviewList);
                        }
                        continue;
                    }

                    mUsersRef.child(userId).get().addOnSuccessListener(userSnap -> {
                        Log.d(TAG, "Dữ liệu JSON thô cho người dùng với userId " + userId + ": " + userSnap.getValue());
                        if (userSnap.exists()) {
                            String fullName = userSnap.child("fullName").getValue(String.class);
                            reviewWithUser.setFullName(fullName != null ? fullName : "Ẩn danh");
                        } else {
                            Log.w(TAG, "Không tìm thấy người dùng cho userId: " + userId + " trong reviewId: " + review.getReviewId());
                            reviewWithUser.setFullName("Ẩn danh");
                        }
                        reviewList.add(reviewWithUser);

                        if (reviewList.size() == totalReviews) {
                            Log.d(TAG, "Đã xử lý xong tất cả đánh giá, tổng: " + reviewList.size());
                            reviewsLiveData.setValue(reviewList);
                        }
                    }).addOnFailureListener(e -> {
                        Log.e(TAG, "Lỗi khi lấy thông tin người dùng cho userId: " + userId + " - " + e.getMessage());
                        reviewWithUser.setFullName("Ẩn danh");
                        reviewList.add(reviewWithUser);

                        if (reviewList.size() == totalReviews) {
                            Log.d(TAG, "Đã xử lý xong tất cả đánh giá (có lỗi), tổng: " + reviewList.size());
                            reviewsLiveData.setValue(reviewList);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Lỗi khi lấy đánh giá: " + error.getMessage());
                reviewsLiveData.setValue(new ArrayList<>());
            }
        });
    }

    public LiveData<List<ReviewWithUser>> getReviewsForLocation(int locationId) {
        MutableLiveData<List<ReviewWithUser>> filteredReviewsLiveData = new MutableLiveData<>();
        mReviewsRef.orderByChild("locationId").equalTo(locationId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<ReviewWithUser> reviewList = new ArrayList<>();
                for (DataSnapshot reviewSnapshot : snapshot.getChildren()) {
                    Review review = reviewSnapshot.getValue(Review.class);
                    if (review != null) {
                        ReviewWithUser reviewWithUser = new ReviewWithUser();
                        reviewWithUser.setReviewId(review.getReviewId());
                        reviewWithUser.setComment(review.getComment());
                        reviewWithUser.setCreateAt(review.getCreateAt());
                        reviewWithUser.setRating(review.getRating());
                        reviewWithUser.setLocationId(review.getLocationId());
                        reviewWithUser.setUserId(review.getUserId());

                        String userId = review.getUserId();
                        if (userId != null && !userId.isEmpty()) {
                            mUsersRef.child(userId).get().addOnSuccessListener(userSnap -> {
                                if (userSnap.exists()) {
                                    String fullName = userSnap.child("fullName").getValue(String.class);
                                    reviewWithUser.setFullName(fullName != null ? fullName : "Ẩn danh");
                                } else {
                                    reviewWithUser.setFullName("Ẩn danh");
                                }
                                reviewList.add(reviewWithUser);
                                if (reviewList.size() == snapshot.getChildrenCount()) {
                                    filteredReviewsLiveData.setValue(reviewList);
                                }
                            });
                        } else {
                            reviewWithUser.setFullName("Ẩn danh");
                            reviewList.add(reviewWithUser);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                filteredReviewsLiveData.setValue(new ArrayList<>());
            }
        });
        return filteredReviewsLiveData;
    }


    public LiveData<List<ReviewWithUser>> getReviews() {
        return reviewsLiveData;
    }

    public void addReview(Review review) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            if (!userId.isEmpty()) {
                review.setUserId(userId);
                // Dùng auto-generated ID của Firebase thay vì tính toán thủ công
                String key = mReviewsRef.push().getKey();
                if (key != null) {
                    mReviewsRef.child(key).setValue(review)
                            .addOnFailureListener(e -> Log.e(TAG, "Không thể thêm đánh giá: " + e.getMessage()));
                }
            } else {
                Log.e(TAG, "Không tìm thấy userId hợp lệ, không thể thêm đánh giá");
            }
        } else {
            Log.e(TAG, "Người dùng chưa đăng nhập, không thể thêm đánh giá");
        }
    }
}
