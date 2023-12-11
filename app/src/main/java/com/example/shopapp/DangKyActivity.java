package com.example.shopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.shopapp.Models.User;
import com.example.shopapp.UserActivities.KhachHangActivity;
import com.example.shopapp.Utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class DangKyActivity extends AppCompatActivity {
    EditText edtEmail, edtPassword;
    LinearLayout layoutDangNhap;
    Button btnDangKy;

    DatabaseReference userRef = FirebaseUtils.getChildRef("User");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        initUi();
        initListener();
    }

    private void initUi() {
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        btnDangKy = findViewById(R.id.btn_dang_ky);
        layoutDangNhap = findViewById(R.id.layout_dang_nhap);
    }

    private void initListener() {
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDangKy();
            }
        });

        layoutDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), DangNhapActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void onClickDangKy() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        if (email.length() > 0 && password.length() > 0) {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    User newUser = new User(
                                            user.getUid(),
                                            user.getDisplayName(),
                                            user.getEmail(),
                                            user.getPhoneNumber(),
                                            "",
                                            "",
                                            "customer"
                                    );
                                    userRef.child(user.getUid()).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(DangKyActivity.this, KhachHangActivity.class);
                                                startActivity(intent);
                                                finishAffinity();
                                            } else {
                                                Toast.makeText(DangKyActivity.this,
                                                        "Lỗi khi lưu thông tin người dùng !", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(DangKyActivity.this,
                                            "Đăng ký thất bại !", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(DangKyActivity.this, "Đăng ký thất bại !",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } else {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin !", Toast.LENGTH_SHORT).show();
        }

    }
}