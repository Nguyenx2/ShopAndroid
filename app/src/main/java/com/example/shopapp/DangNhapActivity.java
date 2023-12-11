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

import com.example.shopapp.Activities.AdminActivity;
import com.example.shopapp.UserActivities.KhachHangActivity;
import com.example.shopapp.Utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class DangNhapActivity extends AppCompatActivity {
    LinearLayout layoutDangKy;
    EditText edtEmail, edtPassword;
    Button btnDangNhap;
    DatabaseReference userRef = FirebaseUtils.getChildRef("User");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        initUi();
        initListener();
    }

    private void initUi() {
        layoutDangKy = findViewById(R.id.layout_dang_ky);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        btnDangNhap = findViewById(R.id.btn_dang_nhap);
    }
    private void initListener() {
        layoutDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DangNhapActivity.this, DangKyActivity.class);
                startActivity(intent);
            }
        });

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    kiemTraRole();
                                } else {
                                    Toast.makeText(DangNhapActivity.this,
                                            "Đăng nhập thất bại !", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void kiemTraRole() {
        String email = edtEmail.getText().toString().trim();
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isAdmin = false;
                for (DataSnapshot data : snapshot.getChildren()) {
                    String role = data.child("role").getValue(String.class);
                    String userEmail = data.child("email").getValue(String.class);
                    if (role != null && role.equals("admin") && userEmail.equals(email)) {
                        isAdmin = true;
                        break;
                    }
                }
                Intent intent;
                if (isAdmin) {
                    intent = new Intent(getBaseContext(), AdminActivity.class);
                } else {
                    intent = new Intent(getBaseContext(), KhachHangActivity.class);
                }
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DangNhapActivity.this,
                        "Có lỗi: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}