package com.example.shopapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.shopapp.Activities.QuanLyNSXActivity;
import com.example.shopapp.Activities.QuanLySanPhamActivity;
import com.example.shopapp.DangNhapActivity;
import com.example.shopapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    Button btnQuanLyNSX, btnQuanLySP, btnDangXuat, btnQuanLyTaiKhoan, btnThongKe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addViews();
        addEvents();
    }

    private void addEvents() {
        btnQuanLySP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent quanLySanPhamIntent = new Intent(getBaseContext(), QuanLySanPhamActivity.class);
                startActivity(quanLySanPhamIntent);
            }
        });

        btnQuanLyNSX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), QuanLyNSXActivity.class);
                startActivity(intent);
            }
        });

        btnQuanLyTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), QuanLyTaiKhoanActivity.class);
                startActivity(intent);
            }
        });
        btnThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ThongKeActivity.class);
                startActivity(intent);
            }
        });
        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getBaseContext(), DangNhapActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }

    private void addViews() {
        btnQuanLyNSX = findViewById(R.id.btnQuanLyNSX);
        btnQuanLySP = findViewById(R.id.btnQuanLySP);
        btnDangXuat = findViewById(R.id.btn_dang_xuat);
        btnQuanLyTaiKhoan = findViewById(R.id.btnQuanLyTaiKhoan);
        btnThongKe = findViewById(R.id.btnThongKe);
    }

}