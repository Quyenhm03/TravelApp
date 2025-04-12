package com.example.travel_app.UI.Activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentManager;

import com.example.travel_app.Data.Model.SearchCoachInfo;
import com.example.travel_app.R;
import com.example.travel_app.UI.Fragment.SearchCoachResultFragment;

public class SearchCoachResultActivity extends AppCompatActivity {
    private TextView txtSearchCoachInfo1, txtSearchCoachInfo2;
    private SearchCoachInfo searchCoachInfo;
    public boolean isReturnCoach = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_coach_result);

        searchCoachInfo = (SearchCoachInfo) getIntent().getSerializableExtra("searchCoachInfo");
        isReturnCoach = getIntent().getBooleanExtra("isReturnCoach", false);

        txtSearchCoachInfo1 = findViewById(R.id.txt_search_coach_info1);
        txtSearchCoachInfo2 = findViewById(R.id.txt_search_coach_info2);

        // Hiển thị thông tin tìm kiếm
        if (!isReturnCoach) {
            String searchCoachInfo1 = searchCoachInfo.getDepartureCity() + " -> " + searchCoachInfo.getArrivalCity();
            txtSearchCoachInfo1.setText(searchCoachInfo1);

            String searchCoachInfo2 = searchCoachInfo.getDepartureDate() + " - " + searchCoachInfo.getSeatCount() + " ghế ngồi";
            txtSearchCoachInfo2.setText(searchCoachInfo2);
        } else {
            String searchCoachInfo1 = searchCoachInfo.getArrivalCity() + " -> " + searchCoachInfo.getDepartureCity();
            txtSearchCoachInfo1.setText(searchCoachInfo1);

            String searchCoachInfo2 = searchCoachInfo.getReturnDate() + " - " + searchCoachInfo.getSeatCount() + " ghế ngồi";
            txtSearchCoachInfo2.setText(searchCoachInfo2);
        }

        if (savedInstanceState == null) {
            SearchCoachResultFragment fragment = new SearchCoachResultFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("searchCoachInfo", searchCoachInfo);
            bundle.putBoolean("isReturnCoach", isReturnCoach);
            fragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container_coach_result, fragment)
                    .commit();
        }

        // Xử lý nút Back
        AppCompatButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
    }
}
