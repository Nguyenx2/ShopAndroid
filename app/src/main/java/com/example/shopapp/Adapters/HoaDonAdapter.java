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
import com.example.shopapp.Models.HoaDon;
import com.example.shopapp.R;
import com.example.shopapp.UserActivities.KhachHangActivity;
import com.example.shopapp.UserActivities.XemChiTietHoaDon;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class HoaDonAdapter extends ArrayAdapter {
    Activity context;
    int resource;
    ArrayList<HoaDon> listHoaDon;

    public HoaDonAdapter(@NonNull Activity context, int resource, ArrayList<HoaDon> listHoaDon) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.listHoaDon = listHoaDon;
    }

    @Override
    public int getCount() {
        return listHoaDon.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View view = inflater.inflate(this.resource, null);
        TextView tvTongTien = view.findViewById(R.id.tv_tong_tien);
        TextView tvThoiGian = view.findViewById(R.id.tv_thoi_gian);
        Button btnXemChiTiet = view.findViewById(R.id.btn_xem_chi_tiet);

        HoaDon hd = this.listHoaDon.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        tvTongTien.setText("Giá tiền: " + decimalFormat.format(hd.getTongTien()) + " VNĐ");
        tvThoiGian.setText(hd.getCreatedAt());
        if (btnXemChiTiet != null) {
            btnXemChiTiet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), XemChiTietHoaDon.class );
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("hd", hd);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }

        return view;
    }
}
