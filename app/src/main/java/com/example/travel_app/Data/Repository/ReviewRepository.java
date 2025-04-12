package com.example.travel_app.Data.Repository;

import com.example.travel_app.Data.Model.Review;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ReviewRepository {
    private DatabaseReference mDatabase;

    public ReviewRepository() {
        mDatabase = FirebaseDatabase.getInstance().getReference("reviews");
    }

//    public List<Review> getReviews() {
//
//    }

    public void addReview(Review review) {
        String key = mDatabase.push().getKey();
        if (key != null) {
            mDatabase.child(key).setValue(review);
        }

    }
}
