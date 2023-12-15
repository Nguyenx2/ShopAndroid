package com.example.shopapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.shopapp.Models.BinhLuan;
import com.example.shopapp.R;

import java.util.ArrayList;

public class BinhLuanAdapter extends ArrayAdapter {
    Activity context;
    int resource;
    ArrayList<BinhLuan> listBinhLuan;
    public BinhLuanAdapter(@NonNull Activity context, int resource, ArrayList<BinhLuan> listBinhLuan) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.listBinhLuan = listBinhLuan;
    }

    @Override
    public int getCount() {
        return listBinhLuan.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View view = inflater.inflate(this.resource, null);

        ImageView imgDaiDien = view.findViewById(R.id.img_dai_dien);
        TextView tvTenKH = view.findViewById(R.id.tv_ten_nguoi_dung);
        TextView tvDanhGia = view.findViewById(R.id.tv_danh_gia);
        TextView tvThoiGian = view.findViewById(R.id.tv_thoi_gian);
        TextView tvBinhLuan = view.findViewById(R.id.tv_binh_luan);

        BinhLuan binhLuan = this.listBinhLuan.get(position);

        Glide.with(getContext()).load(binhLuan.getUser().getAnhDaiDien()).into(imgDaiDien);
        tvTenKH.setText(binhLuan.getUser().getTen());
        tvDanhGia.setText("Đánh giá: " + binhLuan.getDanhGia());
        tvThoiGian.setText(binhLuan.getThoiGian());
        tvBinhLuan.setText(binhLuan.getNoiDung());
        return view;
    }
}
