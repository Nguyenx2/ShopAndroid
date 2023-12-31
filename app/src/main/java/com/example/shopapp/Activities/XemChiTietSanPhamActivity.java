package com.example.shopapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shopapp.Models.SanPham;
import com.example.shopapp.R;

import java.text.DecimalFormat;

public class XemChiTietSanPhamActivity extends AppCompatActivity {
    ImageView img1, img2, img3;
    TextView tvTenSP, tvGiaSP, tvMoTaSP, tvNhaSanXuat;
    Button btnQuayLai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_chi_tiet_san_pham);

        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        tvTenSP = findViewById(R.id.tvTenSP);
        tvGiaSP = findViewById(R.id.tvGiaSP);
        tvMoTaSP = findViewById(R.id.tvMoTaSP);
        tvNhaSanXuat = findViewById(R.id.tvNhaSanXuat);
        btnQuayLai = findViewById(R.id.btnQuayLai);

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        SanPham sp = (SanPham) data.get("dataXemChiTiet");

        tvTenSP.setText(sp.getTenSP());
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        tvGiaSP.setText("Giá: " + decimalFormat.format(sp.getGiaSP()) + " VNĐ");
        tvMoTaSP.setText(sp.getMoTaSP());
        tvNhaSanXuat.setText("Nhà sản xuất: " + sp.getTenNhaSX());

        if (sp.getThumbnails().size() > 0) {
            Glide.with(getBaseContext()).load(sp.getThumbnails().get(0)).into(img1);
        }
        if (sp.getThumbnails().size() > 1) {
            Glide.with(getBaseContext()).load(sp.getThumbnails().get(1)).into(img2);
        }
        if (sp.getThumbnails().size() > 2) {
            Glide.with(getBaseContext()).load(sp.getThumbnails().get(2)).into(img3);
        }

        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}