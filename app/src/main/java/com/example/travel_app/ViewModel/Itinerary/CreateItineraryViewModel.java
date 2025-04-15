package com.example.travel_app.ViewModel.Itinerary;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.travel_app.Data.Model.Itinerary.Itinerary;
import com.example.travel_app.Data.Repository.Itinerary.ItineraryRepository;
import com.example.travel_app.UI.Login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class CreateItineraryViewModel extends AndroidViewModel {
    private final ItineraryRepository repository;
    private final MutableLiveData<Boolean> isSearchingLiveData = new MutableLiveData<>(false);
    private final MutableLiveData<List<Itinerary>> currentItineraries = new MutableLiveData<>();
    private final String currentUserId;
    private boolean isMyItineraryMode = true;

    // Lưu trữ reference đến các Observer để có thể remove khi cần
    private Observer<List<Itinerary>> myItinerariesObserver;
    private Observer<List<Itinerary>> sharedItinerariesObserver;
    private Observer<List<Itinerary>> searchItinerariesObserver;
    private LiveData<List<Itinerary>> currentObservableData;

    public CreateItineraryViewModel(@NonNull Application application) {
        super(application);
        repository = new ItineraryRepository();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
    }

    public LiveData<Boolean> getIsSearchingLiveData() {
        return isSearchingLiveData;
    }

    public LiveData<List<Itinerary>> getCurrentItineraries() {
        return currentItineraries;
    }

    // Chuyển sang chế độ xem itinerary của tôi
    public void switchToMyItineraries() {
        isMyItineraryMode = true;
        isSearchingLiveData.setValue(false);

        // Xóa observer cũ nếu có
        removeCurrentObserver();

        // Tạo và thiết lập observer mới
        if (currentUserId == null) {
            currentItineraries.setValue(new ArrayList<>());
        } else {
            currentObservableData = repository.getItinerariesByUser(currentUserId);
            myItinerariesObserver = itineraries -> currentItineraries.setValue(itineraries);
            currentObservableData.observeForever(myItinerariesObserver);
        }
    }

    // Chuyển sang chế độ xem itinerary được chia sẻ
    public void switchToSharedItineraries() {
        isMyItineraryMode = false;
        isSearchingLiveData.setValue(false);

        // Xóa observer cũ nếu có
        removeCurrentObserver();

        // Tạo và thiết lập observer mới
        currentObservableData = repository.getSharedItineraries();
        sharedItinerariesObserver = itineraries -> currentItineraries.setValue(itineraries);
        currentObservableData.observeForever(sharedItinerariesObserver);
    }

    // Tìm kiếm itineraries
    public void searchItineraries(String query) {
        if (query == null || query.trim().isEmpty()) {
            // Nếu query trống, quay lại chế độ xem ban đầu
            if (isMyItineraryMode) {
                switchToMyItineraries();
            } else {
                switchToSharedItineraries();
            }
            return;
        }

        // Thiết lập trạng thái tìm kiếm
        isSearchingLiveData.setValue(true);

        // Xóa observer cũ nếu có
        removeCurrentObserver();

        // Tạo và thiết lập observer mới cho tìm kiếm
        currentObservableData = repository.searchItinerariesByTitle(query);
        searchItinerariesObserver = itineraries -> currentItineraries.setValue(itineraries);
        currentObservableData.observeForever(searchItinerariesObserver);
    }

    // Xóa observer hiện tại để tránh memory leak
    private void removeCurrentObserver() {
        if (currentObservableData != null) {
            if (myItinerariesObserver != null && isMyItineraryMode) {
                currentObservableData.removeObserver(myItinerariesObserver);
                myItinerariesObserver = null;
            }
            if (sharedItinerariesObserver != null && !isMyItineraryMode) {
                currentObservableData.removeObserver(sharedItinerariesObserver);
                sharedItinerariesObserver = null;
            }
            if (searchItinerariesObserver != null) {
                currentObservableData.removeObserver(searchItinerariesObserver);
                searchItinerariesObserver = null;
            }
        }
    }

    public void shareItinerary(Itinerary itinerary) {
        repository.updateIsShare(itinerary.getId(), true, new ItineraryRepository.Callback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplication(), "Chia sẻ lộ trình thành công", Toast.LENGTH_SHORT).show();
                itinerary.setIsShare(true);

                // Cập nhật lại danh sách nếu cần
                if (isMyItineraryMode) {
                    switchToMyItineraries();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getApplication(), "Lỗi khi chia sẻ: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCleared() {
        // Đảm bảo tất cả observer được xóa khi ViewModel bị destroy
        removeCurrentObserver();
        super.onCleared();
    }
}