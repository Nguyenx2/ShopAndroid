package com.example.shopapp.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.shopapp.Models.NhaSanXuat;
import com.example.shopapp.Models.SanPham;
import com.example.shopapp.R;

import java.util.ArrayList;

public class NhaSanXuatAdapter extends ArrayAdapter {
    Activity context;
    int resource;
    public ArrayList<NhaSanXuat> listNhaSanXuat, listNSXBackup, listNSXFilter;

    public NhaSanXuatAdapter(Activity context, int resource, ArrayList<NhaSanXuat> listNhaSanXuat) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.listNhaSanXuat = this.listNSXBackup = listNhaSanXuat;
    }

    @NonNull
    @Override
    public Activity getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public ArrayList<NhaSanXuat> getListNhaSanXuat() {
        return listNhaSanXuat;
    }

    public void setListNhaSanXuat(ArrayList<NhaSanXuat> listNhaSanXuat) {
        this.listNhaSanXuat = listNhaSanXuat;
    }

    @Override
    public int getCount() {
        return listNhaSanXuat.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View customView = inflater.inflate(this.resource, null);

        ImageView imgNhaSanXuat = customView.findViewById(R.id.imgNhaSanXuat);
        TextView tvTenNhaSX = customView.findViewById(R.id.tvTenNhaSX);
        TextView tvEmail = customView.findViewById(R.id.tvEmail);
        TextView tvSDT = customView.findViewById(R.id.tvSDT);
        TextView tvDiaChi = customView.findViewById(R.id.tvDiaChi);


        NhaSanXuat nhaSanXuat = this.listNhaSanXuat.get(position);

        tvTenNhaSX.setText(nhaSanXuat.getTenNhaSX());
        tvEmail.setText(nhaSanXuat.getEmail());
        tvSDT.setText(nhaSanXuat.getSoDienThoai());
        tvDiaChi.setText(nhaSanXuat.getDiaChi());

        if (nhaSanXuat.getAnhDaiDien().trim().length() > 0) {
            Glide.with(context.getBaseContext()).load(nhaSanXuat.getAnhDaiDien()).into(imgNhaSanXuat);
        }

        return customView;
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
                    listNSXFilter = listNSXBackup;
                } else {
                    listNSXFilter = new ArrayList<>();
                    for (NhaSanXuat nsx : listNSXBackup) {
                        if (nsx.getTenNhaSX().toLowerCase().contains(query)
                                || nsx.getEmail().toLowerCase().contains(query)) {
                            listNSXFilter.add(nsx);
                        }
                    }
                }
                filterResult.values = listNSXFilter;
                return filterResult;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listNhaSanXuat = (ArrayList<NhaSanXuat>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
