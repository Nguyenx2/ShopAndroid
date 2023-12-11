package com.example.shopapp.Activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shopapp.Models.NhaSanXuat;
import com.example.shopapp.R;
import com.example.shopapp.Utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class ThemNhaSX extends AppCompatActivity {
    ImageView imgNhaSanXuat;
    EditText edtTenNhaSX, edtEmail, edtSDT, edtDiaChi;
    Button btnThemMoi, btnQuayLai, btnChonAnh;

    DatabaseReference nhaSanXuatRef = FirebaseUtils.getChildRef("NhaSanXuat");
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_nha_sx);

        addViews();
        addEvents();

    }

    private void addEvents(){
        // Them anh moi
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

        // Them nha san xuat moi
        btnThemMoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenNhaSX = edtTenNhaSX.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String soDienThoai = edtSDT.getText().toString().trim();
                String diaChi = edtDiaChi.getText().toString().trim();
                if (tenNhaSX.length() > 0 && soDienThoai.length() > 0 && email.length() > 0
                && diaChi.length() > 0) {
                    String id = nhaSanXuatRef.push().getKey().toString();
                    NhaSanXuat nhaSanXuat = new NhaSanXuat(id, tenNhaSX, email, soDienThoai, diaChi, "");

                    StorageReference anhDaiDienRef
                            = storageReference.child("AnhNhaSanXuat").child(id + ".jpg");

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
                                                        Toast.makeText(ThemNhaSX.this,
                                                                "Thêm nhà sản xuất mới thành công !", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    } else {
                                                        Toast.makeText(ThemNhaSX.this,
                                                                "Lỗi thêm nhà sản xuất " + task.getException(), Toast.LENGTH_SHORT).show();
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

    private void addViews() {
        imgNhaSanXuat = findViewById(R.id.imgNhaSanXuat);
        edtTenNhaSX = findViewById(R.id.edtTenNhaSX);
        edtEmail = findViewById(R.id.edtEmail);
        edtSDT = findViewById(R.id.edtSDT);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        btnChonAnh = findViewById(R.id.btnChonAnh);
        btnThemMoi = findViewById(R.id.btnThemMoi);
        btnQuayLai = findViewById(R.id.btnQuayLai);
    }
}