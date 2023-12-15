package com.example.shopapp.UserActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shopapp.Adapters.SanPhamDaMuaAdapter;
import com.example.shopapp.Models.GioHangItem;
import com.example.shopapp.Models.HoaDon;
import com.example.shopapp.R;
import com.example.shopapp.Utils.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class XemChiTietHoaDon extends AppCompatActivity {
    TextView tvTen, tvEmail, tvSDT, tvDiaChi, tvTongTien, tvThoiGian;
    ListView lvChiTietHD;
    ArrayList<GioHangItem> listSanPhamDaMua = new ArrayList<>();
    SanPhamDaMuaAdapter sanPhamDaMuaAdapter;
    HoaDon hd;
    Toolbar tbChiTietHD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_chi_tiet_hoa_don);
        initUi();
        initListerner();
    }
    private void initUi(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        hd = (HoaDon) bundle.get("hd");

        tvTen = findViewById(R.id.tv_ten);
        tvEmail = findViewById(R.id.tv_email);
        tvSDT = findViewById(R.id.tv_sdt);
        tvDiaChi = findViewById(R.id.tv_dia_chi);
        tvTongTien = findViewById(R.id.tv_tong_tien);
        tvThoiGian = findViewById(R.id.tv_thoi_gian);
        tbChiTietHD = findViewById(R.id.tb_chi_tiet_hd);

        tvTen.setText(hd.getHoTen());
        tvEmail.setText(hd.getEmail());
        tvSDT.setText(hd.getSdt());
        tvDiaChi.setText(hd.getDiaChi());
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        tvTongTien.setText(decimalFormat.format(hd.getTongTien()) + " VNĐ");
        tvThoiGian.setText(hd.getCreatedAt());

        lvChiTietHD = findViewById(R.id.lv_chi_tiet_hoa_don);
        sanPhamDaMuaAdapter = new SanPhamDaMuaAdapter(XemChiTietHoaDon.this, R.layout.lv_chi_tiet_hoa_don, listSanPhamDaMua);
        lvChiTietHD.setAdapter(sanPhamDaMuaAdapter);

        actionToolBar();
    }
    private void actionToolBar(){
        setSupportActionBar(tbChiTietHD);
        tbChiTietHD.setNavigationIcon(R.drawable.back);
        tbChiTietHD.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initListerner(){
        DatabaseReference hoaDonRef = FirebaseUtils.getChildRef("HoaDon");
        hoaDonRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listSanPhamDaMua.clear();
                for (DataSnapshot data : snapshot.getChildren()){
                    if (data.getKey().equals(hd.getId())){
                        DataSnapshot listItemDaMuaSnapshot = data.child("listGioHangItem");
                        for (DataSnapshot itemSnapshot : listItemDaMuaSnapshot.getChildren()){
                            GioHangItem item = itemSnapshot.getValue(GioHangItem.class);
                            listSanPhamDaMua.add(item);
                        }
                    }
                }
                sanPhamDaMuaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}