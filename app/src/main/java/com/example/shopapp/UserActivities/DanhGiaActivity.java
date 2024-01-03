package com.example.shopapp.UserActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shopapp.Models.GioHangItem;
import com.example.shopapp.Models.SanPham;
import com.example.shopapp.Models.User;
import com.example.shopapp.R;
import com.example.shopapp.Utils.FirebaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DanhGiaActivity extends AppCompatActivity {
    ImageView imgDaiDien;
    TextView tvTenSP, tvGiaSP, tvNhaSX;
    RatingBar rbDanhGia;
    EditText edtBinhLuan;
    Button btnGui;
    Toolbar tbDanhGia;
    GioHangItem item;
    float soSao;
    User userInfor;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference userRef = FirebaseUtils.getChildRef("User");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_gia);

        initUi();
        initListener();
    }

    private void initUi(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        item = (GioHangItem) bundle.get("data");

        tvTenSP = findViewById(R.id.tv_ten_sp);
        tvGiaSP = findViewById(R.id.tv_gia_sp);
        tvNhaSX = findViewById(R.id.tv_nha_sx);
        rbDanhGia = findViewById(R.id.rb_danh_gia);
        edtBinhLuan = findViewById(R.id.edt_binh_luan);
        btnGui = findViewById(R.id.btn_gui);
        tbDanhGia = findViewById(R.id.tb_danh_gia);
        imgDaiDien = findViewById(R.id.img_dai_dien);

        tvTenSP.setText(item.getTenSP());
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        tvGiaSP.setText("Giá: " + decimalFormat.format(item.getGiaSP()) + " VNĐ");
        tvNhaSX.setText("Nhà sản xuất: " + item.getTenNhaSX());
        Glide.with(getBaseContext()).load(item.getAnhDaiDien()).into(imgDaiDien);

        rbDanhGia.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                soSao = rating;
            }
        });
    };

    private void initListener(){
        actionToolBar();
        btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                luuDanhGia();
            }
        });
    };
    private void actionToolBar(){
        setSupportActionBar(tbDanhGia);
        tbDanhGia.setNavigationIcon(R.drawable.back);
        tbDanhGia.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void luuDanhGia(){
        String binhLuan = edtBinhLuan.getText().toString().trim();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String thoiGian = sdf.format(new Date());
        if (binhLuan.length() > 0 && soSao > 0) {
            DatabaseReference danhGiaRef = FirebaseUtils.getChildRef("DanhGia").child(item.getId())
                    .child((user.getUid()));
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot data : snapshot.getChildren()){
                        if (data.getKey().equals(user.getUid())){
                            userInfor = data.getValue(User.class);
                            danhGiaRef.child("User").setValue(userInfor);
                        }
                    }
                    danhGiaRef.child("BinhLuan").setValue(binhLuan);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            danhGiaRef.child("DanhGia").setValue(soSao);
            danhGiaRef.child("ThoiGian").setValue(thoiGian);
            Toast.makeText(this, "Gửi bình luận thành công !", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DanhGiaActivity.this, KhachHangActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Vui lòng điền đầy đủ !", Toast.LENGTH_SHORT).show();
        }

    }
}