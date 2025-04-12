package com.example.travel_app.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app.Data.Model.Review;
import com.example.travel_app.Data.Repository.ReviewRepository;

import java.util.List;

public class ReviewViewModel extends ViewModel{
    private MutableLiveData<List<Review>> _reviews = new MutableLiveData<>();
    private ReviewRepository repository;
    private LiveData<List<Review>> reviews = _reviews;

    public ReviewViewModel() {
        repository = new ReviewRepository();
    }

//    public LiveData<List<Review>> getReviews() {
//        _reviews.setValue(repository.getReviews());
//        return reviews;
//    }

    public void addReview(Review review) {
        repository.addReview(review);
    }
}
