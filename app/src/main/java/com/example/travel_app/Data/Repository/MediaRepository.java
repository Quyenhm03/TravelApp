package com.example.travel_app.Data.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travel_app.Data.Model.Image;
import com.example.travel_app.Data.Model.Media;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MediaRepository {
    private final DatabaseReference mediaRef;
    private final DatabaseReference imageRef;
    private final MutableLiveData<List<Media>> mediaLiveData;
    private final MutableLiveData<List<Image>> imagesLiveData;

    public MediaRepository() {
        mediaRef = FirebaseDatabase.getInstance().getReference("Media");
        imageRef = FirebaseDatabase.getInstance().getReference("Image");
        mediaLiveData = new MutableLiveData<>();
        imagesLiveData = new MutableLiveData<>();
    }

    private Integer tryParseLocationId(String locationId) {
        try {
            return Integer.parseInt(locationId);
        } catch (NumberFormatException e) {
            Log.e("MediaRepository", "Invalid locationId format: " + locationId);
            return null;
        }
    }

    public LiveData<List<Media>> getVideos(String locationId) {
        Integer parsedLocationId = tryParseLocationId(locationId);
        if (parsedLocationId == null) {
            mediaLiveData.setValue(new ArrayList<>());
            return mediaLiveData;
        }

        mediaRef.orderByChild("location_id").equalTo(parsedLocationId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Media> medias = new ArrayList<>();
                        for (DataSnapshot item : snapshot.getChildren()) {
                            Media media = item.getValue(Media.class);
                            if (media != null && "video".equals(media.getMediaType())) {
                                medias.add(media);
                            }
                        }

                        Log.d("MediaRepository", "Found videos: " + medias.size());
                        mediaLiveData.setValue(medias);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("MediaRepository", "Error fetching videos: " + error.getMessage());
                        mediaLiveData.setValue(new ArrayList<>());
                    }
                });

        return mediaLiveData;
    }

    public LiveData<List<Image>> getImages(String locationId) {
        Log.d("MediaRepository", "Starting to fetch images for locationId: " + locationId);
        Integer parsedLocationId = tryParseLocationId(locationId);
        if (parsedLocationId == null) {
            imagesLiveData.setValue(new ArrayList<>());
            return imagesLiveData;
        }

        imageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Image> images = new ArrayList<>();
                for (DataSnapshot item : snapshot.getChildren()) {
                    Image image = item.getValue(Image.class);
                    if (image != null && image.getLocationId() == parsedLocationId) {
                        Log.d("MediaRepository", "Found image: " + image.getUrl());
                        images.add(image);
                    }
                }

                Log.d("MediaRepository", "Found images: " + images.size());
                imagesLiveData.setValue(images);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MediaRepository", "Error fetching images: " + error.getMessage());
                imagesLiveData.setValue(new ArrayList<>());
            }
        });

        return imagesLiveData;
    }

}
