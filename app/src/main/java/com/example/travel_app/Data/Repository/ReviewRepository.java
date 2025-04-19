package com.example.travel_app.Data.Repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;

import com.example.travel_app.Data.Model.Review;
import com.example.travel_app.Data.Model.ReviewWithUser;
import com.example.travel_app.ViewModel.UserCurrentViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//public class ReviewRepository {
//    private static final String TAG = "ReviewRepository";
//    private DatabaseReference mReviewsRef;
//    private DatabaseReference mUsersRef;
//    private MutableLiveData<List<ReviewWithUser>> reviewsLiveData;
//
//    public ReviewRepository() {
//        mReviewsRef = FirebaseDatabase.getInstance().getReference("Review");
//        mUsersRef = FirebaseDatabase.getInstance().getReference("User");
//        reviewsLiveData = new MutableLiveData<>();
//        fetchAllReviews();
//    }
//
//    private void fetchAllReviews() {
//        mReviewsRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot reviewSnapshot) {
//                List<ReviewWithUser> reviewList = new ArrayList<>();
//                long totalReviews = reviewSnapshot.getChildrenCount();
//
//                Log.d(TAG, "Dữ liệu JSON thô cho tất cả đánh giá: " + reviewSnapshot.getValue());
//                if (totalReviews == 0) {
//                    Log.d(TAG, "Không tìm thấy đánh giá nào");
//                    reviewsLiveData.setValue(reviewList);
//                    return;
//                }
//
//                for (DataSnapshot snapshot : reviewSnapshot.getChildren()) {
//                    Log.d(TAG, "Dữ liệu JSON thô cho đánh giá: " + snapshot.getValue());
//                    // Kiểm tra xem snapshot có chứa dữ liệu hợp lệ không
//                    if (!snapshot.exists() || snapshot.getValue() == null) {
//                        Log.w(TAG, "Snapshot không hợp lệ hoặc rỗng: " + snapshot.getKey());
//                        continue;
//                    }
//
//                    Review review = snapshot.getValue(Review.class);
//                    if (review == null) {
//                        Log.w(TAG, "Không thể phân tích đánh giá cho snapshot: " + snapshot.getKey());
//                        continue;
//                    }
//
//                    // Log chi tiết giá trị của review
//                    Log.d(TAG, "Review parsed: reviewId=" + review.getReviewId() +
//                            ", userId=" + review.getUserId() +
//                            ", locationId=" + review.getLocationId() +
//                            ", comment=" + review.getComment() +
//                            ", rating=" + review.getRating() +
//                            ", createAt=" + review.getCreateAt());
//
//                    // Kiểm tra tính hợp lệ của review
//                    if (review.getLocationId() == 0) {
//                        Log.e(TAG, "Dữ liệu đánh giá không hợp lệ cho snapshot: " + snapshot.getKey());
//                        continue;
//                    }
//
//                    ReviewWithUser reviewWithUser = new ReviewWithUser();
//                    reviewWithUser.setReviewId(review.getReviewId());
//                    reviewWithUser.setComment(review.getComment());
//                    reviewWithUser.setCreateAt(review.getCreateAt());
//                    reviewWithUser.setRating(review.getRating());
//                    reviewWithUser.setLocationId(review.getLocationId());
//                    reviewWithUser.setUserId(review.getUserId());
//
//                    String userId = String.valueOf(review.getUserId());
//                    if (!userId.isEmpty()) {
//                        Log.w(TAG, "userId không hợp lệ (null hoặc rỗng) cho reviewId: " + review.getReviewId());
//                        reviewWithUser.setFullName("Ẩn danh");
//                        reviewList.add(reviewWithUser);
//                        if (reviewList.size() == totalReviews) {
//                            reviewsLiveData.setValue(reviewList);
//                        }
//                        continue;
//                    }
//
//                    mUsersRef.child(String.valueOf(userId)).get().addOnSuccessListener(userSnap -> {
//                        Log.d(TAG, "Dữ liệu JSON thô cho người dùng với userId " + userId + ": " + userSnap.getValue());
//                        if (userSnap.exists()) {
//                            String fullName = userSnap.child("fullName").getValue(String.class);
//                            reviewWithUser.setFullName(fullName != null ? fullName : "Ẩn danh");
//                        } else {
//                            Log.w(TAG, "Không tìm thấy người dùng cho userId: " + userId + " trong reviewId: " + review.getReviewId());
//                            reviewWithUser.setFullName("Ẩn danh");
//                        }
//                        reviewList.add(reviewWithUser);
//
//                        if (reviewList.size() == totalReviews) {
//                            Log.d(TAG, "Đã xử lý xong tất cả đánh giá, tổng: " + reviewList.size());
//                            reviewsLiveData.setValue(reviewList);
//                        }
//                    }).addOnFailureListener(e -> {
//                        Log.e(TAG, "Lỗi khi lấy thông tin người dùng cho userId: " + userId + " - " + e.getMessage());
//                        reviewWithUser.setFullName("Ẩn danh");
//                        reviewList.add(reviewWithUser);
//
//                        if (reviewList.size() == totalReviews) {
//                            Log.d(TAG, "Đã xử lý xong tất cả đánh giá (có lỗi), tổng: " + reviewList.size());
//                            reviewsLiveData.setValue(reviewList);
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e(TAG, "Lỗi khi lấy đánh giá: " + error.getMessage());
//                reviewsLiveData.setValue(new ArrayList<>());
//            }
//        });
//    }
//
//    public LiveData<List<ReviewWithUser>> getReviewsForLocation(int locationId) {
//        MutableLiveData<List<ReviewWithUser>> filteredReviewsLiveData = new MutableLiveData<>();
//        mReviewsRef.orderByChild("locationId").equalTo(locationId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<ReviewWithUser> reviewList = new ArrayList<>();
//                for (DataSnapshot reviewSnapshot : snapshot.getChildren()) {
//                    Review review = reviewSnapshot.getValue(Review.class);
//                    if (review != null) {
//                        ReviewWithUser reviewWithUser = new ReviewWithUser();
//                        reviewWithUser.setReviewId(review.getReviewId());
//                        reviewWithUser.setComment(review.getComment());
//                        reviewWithUser.setCreateAt(review.getCreateAt());
//                        reviewWithUser.setRating(review.getRating());
//                        reviewWithUser.setLocationId(review.getLocationId());
//                        reviewWithUser.setUserId((review.getUserId()));
//
//                        int userId = review.getUserId();
//                        if (userId != 0 ) {
//                            mUsersRef.child(String.valueOf(userId)).get().addOnSuccessListener(userSnap -> {
//                                if (userSnap.exists()) {
//                                    String fullName = userSnap.child("fullName").getValue(String.class);
//                                    reviewWithUser.setFullName(fullName != null ? fullName : "Ẩn danh");
//                                } else {
//                                    reviewWithUser.setFullName("Ẩn danh");
//                                }
//                                reviewList.add(reviewWithUser);
//                                if (reviewList.size() == snapshot.getChildrenCount()) {
//                                    filteredReviewsLiveData.setValue(reviewList);
//                                }
//                            });
//                        } else {
//                            reviewWithUser.setFullName("Ẩn danh");
//                            reviewList.add(reviewWithUser);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                filteredReviewsLiveData.setValue(new ArrayList<>());
//            }
//        });
//        return filteredReviewsLiveData;
//    }
//
//
//    public LiveData<List<ReviewWithUser>> getReviews() {
//        return reviewsLiveData;
//    }
//
//    public void addReview(Review review) {
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (currentUser != null) {
//            String userId = currentUser.getUid();
//            if (!userId.isEmpty()) {
//                review.setUserId(Integer.parseInt(userId));
//                // Dùng auto-generated ID của Firebase thay vì tính toán thủ công
//                String key = mReviewsRef.push().getKey();
//                if (key != null) {
//                    mReviewsRef.child(key).setValue(review)
//                            .addOnFailureListener(e -> Log.e(TAG, "Không thể thêm đánh giá: " + e.getMessage()));
//                }
//            } else {
//                Log.e(TAG, "Không tìm thấy userId hợp lệ, không thể thêm đánh giá");
//            }
//        } else {
//            Log.e(TAG, "Người dùng chưa đăng nhập, không thể thêm đánh giá");
//        }
//    }
//}

public class ReviewRepository {
    private static final String TAG = "ReviewRepository";

    private final DatabaseReference mReviewsRef;
    private final DatabaseReference mUsersRef;
    private final MutableLiveData<List<ReviewWithUser>> reviewsLiveData;

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
                    if (!snapshot.exists() || snapshot.getValue() == null) {
                        Log.w(TAG, "Snapshot không hợp lệ hoặc rỗng: " + snapshot.getKey());
                        continue;
                    }

                    try {
                        Review review = snapshot.getValue(Review.class);
                        if (review == null) {
                            Log.w(TAG, "Không thể phân tích đánh giá cho snapshot: " + snapshot.getKey());
                            continue;
                        }

                        Log.d(TAG, "Review parsed: reviewId=" + review.getReviewId() +
                                ", userId=" + review.getUserId() +
                                ", locationId=" + review.getLocationId() +
                                ", comment=" + review.getComment() +
                                ", rating=" + review.getRating() +
                                ", createAt=" + review.getCreateAt());

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
                        reviewWithUser.setUserId(review.getUserId()); // userId là int

                        int userId = review.getUserId();
                        if (userId == 0) {
                            Log.w(TAG, "userId không hợp lệ (0) cho reviewId: " + review.getReviewId());
                            reviewWithUser.setFullName("Ẩn danh");
                            reviewList.add(reviewWithUser);
                            if (reviewList.size() == totalReviews) {
                                reviewsLiveData.setValue(reviewList);
                            }
                            continue;
                        }

                        mUsersRef.child(String.valueOf(userId)).get().addOnSuccessListener(userSnap -> {
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
                            Log.e(TAG, "Lỗi khi lấy thông tin người dùng cho userId: " + userId + ", lỗi: " + e.getMessage());
                            reviewWithUser.setFullName("Ẩn danh");
                            reviewList.add(reviewWithUser);

                            if (reviewList.size() == totalReviews) {
                                Log.d(TAG, "Đã xử lý xong tất cả đánh giá (có lỗi), tổng: " + reviewList.size());
                                reviewsLiveData.setValue(reviewList);
                            }
                        });
                    } catch (Exception e) {
                        Log.e(TAG, "Lỗi khi ánh xạ dữ liệu tại: " + snapshot.getKey() + ", lỗi: " + e.getMessage());
                    }
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
        mReviewsRef.orderByChild("location_id").equalTo(locationId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<ReviewWithUser> reviewList = new ArrayList<>();
                long totalReviews = snapshot.getChildrenCount();

                if (totalReviews == 0) {
                    filteredReviewsLiveData.setValue(reviewList);
                    return;
                }

                for (DataSnapshot reviewSnapshot : snapshot.getChildren()) {
                    try {
                        Review review = reviewSnapshot.getValue(Review.class);
                        if (review == null) {
                            Log.w(TAG, "Không thể phân tích đánh giá cho snapshot: " + reviewSnapshot.getKey());
                            continue;
                        }

                        ReviewWithUser reviewWithUser = new ReviewWithUser();
                        reviewWithUser.setReviewId(review.getReviewId());
                        reviewWithUser.setComment(review.getComment());
                        reviewWithUser.setCreateAt(review.getCreateAt());
                        reviewWithUser.setRating(review.getRating());
                        reviewWithUser.setLocationId(review.getLocationId());
                        reviewWithUser.setUserId(review.getUserId()); // userId là int

                        int userId = review.getUserId();
                        if (userId == 0) {
                            Log.w(TAG, "userId không hợp lệ (0) cho reviewId: " + review.getReviewId());
                            reviewWithUser.setFullName("Ẩn danh");
                            reviewList.add(reviewWithUser);
                            if (reviewList.size() == totalReviews) {
                                filteredReviewsLiveData.setValue(reviewList);
                            }
                            continue;
                        }

                        mUsersRef.child(String.valueOf(userId)).get().addOnSuccessListener(userSnap -> {
                            if (userSnap.exists()) {
                                String fullName = userSnap.child("fullName").getValue(String.class);
                                reviewWithUser.setFullName(fullName != null ? fullName : "Ẩn danh");
                            } else {
                                Log.w(TAG, "Không tìm thấy người dùng cho userId: " + userId);
                                reviewWithUser.setFullName("Ẩn danh");
                            }
                            reviewList.add(reviewWithUser);
                            if (reviewList.size() == totalReviews) {
                                filteredReviewsLiveData.setValue(reviewList);
                            }
                        }).addOnFailureListener(e -> {
                            Log.e(TAG, "Lỗi khi lấy thông tin người dùng cho userId: " + userId + ", lỗi: " + e.getMessage());
                            reviewWithUser.setFullName("Ẩn danh");
                            reviewList.add(reviewWithUser);
                            if (reviewList.size() == totalReviews) {
                                filteredReviewsLiveData.setValue(reviewList);
                            }
                        });
                    } catch (Exception e) {
                        Log.e(TAG, "Lỗi khi ánh xạ dữ liệu tại: " + reviewSnapshot.getKey() + ", lỗi: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Lỗi khi lấy đánh giá: " + error.getMessage());
                filteredReviewsLiveData.setValue(new ArrayList<>());
            }
        });
        return filteredReviewsLiveData;
    }

    public LiveData<List<ReviewWithUser>> getReviews() {
        return reviewsLiveData;
    }

    public void addReview(Review review, String userId) {
        if (userId == null || userId.isEmpty()) {
            Log.e(TAG, "userId không hợp lệ (null hoặc rỗng), không thể thêm đánh giá");
            return;
        }

        try {
            // Kiểm tra xem userId có phải là chuỗi số hợp lệ không
            if (!userId.matches("\\d+")) {
                Log.e(TAG, "userId không phải là chuỗi số hợp lệ: " + userId);
                return;
            }

            int userIdInt = Integer.parseInt(userId); // Chuyển userId từ String thành int
            review.setUserId(userIdInt);
            String key = mReviewsRef.push().getKey();
            if (key != null) {
                mReviewsRef.child(key).setValue(review)
                        .addOnSuccessListener(aVoid -> Log.d(TAG, "Thêm đánh giá thành công"))
                        .addOnFailureListener(e -> Log.e(TAG, "Không thể thêm đánh giá: " + e.getMessage()));
            } else {
                Log.e(TAG, "Không thể tạo key cho đánh giá");
            }
        } catch (NumberFormatException e) {
            Log.e(TAG, "Lỗi khi chuyển userId thành int: " + userId + ", lỗi: " + e.getMessage());
        }
    }
}