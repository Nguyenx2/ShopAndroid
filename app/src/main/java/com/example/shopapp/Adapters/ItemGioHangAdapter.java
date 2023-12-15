package com.example.shopapp.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.shopapp.Models.GioHangItem;
import com.example.shopapp.Models.NhaSanXuat;
import com.example.shopapp.R;
import com.example.shopapp.Utils.FirebaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ItemGioHangAdapter extends ArrayAdapter {
    Activity context;
    int resource;
    public ArrayList<GioHangItem> listItemGH;
    public ItemGioHangAdapter(@NonNull Activity context, int resource, ArrayList<GioHangItem> listItemGH) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.listItemGH = listItemGH;
    }

    @Override
    public int getCount() {
        return listItemGH.size();
    }

    DatabaseReference gioHangRef = FirebaseUtils.getChildRef("GioHang");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View view = inflater.inflate(this.resource, null);

        ImageView imgDaiDien = view.findViewById(R.id.img_dai_dien);
        TextView tvTenSP = view.findViewById(R.id.tv_ten_sp);
        TextView tvGiaSP = view.findViewById(R.id.tv_gia_sp);
        TextView tvTenNhaSX = view.findViewById(R.id.tv_ten_nha_sx);
        Button btnTru = view.findViewById(R.id.btn_tru);
        Button btnCong = view.findViewById(R.id.btn_cong);
        Button btnHuy = view.findViewById(R.id.btn_huy);
        Button btnGiaTri = view.findViewById(R.id.btn_gia_tri);

        GioHangItem item = listItemGH.get(position);

        String userId = user.getUid();
        DatabaseReference itemRef = gioHangRef.child(userId).child(item.getId());

        tvTenSP.setText(item.getTenSP());
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        tvGiaSP.setText("Giá: " + decimalFormat.format(item.getGiaSP()) + " VNĐ");
        Glide.with(getContext()).load(item.getAnhDaiDien()).into(imgDaiDien);
        btnGiaTri.setText(String.valueOf(item.getSoLuong()));
        tvTenNhaSX.setText("Nhà sản xuất: " + item.getTenNhaSX());

        if (item.getSoLuong() == 1) {
            btnTru.setVisibility(View.INVISIBLE);
        }

        //Them san pham vao gio hang
        btnCong.setOnClickListener(new View.OnClickListener() {
            int soLuong = item.getSoLuong();
            @Override
            public void onClick(View view) {
                soLuong += 1;
                btnGiaTri.setText(String.valueOf(soLuong));
                itemRef.child("soLuong").setValue(soLuong);
            }
        });

        //Giam so san pham muon mua
        btnTru.setOnClickListener(new View.OnClickListener() {
            int soLuong = item.getSoLuong();
            @Override
            public void onClick(View view) {
                if (soLuong == 1){
                    btnTru.setVisibility(View.INVISIBLE);
                } else {
                    soLuong -= 1;
                    btnTru.setVisibility(View.VISIBLE);
                }
                btnGiaTri.setText(String.valueOf(soLuong));
                itemRef.child("soLuong").setValue(soLuong);
            }
        });

        // Xoa san pham khoi gio hang
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận");
                builder.setMessage("Bạn có muốn xóa sản phẩm này khỏi giỏ hàng không ?");

                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        itemRef.removeValue();
                        listItemGH.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Xóa thành công !", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return view;
    }
}
