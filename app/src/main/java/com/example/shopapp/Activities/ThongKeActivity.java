package com.example.shopapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shopapp.Adapters.SanPhamAdapter;
import com.example.shopapp.Models.HoaDon;
import com.example.shopapp.Models.SanPham;
import com.example.shopapp.R;
import com.example.shopapp.Utils.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ThongKeActivity extends AppCompatActivity {
    Toolbar tbThongKe;
    ListView lvSanPham;
    TextView tvTongSoTien;
    SanPhamAdapter sanPhamAdapter;
    ArrayList<SanPham> listSanPham = new ArrayList<>();
    DatabaseReference sanPhamRef = FirebaseUtils.getChildRef("SanPham");
    DatabaseReference hoaDonRef = FirebaseUtils.getChildRef("HoaDon");
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
        actionBar();
    }
    private void actionBar() {
        setSupportActionBar(tbThongKe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tbThongKe.setNavigationIcon(R.drawable.back);
        tbThongKe.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
        hoaDonRef.addValueEventListener(new ValueEventListener() {
            float tongTien = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    HoaDon hd = data.getValue(HoaDon.class);
                    tongTien += hd.getTongTien();
                }
                DecimalFormat decimalFormat = new DecimalFormat("#,###");
                tvTongSoTien.setText(decimalFormat.format(tongTien) + " VNĐ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_thongke, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.soLuongBanChay){
            sanPhamTheoSLDaBan();
            return true;
        } else if (itemId == R.id.doanhThuCaoNhat) {
            sanPhamTheoDoanhThu();
            return true;
        }
        return true;
    }

    private void sanPhamTheoSLDaBan() {
        Collections.sort(listSanPham, new Comparator<SanPham>() {
            @Override
            public int compare(SanPham sp1, SanPham sp2) {
                return Double.compare(sp2.getSoLuongDaBan(), sp1.getSoLuongDaBan());
            }
        });
        sanPhamAdapter = new SanPhamAdapter(ThongKeActivity.this, R.layout.lv_thong_ke_san_pham, listSanPham);
        lvSanPham.setAdapter(sanPhamAdapter);
    }

    private void sanPhamTheoDoanhThu() {
        Collections.sort(listSanPham, new Comparator<SanPham>() {
            @Override
            public int compare(SanPham sp1, SanPham sp2) {
                return Double.compare(sp2.getSoLuongDaBan()*sp2.getGiaSP(), sp1.getSoLuongDaBan()*sp2.getGiaSP());
            }
        });
        sanPhamAdapter = new SanPhamAdapter(ThongKeActivity.this, R.layout.lv_thong_ke_san_pham, listSanPham);
        lvSanPham.setAdapter(sanPhamAdapter);
    }
}