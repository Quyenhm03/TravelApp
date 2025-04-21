package com.example.travel_app.ViewModel;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.travel_app.Data.Model.User;
import com.example.travel_app.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;

public class UserCurrentViewModel extends ViewModel {
    private static final String TAG = "UserCurrentViewModel";
    private final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User");
    private final MutableLiveData<User> mUser = new MutableLiveData<>();
    public final LiveData<User> user = mUser;

    public UserCurrentViewModel() {
        fetchCurrentUser();
    }

    public LiveData<User> getCurrentUser() {
        return user;
    }

    private void fetchCurrentUser() {
        FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();
        if (authUser == null || authUser.getEmail() == null) {
            Log.w(TAG, "Không có người dùng đăng nhập hoặc email null");
            mUser.setValue(null);
            return;
        }

        String email = authUser.getEmail();
        Log.d(TAG, "Truy vấn người dùng với email: " + email);
        userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "Snapshot cho email " + email + ": " + snapshot.toString());
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        if (user != null) {
                            Log.d(TAG, "Tìm thấy người dùng: userId=" + user.getUserId() + ", fullName=" + user.getFullName());
                            mUser.setValue(user);
                            return;
                        }
                    }
                } else {
                    Log.w(TAG, "Không tìm thấy người dùng với email: " + email);
                    createNewUser(authUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Lỗi khi truy vấn người dùng: " + error.getMessage());
                mUser.setValue(null);
            }
        });
    }

    private void createNewUser(FirebaseUser authUser) {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long maxId = 0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    String userId = child.child("userId").getValue(String.class);
                    if (userId != null && userId.matches("\\d+")) {
                        long id = Long.parseLong(userId);
                        if (id > maxId) maxId = id;
                    }
                }
                String newUserId = String.valueOf(maxId + 1);
                User newUser = new User(
                        newUserId,
                        authUser.getDisplayName() != null ? authUser.getDisplayName() : "Unknown",
                        authUser.getEmail() != null ? authUser.getEmail() : "",
                        "", // phone mặc định
                        "", // address mặc định
                        (Date) null // dateOfBirth mặc định
                );

                userRef.child(newUserId).setValue(newUser).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Tạo bản ghi người dùng mới thành công: userId=" + newUserId);
                        mUser.setValue(newUser);
                    } else {
                        Log.e(TAG, "Lỗi khi tạo bản ghi người dùng: " + task.getException());
                        mUser.setValue(null);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Lỗi khi lấy danh sách User: " + error.getMessage());
                mUser.setValue(null);
            }
        });
    }

    public void updateInfoUser(String fullName, String birthday, String email, String address, String phone) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null || firebaseUser.getEmail() == null) {
            Log.w(TAG, "Không có người dùng đăng nhập để cập nhật");
            return;
        }

        String emailKey = firebaseUser.getEmail();
        userRef.orderByChild("email").equalTo(emailKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String userId = userSnapshot.child("userId").getValue(String.class);
                        if (userId != null) {
                            HashMap<String, Object> updates = new HashMap<>();
                            updates.put("fullName", fullName);
                            updates.put("email", email);
                            updates.put("address", address);
                            updates.put("phone", phone);

                            try {
                                Date dateOfBirth = Utils.stringToDate(birthday, "dd/MM/yyyy");
                                updates.put("dateOfBirth", dateOfBirth != null ? dateOfBirth.getTime() : null);
                            } catch (Exception e) {
                                Log.e(TAG, "Lỗi chuyển đổi ngày sinh", e);
                            }

                            userRef.child(userId).updateChildren(updates).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Cập nhật người dùng thành công");
                                    refreshCurrentUser(); // Tải lại dữ liệu
                                } else {
                                    Log.e(TAG, "Lỗi khi cập nhật người dùng", task.getException());
                                }
                            });
                            break;
                        }
                    }
                } else {
                    Log.w(TAG, "Không tìm thấy người dùng để cập nhật");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Lỗi Firebase khi cập nhật người dùng: " + error.getMessage());
            }
        });
    }


    public void refreshCurrentUser() {
        fetchCurrentUser();
    }
}