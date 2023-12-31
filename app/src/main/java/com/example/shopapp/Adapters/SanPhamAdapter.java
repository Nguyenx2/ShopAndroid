package com.example.shopapp.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.shopapp.Activities.QuanLySanPhamActivity;
import com.example.shopapp.Activities.SuaNhaSanXuatActivity;
import com.example.shopapp.Activities.SuaSanPhamActivity;
import com.example.shopapp.Activities.XemChiTietSanPhamActivity;
import com.example.shopapp.Models.NhaSanXuat;
import com.example.shopapp.Models.SanPham;
import com.example.shopapp.R;
import com.example.shopapp.Utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SanPhamAdapter extends ArrayAdapter {
    Activity context;
    int resource;
    public ArrayList<SanPham> listSanPham, listSanPhamFilter, listSanPhamBackup;

    public SanPhamAdapter(@NonNull Activity context, int resource, ArrayList<SanPham> listSanPham) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.listSanPham = this.listSanPhamBackup = listSanPham;
    }

    @Override
    public int getCount() {
        return listSanPham.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View view = inflater.inflate(this.resource, null);

        ImageView imgAnhDaiDien = view.findViewById(R.id.imgAnhDaiDien);
        TextView tvTenSP = view.findViewById(R.id.tvTenSP);
        TextView tvGiaSP = view.findViewById(R.id.tvGiaSP);
        TextView tvTenNhaSX = view.findViewById(R.id.tvTenNhaSX);
        TextView tvSoLuongDaBan = view.findViewById(R.id.tvSoLuongDaBan);
        LinearLayout layoutControl = view.findViewById(R.id.layout_control);
        Button btnXemChiTiet = view.findViewById(R.id.btnXemChiTiet);
        Button btnSua = view.findViewById(R.id.btnSua);
        Button btnXoa = view.findViewById(R.id.btnXoa);

        SanPham sp = this.listSanPham.get(position);

        tvTenSP.setText(sp.getTenSP());
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        tvGiaSP.setText("Giá: " + decimalFormat.format(sp.getGiaSP()) + " VNĐ");

        if (getClass().getSimpleName().equals("ThongKeActivity")) {
            tvSoLuongDaBan.setVisibility(View.VISIBLE);
            layoutControl.setVisibility(View.GONE);
        }
        tvSoLuongDaBan.setText("Đã bán: " + sp.getSoLuongDaBan());
        if (sp.getThumbnails().size() > 0) {
            Glide.with(context.getBaseContext()).load(sp.getThumbnails().get(0)).into(imgAnhDaiDien);
        }

        if (tvTenNhaSX != null) {
            tvTenNhaSX.setText(sp.getTenNhaSX());
        }

        if (btnXemChiTiet != null) {
            btnXemChiTiet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), XemChiTietSanPhamActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dataXemChiTiet", sp);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
        if (btnSua != null) {
            btnSua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), SuaSanPhamActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dataSuaSanPham", sp);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
        if (btnXoa != null) {
            btnXoa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Xác nhận");
                    builder.setMessage("Bạn có muốn xóa sản phẩm này không ?");

                    builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatabaseReference sanPhamRef = FirebaseUtils.getChildRef("SanPham");
                            sanPhamRef.child(sp.getId()).removeValue();
                            StorageReference anhSanPhamRef = FirebaseUtils.getChildStorageRef("AnhSanPham");
                            for (int j = 0; j < sp.getThumbnails().size(); j++) {
                                String fileName = j + 1 + ".jpg";
                                StorageReference imageRef = anhSanPhamRef.child(sp.getId()).child(fileName);
                                imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("TAG", "Xóa thành công !");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("TAG", "Gặp lỗi: " + e.getMessage());
                                    }
                                });

                            }
                            listSanPham.remove(position);
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
        }
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResult = new FilterResults();
                String query = charSequence.toString().trim().toLowerCase();
                if (query.length() < 1) {
                    listSanPhamFilter = listSanPhamBackup;
                } else {
                    listSanPhamFilter = new ArrayList<>();
                    for (SanPham sp : listSanPhamBackup) {
                        if (sp.getTenNhaSX().toLowerCase().contains(query)
                                || sp.getTenSP().toLowerCase().contains(query)) {
                            listSanPhamFilter.add(sp);
                        }
                    }
                }
                filterResult.values = listSanPhamFilter;
                return filterResult;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listSanPham = (ArrayList<SanPham>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
