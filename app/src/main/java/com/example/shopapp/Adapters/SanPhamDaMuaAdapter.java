package com.example.shopapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.shopapp.Models.GioHangItem;
import com.example.shopapp.R;
import com.example.shopapp.UserActivities.DanhGiaActivity;
import com.example.shopapp.Utils.FirebaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SanPhamDaMuaAdapter extends ArrayAdapter {
    Activity context;
    int resource;
    ArrayList<GioHangItem> listSanPhamDaMua;
    public SanPhamDaMuaAdapter(@NonNull Activity context, int resource, ArrayList<GioHangItem> listSanPhamDaMua) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.listSanPhamDaMua = listSanPhamDaMua;
    }

    @Override
    public int getCount() {
        return listSanPhamDaMua.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View view = inflater.inflate(this.resource, null);

        ImageView imgDaiDien = view.findViewById(R.id.img_dai_dien);
        TextView tvTenSP = view.findViewById(R.id.tv_ten_sp);
        TextView tvGiaSP = view.findViewById(R.id.tv_gia_sp);
        TextView tvSoLuong = view.findViewById(R.id.tv_so_luong);
        TextView tvTenNhaSX = view.findViewById(R.id.tv_nha_sx);

        Button btnDanhGia = view.findViewById(R.id.btn_danh_gia);

        GioHangItem item = this.listSanPhamDaMua.get(position);
        tvTenSP.setText(item.getTenSP());
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        tvGiaSP.setText("Giá: " + decimalFormat.format(item.getGiaSP()) + " VNĐ");
        Glide.with(context.getBaseContext()).load(item.getAnhDaiDien()).into(imgDaiDien);
        tvSoLuong.setText("Đã mua: " + item.getSoLuong());
        tvTenNhaSX.setText("Nhà sản xuất: " + item.getTenNhaSX());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = FirebaseUtils.getChildRef("User");
        userRef.child(user.getUid()).child("role").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userRole = snapshot.getValue(String.class);
                if (userRole.equals("admin")) {
                    btnDanhGia.setVisibility(View.GONE);
                } else {
                    btnDanhGia.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnDanhGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DanhGiaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", item);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return view;
    }
}
