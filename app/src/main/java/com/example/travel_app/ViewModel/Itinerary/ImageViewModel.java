package com.example.travel_app.ViewModel.Itinerary;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travel_app.Data.Repository.Itinerary.ImageRepository;

import java.util.HashMap;
import java.util.Map;

public class ImageViewModel extends AndroidViewModel {
    private final ImageRepository imageRepository;
    private final MutableLiveData<Map<Integer, String>> imageUrlMapLiveData = new MutableLiveData<>(new HashMap<>());

    public ImageViewModel(@NonNull Application application) {
        super(application);
        imageRepository = new ImageRepository(application);
    }

    public LiveData<Map<Integer, String>> getImageUrlMapLiveData() {
        return imageUrlMapLiveData;
    }

    public void loadImageForLocation(int locationId) {
        Map<Integer, String> currentMap = imageUrlMapLiveData.getValue();
        if (currentMap != null && currentMap.containsKey(locationId)) {
            Log.d("ImageViewModel", "Image for locationId = " + locationId + " already loaded, skipping.");
            return; // Không load lại nếu đã có
        }

        Log.d("ImageViewModel", "Đang load ảnh cho locationId = " + locationId);

        imageRepository.getImageUrlForLocation(locationId, new ImageRepository.ImageCallback() {
            @Override
            public void onImageLoaded(String imageUrl) {
                Log.d("ImageViewModel", "Image loaded for locationId = " + locationId + ", URL = " + imageUrl);

                Map<Integer, String> updatedMap = new HashMap<>(imageUrlMapLiveData.getValue());
                updatedMap.put(locationId, imageUrl);
                imageUrlMapLiveData.setValue(updatedMap);
            }

            @Override
            public void onError(String error) {
                Log.w("ImageViewModel", "Error loading image for locationId = " + locationId + ", error: " + error);

                Map<Integer, String> updatedMap = new HashMap<>(imageUrlMapLiveData.getValue());
                updatedMap.put(locationId, null);
                imageUrlMapLiveData.setValue(updatedMap);
            }
        });
    }
}
