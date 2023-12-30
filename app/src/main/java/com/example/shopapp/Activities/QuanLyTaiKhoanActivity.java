package com.example.shopapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.shopapp.Adapters.TaiKhoanAdapter;
import com.example.shopapp.Models.User;
import com.example.shopapp.R;
import com.example.shopapp.Utils.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuanLyTaiKhoanActivity extends AppCompatActivity {
    Toolbar tbQuanLyTK;
    ListView lvTaiKhoan;
    ArrayList<User> listUser = new ArrayList<>();
    TaiKhoanAdapter taiKhoanAdapter;
    DatabaseReference userRef = FirebaseUtils.getChildRef("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_tai_khoan);

        tbQuanLyTK = findViewById(R.id.tb_quan_ly_tai_khoan);
        lvTaiKhoan = findViewById(R.id.lv_tai_khoan);

        taiKhoanAdapter = new TaiKhoanAdapter(QuanLyTaiKhoanActivity.this, R.layout.lv_tai_khoan, listUser);
        lvTaiKhoan.setAdapter(taiKhoanAdapter);

        lvTaiKhoan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                User userSelected = listUser.get(position);
                Intent intent = new Intent(QuanLyTaiKhoanActivity.this, ChiTietTaiKhoanActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", userSelected);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        docDuLieu();
        actionBar();
    }

    private void actionBar() {
        setSupportActionBar(tbQuanLyTK);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tbQuanLyTK.setNavigationIcon(R.drawable.back);
        tbQuanLyTK.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void docDuLieu() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUser.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    if (user != null && user.getRole().equals("customer")) {
                        listUser.add(user);
                    }
                }
                taiKhoanAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
