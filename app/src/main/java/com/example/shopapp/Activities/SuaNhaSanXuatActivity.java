package com.example.shopapp.Activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shopapp.Models.NhaSanXuat;
import com.example.shopapp.R;
import com.example.shopapp.Utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class SuaNhaSanXuatActivity extends AppCompatActivity {
    TextView tvTieuDe;
    ImageView imgNhaSanXuat;
    EditText edtTenNhaSX, edtEmail, edtSDT, edtDiaChi;
    Button btnSua, btnQuayLai, btnChonAnh;
    DatabaseReference nhaSanXuatRef = FirebaseUtils.getChildRef("NhaSanXuat");
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_nha_san_xuat);

        tvTieuDe = findViewById(R.id.tvTieuDe);
        imgNhaSanXuat = findViewById(R.id.imgNhaSanXuat);
        edtTenNhaSX = findViewById(R.id.edtTenNhaSX);
        edtEmail = findViewById(R.id.edtEmail);
        edtSDT = findViewById(R.id.edtSDT);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        btnChonAnh = findViewById(R.id.btnChonAnh);
        btnSua = findViewById(R.id.btnSua);
        btnQuayLai = findViewById(R.id.btnQuayLai);
        addEvents();

    }

    private void addEvents() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String id = bundle.getString("id");
        nhaSanXuatRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NhaSanXuat nhaSanXuat = snapshot.getValue(NhaSanXuat.class);
                tvTieuDe.setText("Sửa nhà sản xuất: " + nhaSanXuat.getTenNhaSX());
                edtTenNhaSX.setText(nhaSanXuat.getTenNhaSX());
                edtEmail.setText(nhaSanXuat.getEmail());
                edtSDT.setText(nhaSanXuat.getSoDienThoai());
                edtDiaChi.setText(nhaSanXuat.getDiaChi());
                if (nhaSanXuat.getAnhDaiDien().trim().length() > 0) {
                    Glide.with(getBaseContext()).load(nhaSanXuat.getAnhDaiDien()).into(imgNhaSanXuat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ActivityResultLauncher chonAnhLaucher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri reusult) {
                        imgNhaSanXuat.setImageURI(reusult);
                    }
                }
        );

        btnChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chonAnhLaucher.launch("image/*");
            }
        });

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenNhaSX = edtTenNhaSX.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String soDienThoai = edtSDT.getText().toString().trim();
                String diaChi = edtDiaChi.getText().toString().trim();
                if (tenNhaSX.length() > 0 && soDienThoai.length() > 0 && email.length() > 0
                        && diaChi.length() > 0) {
                    NhaSanXuat nhaSanXuat = new NhaSanXuat(id, tenNhaSX, email, soDienThoai, diaChi, "");

                    StorageReference anhDaiDienRef
                            = storageReference.child("AnhDaiDien").child(id + ".jpg");

                    BitmapDrawable bitmapDrawable = (BitmapDrawable) imgNhaSanXuat.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baoStream);
                    byte[] imageData = baoStream.toByteArray();
                    anhDaiDienRef.putBytes(imageData)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    anhDaiDienRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String linkAnhDaiDien = uri.toString();
                                            nhaSanXuat.setAnhDaiDien(linkAnhDaiDien);
                                            nhaSanXuatRef.child(id).setValue(nhaSanXuat).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getBaseContext(),
                                                                "Sửa nhà sản xuất thành công !", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    } else {
                                                        Toast.makeText(getBaseContext(),
                                                                "Lỗi sửa nhà sản xuất " + task.getException(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                }
            }
        });
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}