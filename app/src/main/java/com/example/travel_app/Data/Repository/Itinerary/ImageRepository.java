package com.example.travel_app.Data.Repository.Itinerary;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.travel_app.Data.Model.Itinerary.Image;
import com.example.travel_app.Data.Model.Itinerary.Media;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ImageRepository {
    private final Map<Integer, String> imageCache = new HashMap<>();
    private final Context context;

    public ImageRepository(Context context) {
        this.context = context;
    }

    public interface ImageCallback {
        void onImageLoaded(String imageUrl);
        void onError(String error);
    }

    public void getImageUrlForLocation(int locationId, ImageCallback callback) {
        // Kiểm tra cache
        String cachedUrl = imageCache.get(locationId);
        if (cachedUrl != null) {
            Log.d("ImageRepository", "Using cached image for location ID: " + locationId + ", URL: " + cachedUrl);
            callback.onImageLoaded(cachedUrl);
            return;
        }

        // Kiểm tra mạng
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null || !activeNetwork.isConnectedOrConnecting()) {
            Log.e("ImageRepository", "No internet connection for location ID: " + locationId);
            imageCache.put(locationId, "");
            callback.onImageLoaded("");
            return;
        }

        DatabaseReference mediaRef = FirebaseDatabase.getInstance().getReference("Media");
        mediaRef.orderByChild("location_id").equalTo(locationId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot mediaSnapshot : snapshot.getChildren()) {
                    Media media = mediaSnapshot.getValue(Media.class);
                    if (media != null) {
                        Log.d("ImageRepository", "Media found: location_id=" + media.getLocation_id() + ", media_id=" + media.getMedia_id());
                        DatabaseReference imageRef = FirebaseDatabase.getInstance().getReference("Image");
                        imageRef.orderByChild("media_id").equalTo(media.getMedia_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot imageSnapshot) {
                                for (DataSnapshot imgSnapshot : imageSnapshot.getChildren()) {
                                    Image image = imgSnapshot.getValue(Image.class);
                                    if (image != null && image.getUrl() != null && !image.getUrl().isEmpty()) {
                                        String url = image.getUrl();
                                        Log.d("ImageRepository", "Image found for location ID: " + locationId + ", URL: " + url);
                                        imageCache.put(locationId, url);
                                        callback.onImageLoaded(url);
                                        return;
                                    }
                                }
                                Log.w("ImageRepository", "No image found for location ID: " + locationId);
                                imageCache.put(locationId, "");
                                callback.onImageLoaded("");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("ImageRepository", "Failed to fetch image for location ID: " + locationId + ": " + error.getMessage());
                                imageCache.put(locationId, "");
                                callback.onImageLoaded("");
                            }
                        });
                        return;
                    }
                }
                Log.w("ImageRepository", "No media found for location ID: " + locationId);
                imageCache.put(locationId, "");
                callback.onImageLoaded("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ImageRepository", "Failed to fetch media for location ID: " + locationId + ": " + error.getMessage());
                imageCache.put(locationId, "");
                callback.onImageLoaded("");
            }
        });
    }
}