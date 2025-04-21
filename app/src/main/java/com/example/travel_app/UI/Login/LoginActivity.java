package com.example.travel_app.UI.Login;

import static com.example.travel_app.UI.Register.RegisterActivity.EXTRA_EMAIL;
import static com.example.travel_app.UI.Register.RegisterActivity.EXTRA_PASSWORD;
import static com.example.travel_app.UI.Register.RegisterActivity.isClickRegister;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travel_app.R;
import com.example.travel_app.UI.Activity.HomeActivity;
import com.example.travel_app.UI.Register.RegisterActivity;
import com.example.travel_app.ViewModel.UserCurrentViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText edtEmail, edtPassword;
    private TextView tvRegister;
    private Button btnLogin;
    private UserCurrentViewModel userCurrentViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        userCurrentViewModel = new UserCurrentViewModel();
        mAuth = FirebaseAuth.getInstance();
        initView();
    }

    private void initView() {
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
        btnLogin.setOnClickListener(v -> ActionLogin());
    }


    private void ActionLogin() {
        if (edtEmail.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        Intent intentGetUser = getIntent();

        if (isClickRegister == 0) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, HomeActivity.class);
                            userCurrentViewModel.refreshCurrentUser();
                            startActivity(intent);
                            finishAffinity();
                        } else {

                            Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
            return;
        }
        String emailGetUser = intentGetUser.getStringExtra(EXTRA_EMAIL);
        String passwordGetUser = intentGetUser.getStringExtra(EXTRA_PASSWORD);
        assert emailGetUser != null;
        assert passwordGetUser != null;
        mAuth.signInWithEmailAndPassword(emailGetUser, passwordGetUser)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, HomeActivity.class);
                        startActivity(intent);
                    } else {

                        Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    }
                });


    }
}