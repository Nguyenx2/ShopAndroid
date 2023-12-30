package com.example.shopapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shopapp.Adapters.HoaDonAdapter;
import com.example.shopapp.Models.HoaDon;
import com.example.shopapp.Models.User;
import com.example.shopapp.R;
import com.example.shopapp.Utils.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ChiTietTaiKhoanActivity extends AppCompatActivity {
    User user;
    ImageView imgAvt;
    TextView tvTenNguoiDung, tvEmail, tvSDT, tvDiaChi, tvTongSoTien, tvSoHD;
    Button btnLuuVaiTro;
    ListView lvHoaDon;
    ArrayList<HoaDon> listHoaDon = new ArrayList<>();
    AppCompatSpinner spinnerVaiTro;
    Toolbar tbChiTietTK;
    String role;
    float tongTien = 0;
    HoaDonAdapter hoaDonAdapter;
    DatabaseReference hoaDonRef = FirebaseUtils.getChildRef("HoaDon");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_tai_khoan);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        user = (User) bundle.get("user");

        initUi();
        initListenner();
    }
    private void initUi(){
        imgAvt = findViewById(R.id.img_avt);
        tvTenNguoiDung = findViewById(R.id.tv_ten_nguoi_dung);
        tvEmail = findViewById(R.id.tv_email);
        tvSDT = findViewById(R.id.tv_SDT);
        tvDiaChi = findViewById(R.id.tv_dia_chi);
        tvTongSoTien = findViewById(R.id.tv_tong_so_tien);
        tvSoHD = findViewById(R.id.tv_so_hoa_don);
        btnLuuVaiTro = findViewById(R.id.btn_luu_vai_tro);
        lvHoaDon = findViewById(R.id.lv_hoa_don);
        spinnerVaiTro = findViewById(R.id.spinner_vai_tro);
        tbChiTietTK = findViewById(R.id.tb_chi_tiet_tk);

        Glide.with(getBaseContext()).load(user.getAnhDaiDien()).into(imgAvt);
        tvTenNguoiDung.setText("Tên: " + user.getTen());
        tvEmail.setText("Email: " + user.getEmail());
        tvSDT.setText("SĐT: " + user.getSoDienThoai());
        tvDiaChi.setText("Địa chỉ: " + user.getDiaChi());

        // Xu ly spinner
        String[] roles = {"customer", "admin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, roles);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinnerVaiTro.setAdapter(adapter);
        int positon = adapter.getPosition(user.getRole());
        spinnerVaiTro.setSelection(positon);

        spinnerVaiTro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                role = adapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // Du lieu hoa don
        hoaDonAdapter = new HoaDonAdapter(ChiTietTaiKhoanActivity.this, R.layout.lv_hoa_don, listHoaDon);
        lvHoaDon.setAdapter(hoaDonAdapter);
    }
    private void initListenner(){
        actionBar();
        luuVaiTro();
        docDuLieuHD();
    }
    private void actionBar(){
        setSupportActionBar(tbChiTietTK);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tbChiTietTK.setNavigationIcon(R.drawable.back);
        tbChiTietTK.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void luuVaiTro(){
        btnLuuVaiTro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference userRef = FirebaseUtils.getChildRef("User");
                userRef.child(user.getId()).child("role").setValue(role);
                Toast.makeText(ChiTietTaiKhoanActivity.this,
                        "Thay đổi vai trò tài khoản thành công !", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void docDuLieuHD(){
        hoaDonRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listHoaDon.clear();
                for (DataSnapshot data : snapshot.getChildren()){
                    String userId = data.child("userId").getValue(String.class);
                    if (userId != null && userId.equals(user.getId())){
                        HoaDon hoaDon = data.getValue(HoaDon.class);
                        listHoaDon.add(hoaDon);
                    }
                }
                tvSoHD.setText("Số hóa đơn: (" + listHoaDon.size() + ")");
                hoaDonAdapter.notifyDataSetChanged();

                for (HoaDon hd : listHoaDon) {
                    tongTien += hd.getTongTien();
                }
                DecimalFormat decimalFormat = new DecimalFormat("#,###");
                tvTongSoTien.setText("Tổng số tiền đã chi tiêu: " + decimalFormat.format(tongTien) + " VNĐ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}