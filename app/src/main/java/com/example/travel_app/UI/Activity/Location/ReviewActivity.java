package com.example.travel_app.UI.Activity.Location;

import android.content.Intent;
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
