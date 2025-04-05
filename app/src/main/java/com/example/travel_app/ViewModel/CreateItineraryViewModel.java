package com.example.travel_app.ViewModel;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travel_app.Data.Model.Itinerary;
import com.example.travel_app.Data.Repository.ItineraryRepository;

import java.util.List;

public class CreateItineraryViewModel extends AndroidViewModel {
    private ItineraryRepository repository;
    private MutableLiveData<List<Itinerary>> itineraryListLiveData = new MutableLiveData<>();
    private String currentUserId; // Giả sử bạn có cách lấy userId (ví dụ: từ Firebase Auth)

    public CreateItineraryViewModel(Application application) {
        super(application);
        repository = new ItineraryRepository();
        currentUserId = "user123"; // Thay bằng cách lấy userId thực tế, ví dụ: FirebaseAuth.getInstance().getCurrentUser().getUid()
    }

    public LiveData<List<Itinerary>> getItineraryListLiveData() {
        return itineraryListLiveData;
    }

    public void loadMyItineraries() {
        repository.getItinerariesByUser(currentUserId).observeForever(itineraries -> {
            itineraryListLiveData.setValue(itineraries);
        });
    }

    public void loadSharedItineraries() {
        repository.getSharedItineraries().observeForever(itineraries -> {
            itineraryListLiveData.setValue(itineraries);
        });
    }

    public void shareItinerary(Itinerary itinerary) {
        repository.updateIsShare(itinerary.getId(), true, new ItineraryRepository.Callback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplication(), "Chia sẻ lộ trình thành công", Toast.LENGTH_SHORT).show();
                itinerary.setIsShare(true);
                loadMyItineraries(); // Cập nhật danh sách của người dùng
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getApplication(), "Lỗi khi chia sẻ: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Xóa các observer nếu cần khi ViewModel bị hủy
    }
}