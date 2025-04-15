package com.example.travel_app.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app.Data.Model.Review;
import com.example.travel_app.Data.Model.ReviewWithUser;
import com.example.travel_app.Data.Repository.ReviewRepository;

import java.util.List;

public class ReviewViewModel extends ViewModel {
    private ReviewRepository reviewRepository;

    public ReviewViewModel() {
        reviewRepository = new ReviewRepository();
    }

    public LiveData<List<ReviewWithUser>> getReviews(int locationId) {
        return reviewRepository.getReviewsForLocation(locationId);
    }

    public void addReview(Review review) {
        reviewRepository.addReview(review);
    }
}