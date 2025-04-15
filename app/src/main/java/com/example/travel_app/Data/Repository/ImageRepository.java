package com.example.travel_app.Data.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travel_app.Data.Model.Image;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ImageRepository {
    private final DatabaseReference imageRef;
    private MutableLiveData<List<Image>> _mImageLiveData;
    private LiveData<List<Image>> imageLiveData = _mImageLiveData;

    public ImageRepository() {
        imageRef = FirebaseDatabase.getInstance().getReference("Media");
        _mImageLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Image>> getImages(String locationId) {
        Log.d("MediaRepository", "Starting to fetch images for locationId: " + locationId);

        imageRef.orderByChild("location_id").equalTo(locationId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Log.e("MediaRepository", "No images found for locationId: " + locationId);
                    _mImageLiveData.setValue(new ArrayList<>());
                    return;
                }

                List<Image> images = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Image image = snapshot.getValue(Image.class);
                    if (image != null) {
                        Log.d("MediaRepository", "Found image: " + image.getUrl());
                        images.add(image);
                    } else {
                        Log.w("MediaRepository", "Found an invalid image record: " + snapshot.getKey());
                    }
                }

                if (images.isEmpty()) {
                    Log.e("MediaRepository", "No valid images found for locationId: " + locationId);
                } else {
                    Log.d("MediaRepository", "Found images: " + images.size());
                }
                _mImageLiveData.setValue(images);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("MediaRepository", "Error fetching images: " + databaseError.getMessage());
                _mImageLiveData.setValue(new ArrayList<>());
            }
        });
        return _mImageLiveData;
    }
}
