package com.example.travel_app.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app.Data.Model.Image;
import com.example.travel_app.Data.Model.Media;
import com.example.travel_app.Data.Repository.MediaRepository;

import java.util.List;

public class MediaViewModel extends ViewModel {
    private final MediaRepository mediaRepository;

    public MediaViewModel() {
        mediaRepository = new MediaRepository();
    }

    public LiveData<List<Image>> getImages(String locationId) {
        return mediaRepository.getImages(locationId);
    }

    public LiveData<List<Media>> getVideos(String locationId) {
        return mediaRepository.getVideos(locationId);
    }
}