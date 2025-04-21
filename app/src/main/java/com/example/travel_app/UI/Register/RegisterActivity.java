package com.example.travel_app.UI.Register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travel_app.Data.Model.User;
import com.example.travel_app.R;
import com.example.travel_app.UI.Login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtFullName, edtEmail, edtPhone, edtPassword;
    private LinearLayout layoutLoginPrompt;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference lastUserIdRef;

    public static final String EXTRA_EMAIL = "EXTRA_EMAIL";
    public static final String EXTRA_PASSWORD = "EXTRA_PASSWORD";
    public static int isClickRegister = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        lastUserIdRef = FirebaseDatabase.getInstance().getReference("lastUserId");

        initViews();
    }

    private void initViews() {
        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        layoutLoginPrompt = findViewById(R.id.layoutLoginPrompt);

        TextView tvLoginNow = findViewById(R.id.tvLoginNow);
        tvLoginNow.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
        layoutLoginPrompt.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });

        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> ActionRegister());
    }

    private void ActionRegister() {
        if (edtFullName.getText().toString().isEmpty()
                || edtEmail.getText().toString().isEmpty()
                || edtPhone.getText().toString().isEmpty()
                || edtPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        String fullName = edtFullName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            // Lấy userId cuối cùng từ Firebase và tăng lên 1
                            lastUserIdRef.get().addOnCompleteListener(lastUserTask -> {
                                if (lastUserTask.isSuccessful()) {
                                    String lastUserId = lastUserTask.getResult().getValue(String.class);
                                    int newUserId = (lastUserId == null) ? 1 : Integer.parseInt(lastUserId) + 1;

                                    // Lưu lại ID người dùng mới vào Firebase
                                    String newUserIdStr = String.valueOf(newUserId);
                                    lastUserIdRef.setValue(newUserIdStr);  // Cập nhật ID cuối cùng

                                    // Tạo người dùng với ID mới
                                    User user = new User(newUserIdStr, fullName, email, phone, "", (Date) null);
                                    databaseReference.child(newUserIdStr).setValue(user)
                                            .addOnSuccessListener(unused -> {
                                                Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                                isClickRegister = 1;
                                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                intent.putExtra(EXTRA_EMAIL, email);
                                                intent.putExtra(EXTRA_PASSWORD, password);
                                                startActivity(intent);
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(RegisterActivity.this, "Lỗi lưu thông tin người dùng", Toast.LENGTH_SHORT).show();
                                            });
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Lỗi khi lấy ID người dùng cuối", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        String errorMsg = task.getException() != null ? task.getException().getMessage() : "Không rõ lỗi";
                        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại: " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
