package com.example.travel_app.ViewModel.Itinerary;


import android.app.Application;

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
        imageRepository.getImageUrlForLocation(locationId, new ImageRepository.ImageCallback() {
            @Override
            public void onImageLoaded(String imageUrl) {
                Map<Integer, String> currentMap = imageUrlMapLiveData.getValue();
                if (currentMap == null) {
                    currentMap = new HashMap<>();
                }
                currentMap.put(locationId, imageUrl != null ? imageUrl : "");
                imageUrlMapLiveData.setValue(currentMap);
            }

            @Override
            public void onError(String error) {
                Map<Integer, String> currentMap = imageUrlMapLiveData.getValue();
                if (currentMap == null) {
                    currentMap = new HashMap<>();
                }
                currentMap.put(locationId, "");
                imageUrlMapLiveData.setValue(currentMap);
            }
        });
    }
}