package com.example.shopapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shopapp.Adapters.SanPhamAdapter;
import com.example.shopapp.Models.SanPham;
import com.example.shopapp.R;
import com.example.shopapp.Utils.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ThongKeActivity extends AppCompatActivity {
    Toolbar tbThongKe;
    ListView lvSanPham;
    TextView tvTongSoTien;
    SanPhamAdapter sanPhamAdapter;
    ArrayList<SanPham> listSanPham = new ArrayList<>();
    DatabaseReference sanPhamRef = FirebaseUtils.getChildRef("SanPham");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);
        tbThongKe = findViewById(R.id.tb_thong_ke);
        lvSanPham = findViewById(R.id.lv_san_pham);
        tvTongSoTien = findViewById(R.id.tvTongSoTien);

        sanPhamAdapter = new SanPhamAdapter(ThongKeActivity.this, R.layout.lv_thong_ke_san_pham, listSanPham);
        lvSanPham.setAdapter(sanPhamAdapter);
        docDuLieu();

    }
    private void docDuLieu(){
        sanPhamRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listSanPham.clear();
                for (DataSnapshot data :snapshot.getChildren()) {
                    SanPham sp = data.getValue(SanPham.class);
                    if (sp != null) {
                        listSanPham.add(sp);
                    }
                }
                sanPhamAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}