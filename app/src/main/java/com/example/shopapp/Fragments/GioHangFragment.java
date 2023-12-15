package com.example.shopapp.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopapp.Adapters.ItemGioHangAdapter;
import com.example.shopapp.Models.GioHangItem;
import com.example.shopapp.Models.HoaDon;
import com.example.shopapp.Models.User;
import com.example.shopapp.R;
import com.example.shopapp.Utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GioHangFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GioHangFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GioHangFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GioHangFragment newInstance(String param1, String param2) {
        GioHangFragment fragment = new GioHangFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ItemGioHangAdapter itemGioHangAdapter;
    ArrayList<GioHangItem> gioHangItems = new ArrayList<>();
    ListView lvGioHang;
    TextView tvThongBao, tvTongTien;
    Button btnDatHang;
    HoaDon hoaDon;
    DatabaseReference hoaDonRef = FirebaseUtils.getChildRef("HoaDon");

    DatabaseReference gioHangRef = FirebaseUtils.getChildRef("GioHang");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<GioHangItem> listGioHangItem = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        tvThongBao = view.findViewById(R.id.tv_thong_bao);
        tvTongTien = view.findViewById(R.id.tv_tong_tien);
        btnDatHang = view.findViewById(R.id.btn_dat_hang);

        lvGioHang = view.findViewById(R.id.lv_gio_hang);
        itemGioHangAdapter = new ItemGioHangAdapter(getActivity(), R.layout.lv_item_gio_hang, gioHangItems);
        lvGioHang.setAdapter(itemGioHangAdapter);

        initListener();

        return view;
    }
    private void initListener(){
        docDuLieu();
        datHang();
    }
    private void docDuLieu(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        DatabaseReference userGioHangRef = gioHangRef.child(userId);
        userGioHangRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gioHangItems.clear();
                float tongTien = 0;
                for (DataSnapshot data : snapshot.getChildren()) {
                    GioHangItem gioHangItem = data.getValue(GioHangItem.class);
                    if (gioHangItem != null) {
                        gioHangItems.add(gioHangItem);
                    }
                }
                if (gioHangItems.size() > 0){
                    tvThongBao.setVisibility(View.INVISIBLE);
                }
                for (int i = 0; i < gioHangItems.size(); i++){
                    tongTien += gioHangItems.get(i).getGiaSP() * gioHangItems.get(i).getSoLuong();
                }
                DecimalFormat decimalFormat = new DecimalFormat("#,###");
                tvTongTien.setText(decimalFormat.format(tongTien) + " VNĐ");
                itemGioHangAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void datHang(){
        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hienThiXacNhanTT();
            }
        });
    }
    private void hienThiXacNhanTT(){
        // Thong tin hoa don
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_user_info, null);
        builder.setView(dialogView);
        EditText edtTen = dialogView.findViewById(R.id.edt_ten);
        EditText edtSDT = dialogView.findViewById(R.id.edt_sdt);
        EditText edtEmail = dialogView.findViewById(R.id.edt_email);
        EditText edtDiaChi = dialogView.findViewById(R.id.edt_dia_chi);

        DatabaseReference userRef = FirebaseUtils.getChildRef("User");
        userRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    User user1 =snapshot.getValue(User.class);
                    edtTen.setText(user1.getTen());
                    edtSDT.setText(user1.getSoDienThoai());
                    edtEmail.setText(user1.getEmail());
                    edtDiaChi.setText(user1.getDiaChi());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        builder.setTitle("Xác nhận thông tin giao hàng: ");
        builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int j) {
                String id = hoaDonRef.push().getKey().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String thoiGian = sdf.format(new Date());
                String hoTen = edtTen.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String sdt = edtSDT.getText().toString().trim();
                String diaChi = edtDiaChi.getText().toString().trim();
                float tongTien = 0;
                for (int i = 0; i < gioHangItems.size(); i++){
                    tongTien += gioHangItems.get(i).getGiaSP() * gioHangItems.get(i).getSoLuong();
                }
                if (hoTen.length() > 0 && email.length() > 0 && sdt.length() > 0&& diaChi.length() >0){
                    HoaDon hd = new HoaDon(id, user.getUid(), hoTen, email,
                            sdt, diaChi, listGioHangItem, tongTien, thoiGian);
                    gioHangRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot data : snapshot.getChildren()){
                                GioHangItem gioHangItem = data.getValue(GioHangItem.class);
                                listGioHangItem.add(gioHangItem);
                            }
                            hoaDonRef.child(id).setValue(hd).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        gioHangRef.child(user.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getActivity(), "Thêm hóa đơn thành công !", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getActivity(), "Lỗi xử lý !", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                            Toast.makeText(getActivity().getBaseContext(), "Them hoa don thanh cong !", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Vui lòng điền đầy đủ thông tin !", Toast.LENGTH_SHORT).show();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}