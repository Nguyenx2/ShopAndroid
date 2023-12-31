package com.example.shopapp.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shopapp.DangNhapActivity;
import com.example.shopapp.Models.User;
import com.example.shopapp.R;
import com.example.shopapp.Utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CaNhanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CaNhanFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CaNhanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CaNhanFragment newInstance(String param1, String param2) {
        CaNhanFragment fragment = new CaNhanFragment();
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
    EditText edtTen, edtEmail, edtSDT, edtDiaChi;
    Button btnLuuAnh, btnLuuThongTin, btnDangXuat;
    Toolbar tbCaNhan;
    ImageView imgAvt;
    Uri uriAvt;
    String imageUrl;
    DatabaseReference userRef = FirebaseUtils.getChildRef("User");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        imgAvt = view.findViewById(R.id.img_avt);
        edtTen = view.findViewById(R.id.edt_ten);
        edtEmail = view.findViewById(R.id.edt_email);
        edtSDT = view.findViewById(R.id.edt_sdt);
        edtDiaChi = view.findViewById(R.id.edt_dia_chi);
        btnLuuAnh = view.findViewById(R.id.btn_luu_anh);
        tbCaNhan = view.findViewById(R.id.tb_ca_nhan);
        btnLuuThongTin = view.findViewById(R.id.btn_thay_doi_tt);
        btnDangXuat = view.findViewById(R.id.btn_dang_xuat);

        initListener();
        return view;
    }
    private void initListener(){
        docDuLieu();
        chonAnh();
        luuAnh();
        luuThongTin();
        dangXuat();
    }
    private void dangXuat(){
        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Xác nhận muốn đăng xuất ? ");
                builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getActivity(), DangNhapActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
    private void docDuLieu(){
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()){
                    if (data.getKey().equals(user.getUid())){
                        User user1 = data.getValue(User.class);
                        Glide.with(getActivity().getBaseContext()).load(user1.getAnhDaiDien()).into(imgAvt);
                        edtTen.setText(user1.getTen());
                        edtEmail.setText(user1.getEmail());
                        edtSDT.setText(user1.getSoDienThoai());
                        edtDiaChi.setText(user1.getDiaChi());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void chonAnh(){
        ActivityResultLauncher chonAnhLaucher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        uriAvt = result;
                        imgAvt.setImageURI(result);
                    }
                }
        );

        imgAvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chonAnhLaucher.launch("image/*");
            }
        });
    }
    private void luuAnh(){
        btnLuuAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorageReference imageRef = FirebaseUtils.getChildStorageRef("AnhNguoiDung")
                        .child(user.getUid()).child(1 + ".jpg");
                imageRef.putFile(uriAvt).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageUrl = uri.toString();
                                userRef.child(user.getUid()).child("anhDaiDien").setValue(imageUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(getActivity(), "Lưu ảnh thành công !", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), "Lỗi !", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Tải ảnh lên bị lỗi !", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void luuThongTin(){
        btnLuuThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hoTen = edtTen.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String sdt = edtSDT.getText().toString().trim();
                String diaChi = edtDiaChi.getText().toString().trim();
                if (hoTen.length() > 0 && email.length() > 0 && sdt.length() > 0 && diaChi.length() > 0){
                    User resetUser = new User(
                            user.getUid(),
                            hoTen,
                            email,
                            sdt,
                            diaChi,
                            "",
                            "customer");
                    if (imageUrl.length() > 0){
                        resetUser.setAnhDaiDien(imageUrl);
                        userRef.child(user.getUid()).setValue(resetUser);
                    } else {
                        resetUser.setAnhDaiDien("https://firebasestorage.googleapis.com/v0/b/shopapp-8cf03.appspot.com/o/AnhNguoiDung%2FAnhMacDinh%2Favt.jpg?alt=media&token=6ccfb226-a492-4eea-8260-912bb44046bb");
                        userRef.child(user.getUid()).setValue(resetUser);
                    }
                    Toast.makeText(getActivity().getBaseContext(), "Lưu thông tin thành công !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getBaseContext(), "Vui lòng điền đầy đủ thông tin !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}