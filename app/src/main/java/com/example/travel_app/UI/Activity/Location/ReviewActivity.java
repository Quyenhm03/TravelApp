package com.example.travel_app.UI.Activity.Location;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travel_app.R;

public class ReviewActivity extends AppCompatActivity {

    private RatingBar rbUserRating;
    private EditText etUserComment;
    private Button btnSubmitReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        rbUserRating = findViewById(R.id.rbUserRating);
        LayerDrawable stars = (LayerDrawable) rbUserRating.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#FFD700"), PorterDuff.Mode.SRC_ATOP); // full star
        stars.getDrawable(1).setColorFilter(Color.parseColor("#FFD700"), PorterDuff.Mode.SRC_ATOP); // half star
        stars.getDrawable(0).setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);
        etUserComment = findViewById(R.id.etUserComment);
        btnSubmitReview = findViewById(R.id.btnSubmitReview);

        btnSubmitReview.setOnClickListener(v -> {
            float rating = rbUserRating.getRating();
            String comment = etUserComment.getText().toString().trim();

            if (!comment.isEmpty() && rating > 0) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("rating", rating);
                resultIntent.putExtra("comment", comment);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(ReviewActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
