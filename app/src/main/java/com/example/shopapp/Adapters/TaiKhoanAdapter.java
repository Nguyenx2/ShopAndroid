package com.example.shopapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.shopapp.Models.User;
import com.example.shopapp.R;

import java.util.ArrayList;
import java.util.List;

public class TaiKhoanAdapter extends ArrayAdapter {
    Activity context;
    int resource;
    ArrayList<User> listUser;

    public TaiKhoanAdapter(@NonNull Activity context, int resource, ArrayList<User> listUser) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.listUser = listUser;
    }

    @Override
    public int getCount() {
        return listUser.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View view = inflater.inflate(this.resource, null);

        ImageView imgDaiDien = view.findViewById(R.id.img_dai_dien);
        TextView tvTen = view.findViewById(R.id.tv_ten_nguoi_dung);
        TextView tvId = view.findViewById(R.id.tv_id);
        TextView tvVaiTro = view.findViewById(R.id.tv_vai_tro);

        User user = this.listUser.get(position);
        Glide.with(getContext()).load(user.getAnhDaiDien()).into(imgDaiDien);
        tvTen.setText(user.getTen());
        tvId.setText("ID: " + user.getId());
        tvVaiTro.setText("Vai trò: " + user.getRole());
        return view;
    }
}
