package com.example.shopapp.Activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shopapp.Models.SanPham;
import com.example.shopapp.R;
import com.example.shopapp.Utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuaSanPhamActivity extends AppCompatActivity {
    ImageView imgAnhDaiDien1, imgAnhDaiDien2, imgAnhDaiDien3;
    Button btnChonAnh1, btnChonAnh2, btnChonAnh3;
    EditText edtTenSP, edtGiaSP, edtMoTaSP;
    Spinner spinnerNhaSX;
    Button btnLuu, btnQuayLai;

    List<String> listTenNhaSX = new ArrayList<>();
    Map<String, String> nhaSanXuatMap = new HashMap<>();
    ArrayList<String> listThumbnails = new ArrayList<>();
    ArrayList<Uri> imageUriList = new ArrayList<>();
    String nhaSanXuatId = null;
    DatabaseReference nhaSanXuatRef = FirebaseUtils.getChildRef("NhaSanXuat");
    DatabaseReference sanPhamRef = FirebaseUtils.getChildRef("SanPham");
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_san_pham);

        imgAnhDaiDien1 = findViewById(R.id.imgAnhDaiDien1);
        btnChonAnh1 = findViewById(R.id.btnChonAnh1);

        imgAnhDaiDien2 = findViewById(R.id.imgAnhDaiDien2);
        btnChonAnh2 = findViewById(R.id.btnChonAnh2);

        imgAnhDaiDien3 = findViewById(R.id.imgAnhDaiDien3);
        btnChonAnh3 = findViewById(R.id.btnChonAnh3);

        edtTenSP = findViewById(R.id.edtTenSP);
        edtGiaSP = findViewById(R.id.edtGiaSP);
        edtMoTaSP = findViewById(R.id.edtMoTaSP);
        spinnerNhaSX = findViewById(R.id.spinnerNhaSX);
        btnLuu = findViewById(R.id.btnLuu);
        btnQuayLai = findViewById(R.id.btnQuayLai);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        addEvents();

    }

    private void addEvents() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        SanPham sp = (SanPham) bundle.get("dataSuaSanPham");
        String id = sp.getId();

        edtTenSP.setText(sp.getTenSP());

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        edtGiaSP.setText(decimalFormat.format(sp.getGiaSP()));

        edtMoTaSP.setText(sp.getMoTaSP());

        Glide.with(getBaseContext()).load(sp.getThumbnails().get(0)).into(imgAnhDaiDien1);
        Glide.with(getBaseContext()).load(sp.getThumbnails().get(1)).into(imgAnhDaiDien2);
        Glide.with(getBaseContext()).load(sp.getThumbnails().get(2)).into(imgAnhDaiDien3);

        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        nhaSanXuatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    String id = data.child("id").getValue(String.class);
                    String tenNhaSX = data.child("tenNhaSX").getValue(String.class);
                    listTenNhaSX.add(tenNhaSX);
                    nhaSanXuatMap.put(tenNhaSX, id);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listTenNhaSX);
                adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                spinnerNhaSX.setAdapter(adapter);

                spinnerNhaSX.setSelection(adapter.getPosition(sp.getTenNhaSX()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Them anh
        ActivityResultLauncher chonAnhLaucher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        imageUriList.add(result);
                        imgAnhDaiDien1.setImageURI(result);
                    }
                }
        );

        btnChonAnh1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chonAnhLaucher.launch("image/*");
            }
        });

// Them anh 2
        ActivityResultLauncher chonAnhLaucher2 = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        imageUriList.add(result);
                        imgAnhDaiDien2.setImageURI(result);
                    }
                }
        );

        btnChonAnh2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chonAnhLaucher2.launch("image/*");
            }
        });

// Them anh 3
        ActivityResultLauncher chonAnhLaucher3 = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        imageUriList.add(result);
                        imgAnhDaiDien3.setImageURI(result);
                    }
                }
        );

        btnChonAnh3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chonAnhLaucher3.launch("image/*");
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String tenSP = edtTenSP.getText().toString().trim();
                float giaSP = Float.parseFloat(edtGiaSP.getText().toString().trim());
                String moTaSP = edtMoTaSP.getText().toString().trim();
                String selectedTenNhaSX = (String) spinnerNhaSX.getSelectedItem();

                if (selectedTenNhaSX != null && tenSP.length() > 0 && giaSP > 0 && moTaSP.length() > 0) {
                    String nhaSanXuatId = nhaSanXuatMap.get(selectedTenNhaSX);
                    SanPham sp =  new SanPham(id, tenSP, giaSP, moTaSP, listThumbnails , nhaSanXuatId, selectedTenNhaSX);

                    StorageReference anhSanPhamRef = FirebaseUtils.getChildStorageRef("AnhSanPham");

                    for (int i = 0; i < imageUriList.size(); i++) {
                        Uri imageUri = imageUriList.get(i);

                        String fileName = (i + 1) + ".jpg";

                        StorageReference imageRef = anhSanPhamRef.child(id).child(fileName);

                        imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        listThumbnails.add(imageUrl);
                                        if (listThumbnails.size() == imageUriList.size()) {
                                            sanPhamRef.child(id).setValue(sp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(SuaSanPhamActivity.this,
                                                                "Thêm sản phẩm mới thành công !", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    } else {
                                                        Toast.makeText(SuaSanPhamActivity.this,
                                                                "Lỗi khi thêm sản phẩm mới: " + task.getException(), Toast.LENGTH_SHORT).show();
                                                    }
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                }
                                            });
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        System.out.println("Tải ảnh lên bị lỗi: " + e.toString());
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                        });
                    }
                }
            }
        });
    }
}