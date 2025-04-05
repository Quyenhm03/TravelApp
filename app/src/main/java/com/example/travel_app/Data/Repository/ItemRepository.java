package com.example.travel_app.Data.Repository;

import com.example.travel_app.Data.Model.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ItemRepository {
    private DatabaseReference itemsRef;

    public ItemRepository() {
        itemsRef = FirebaseDatabase.getInstance().getReference("Item_tmp");
    }

    public void searchItems(String query, SearchCallback callback) {
        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Item> result = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    if (item != null && (item.getTitle().toLowerCase().contains(query.toLowerCase())
                    || item.getAddress().toLowerCase().contains(query.toLowerCase()))) {
                        result.add(item);
                    }
                }
                callback.onSuccess(result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure(databaseError.toException());
            }
        });
    }

    public interface SearchCallback {
        void onSuccess(List<Item> items);
        void onFailure(Exception e);
    }
}