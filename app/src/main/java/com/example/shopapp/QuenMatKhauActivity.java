package com.example.shopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class QuenMatKhauActivity extends AppCompatActivity {
    EditText edtEmail;
    Button btnGui, btnQuayLai;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mat_khau);
        edtEmail = findViewById(R.id.edt_email);
        btnGui = findViewById(R.id.btn_gui);
        btnQuayLai = findViewById(R.id.btn_quay_lai);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String email = edtEmail.getText().toString().trim();
                if (email.length() > 0) {
                    auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(QuenMatKhauActivity.this,
                                                "Vui lòng kiểm tra email của bạn !", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(QuenMatKhauActivity.this, DangKyActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(QuenMatKhauActivity.this, "Không được để trống thông tin !", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}