package com.example.shopapp.UserActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.shopapp.Models.GioHangItem;
import com.example.shopapp.Models.SanPham;
import com.example.shopapp.R;
import com.example.shopapp.Utils.FirebaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class XemSanPhamActivity extends AppCompatActivity {
    TextView tvTenSP, tvGiaSP, tvMoTaSP, tvNhaSX;
    Button btnThemGioHang;
    Spinner spinnerSanPham;
    ViewFlipper vfSanPham;
    Toolbar tbSanPham;
    ListView lvBinhLuan;
    Animation in, out;
    ImageButton btnTruoc, btnSau;
    SanPham sp;

    DatabaseReference gioHangRef = FirebaseUtils.getChildRef("GioHang");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_san_pham);
        initUi();
        initListener();
    }

    private void initUi(){
        tvTenSP = findViewById(R.id.tv_ten_sp);
        tvGiaSP = findViewById(R.id.tv_gia_sp);
        tvMoTaSP = findViewById(R.id.tv_mo_ta_sp);
        tvNhaSX = findViewById(R.id.tv_nha_san_xuat);
        lvBinhLuan = findViewById(R.id.lv_binh_luan);
        btnThemGioHang = findViewById(R.id.btn_them_gio_hang);
        spinnerSanPham = findViewById(R.id.spinner_so_luong_mua);
        tbSanPham = findViewById(R.id.tb_san_pham);
        vfSanPham = findViewById(R.id.vf_san_pham);
        btnTruoc = findViewById(R.id.btn_truoc);
        btnSau = findViewById(R.id.btn_sau);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        SanPham sanPham = (SanPham) bundle.get("sp");
        sp = sanPham;

        tvTenSP.setText(sp.getTenSP());
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        tvGiaSP.setText("Giá: " + decimalFormat.format(sp.getGiaSP()) + " VNĐ");
        tvMoTaSP.setText(sp.getMoTaSP());
        tvNhaSX.setText("Nhà sản xuất: " + sp.getTenNhaSX());

        catchEventSpinner();
        actionToolbar();

        // Thuc hien viewflipper
        for (int i = 0; i < sp.getThumbnails().size(); i++){
            ImageView imageView = new ImageView(getBaseContext());
            Glide.with(getBaseContext()).load(sp.getThumbnails().get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            vfSanPham.addView(imageView);
        }

        in = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_in);
        out = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_out);
        vfSanPham.setInAnimation(in);
        vfSanPham.setOutAnimation(out);
        vfSanPham.setFlipInterval(3000);
        vfSanPham.setAutoStart(true);
        actionViewFlipper();
    }
    private void actionViewFlipper(){
        btnTruoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vfSanPham.isAutoStart()) {
                    vfSanPham.stopFlipping();
                    vfSanPham.showPrevious();
                    vfSanPham.startFlipping();
                    vfSanPham.setAutoStart(true);
                }
            }
        });
        btnSau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vfSanPham.isAutoStart()) {
                    vfSanPham.stopFlipping();
                    vfSanPham.showNext();
                    vfSanPham.startFlipping();
                    vfSanPham.setAutoStart(true);
                }
            }
        });
    }
    private void actionToolbar() {
        setSupportActionBar(tbSanPham);
        tbSanPham.setNavigationIcon(R.drawable.back);
        tbSanPham.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void catchEventSpinner(){
        Integer[] soLuong = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, soLuong);
        spinnerSanPham.setAdapter(arrayAdapter);
    }
    private void initListener(){
        themVaoGioHang();
    }
    private void themVaoGioHang(){
        int soLuong = Integer.parseInt(spinnerSanPham.getSelectedItem().toString());
        btnThemGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                GioHangItem item =
                        new GioHangItem(sp.getId(), sp.getTenSP(), sp.getGiaSP(), sp.getThumbnails().get(0), soLuong);
                gioHangRef.child(user.getUid()).child(sp.getId()).setValue(item);
                Intent intent = new Intent(XemSanPhamActivity.this, KhachHangActivity.class);
                intent.putExtra("fragmentToOpen", "GioHangFragment");
                startActivity(intent);
            }
        });
    }
}