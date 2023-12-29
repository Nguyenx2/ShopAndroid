package com.example.shopapp.UserActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.shopapp.Adapters.BinhLuanAdapter;
import com.example.shopapp.Models.BinhLuan;
import com.example.shopapp.Models.GioHangItem;
import com.example.shopapp.Models.HoaDon;
import com.example.shopapp.Models.SanPham;
import com.example.shopapp.Models.User;
import com.example.shopapp.R;
import com.example.shopapp.Utils.FirebaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class XemSanPhamActivity extends AppCompatActivity {
    TextView tvTenSP, tvGiaSP, tvMoTaSP, tvNhaSX, tvDanhGiaTB, tvSoBinhLuan, tvThongBao;
    Button btnThemGioHang;
    Spinner spinnerSanPham;
    ViewFlipper vfSanPham;
    Toolbar tbSanPham;
    ListView lvBinhLuan;
    Animation in, out;
    ImageButton btnTruoc, btnSau;
    SanPham sp;
    BinhLuan binhLuan;
    DatabaseReference gioHangRef = FirebaseUtils.getChildRef("GioHang");
    DatabaseReference danhGiaRef = FirebaseUtils.getChildRef("DanhGia");
    BinhLuanAdapter binhLuanAdapter;
    ArrayList<BinhLuan> listBinhLuan = new ArrayList<>();
    ArrayList<Long> listDiemDanhGia = new ArrayList<>();
    List<ImageView> imageViews = new ArrayList<>();
    List<String> imageUrls = new ArrayList<>();
    ImageView dialogImageView;

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
        tvDanhGiaTB = findViewById(R.id.tv_danh_gia_tb);
        tvSoBinhLuan = findViewById(R.id.tv_so_binh_luan);
        tvThongBao = findViewById(R.id.tv_thong_bao);

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
            imageViews.add(imageView);
            // Lưu địa chỉ URL vào danh sách
            imageUrls.add(sp.getThumbnails().get(i));
        }

        for (ImageView imageView : imageViews){
            vfSanPham.addView(imageView);
        }

        in = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_in);
        out = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_out);
        vfSanPham.setInAnimation(in);
        vfSanPham.setOutAnimation(out);
        vfSanPham.setFlipInterval(3000);
        vfSanPham.setAutoStart(true);

        // Gan adapter
        binhLuanAdapter = new BinhLuanAdapter(XemSanPhamActivity.this, R.layout.lv_binh_luan, listBinhLuan);
        lvBinhLuan.setAdapter(binhLuanAdapter);
        actionViewFlipper();
        hienThiBinhLuan();
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
    private void hienThiBinhLuan(){
        danhGiaRef.child(sp.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listBinhLuan.clear();
                listDiemDanhGia.clear();
                for (DataSnapshot data : snapshot.getChildren()){
                    User user = data.child("User").getValue(User.class);
                    String thoiGian = data.child("ThoiGian").getValue(String.class);
                    String noiDung = data.child("BinhLuan").getValue(String.class);
                    Long danhGia = data.child("DanhGia").getValue(Long.class);
                    binhLuan = new BinhLuan(user, thoiGian, noiDung, danhGia);
                    listBinhLuan.add(binhLuan);
                    listDiemDanhGia.add(danhGia);
                }
                Collections.sort(listBinhLuan, comparator);
                tvSoBinhLuan.setText("(" + listBinhLuan.size() + ")");
                if (listBinhLuan.size() > 0){
                    tvThongBao.setVisibility(View.INVISIBLE);
                } else {
                    tvThongBao.setVisibility(View.VISIBLE);
                }

                double tongDiemDanhGia = 0;
                for (long diemDanhGia : listDiemDanhGia) {
                    tongDiemDanhGia += diemDanhGia;
                }

                if (listDiemDanhGia.size() > 0) {
                    double diemTB = (double)tongDiemDanhGia / listDiemDanhGia.size();
                    DecimalFormat decimalFormat = new DecimalFormat("#.#");
                    tvDanhGiaTB.setText("Đánh giá: " + decimalFormat.format(diemTB));
                } else {
                    tvDanhGiaTB.setText("Chưa có đánh giá nào !");
                }

                binhLuanAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    Comparator<BinhLuan> comparator = new Comparator<BinhLuan>() {
        @Override
        public int compare(BinhLuan bl1, BinhLuan bl2) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date1 = sdf.parse(bl1.getThoiGian());
                Date date2 = sdf.parse(bl2.getThoiGian());
                return  date2.compareTo(date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        }
    };

    private void catchEventSpinner(){
        Integer[] soLuong = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, soLuong);
        spinnerSanPham.setAdapter(arrayAdapter);
    }
    private void initListener(){
        themVaoGioHang();
        openGallery();
    }
    private void openGallery(){
        vfSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGallery();
            }
        });
    }
    private void showGallery(){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.gallery_layout);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        dialogImageView = dialog.findViewById(R.id.imageView);
        ImageButton btnPrev = dialog.findViewById(R.id.buttonPrevious);
        ImageButton btnNext = dialog.findViewById(R.id.buttonNext);
        ImageButton btnClose = dialog.findViewById(R.id.buttonClose);

        String imageUrl = getCurrentImageUrl();

        Glide.with(this).load(imageUrl).into(dialogImageView);

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPreviousImage();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNextImage();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private String getCurrentImageUrl(){
        int currentImageIndex = vfSanPham.getDisplayedChild();
        if (currentImageIndex >= 0 && currentImageIndex < imageUrls.size()) {
            return imageUrls.get(currentImageIndex);
        } else {
            return "";
        }
    }

    private void showPreviousImage() {
        vfSanPham.showPrevious();
        updateDialogImage();
    }

    private void showNextImage() {
        vfSanPham.showNext();
        updateDialogImage();
    }
    private void updateDialogImage(){
        String imageUrl = getCurrentImageUrl();
        Glide.with(this).load(imageUrl).into(dialogImageView);
    }

    private void themVaoGioHang(){
        btnThemGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int soLuong = Integer.parseInt(spinnerSanPham.getSelectedItem().toString());
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference gioHangUserRef = gioHangRef.child(user.getUid());

                gioHangUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            if (snapshot.hasChild(sp.getId())) {
                                GioHangItem tonTaiItem = snapshot.child(sp.getId()).getValue(GioHangItem.class);
                                int newSoLuong = tonTaiItem.getSoLuong() + soLuong;
                                tonTaiItem.setSoLuong(newSoLuong);
                                gioHangUserRef.child(sp.getId()).setValue(tonTaiItem);
                            } else {
                                GioHangItem newItem = new GioHangItem(sp.getId(), sp.getTenSP(), sp.getGiaSP(),
                                        sp.getThumbnails().get(0), soLuong, sp.getTenNhaSX());
                                gioHangUserRef.child(sp.getId()).setValue(newItem);
                            }
                        } else {
                            GioHangItem newItem = new GioHangItem(sp.getId(), sp.getTenSP(), sp.getGiaSP(),
                                    sp.getThumbnails().get(0), soLuong, sp.getTenNhaSX());
                            gioHangUserRef.child(sp.getId()).setValue(newItem);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Intent intent = new Intent(XemSanPhamActivity.this, KhachHangActivity.class);
                intent.putExtra("fragmentToOpen", "GioHangFragment");
                startActivity(intent);
            }
        });
    }
}