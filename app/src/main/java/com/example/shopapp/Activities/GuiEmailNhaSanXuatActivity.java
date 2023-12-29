package com.example.shopapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shopapp.R;
import com.example.shopapp.Utils.SendEmailUtils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GuiEmailNhaSanXuatActivity extends AppCompatActivity {
    Toolbar tbGuiEmail;
    EditText edtEmailNSX, edtTieuDe, edtNoiDung;
    Button btnGuiEmail;
    String email;
    ProgressBar progressBar;
    LinearLayout layoutScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui_email_nha_san_xuat);

        initUi();
        initListener();
    }
    private void initUi(){
        tbGuiEmail = findViewById(R.id.tb_gui_email);
        edtEmailNSX = findViewById(R.id.edt_email_nsx);
        edtTieuDe = findViewById(R.id.edt_tieu_de);
        edtNoiDung = findViewById(R.id.edt_noi_dung);
        btnGuiEmail = findViewById(R.id.btn_gui_email);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        layoutScreen = findViewById(R.id.layout_screen);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        email = bundle.getString("email");

        edtEmailNSX.setText(email);
        actionToolBar();
    }
    private void actionToolBar(){
        setSupportActionBar(tbGuiEmail);
        tbGuiEmail.setNavigationIcon(R.drawable.back);
        tbGuiEmail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initListener(){
        btnGuiEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guiEmail();
                progressBar.setVisibility(View.VISIBLE);
                layoutScreen.setVisibility(View.INVISIBLE);
            }
        });
    }
    private void guiEmail(){
        String toEmail = edtEmailNSX.getText().toString().trim();
        String tieuDe = edtTieuDe.getText().toString().trim();
        String noiDung = edtNoiDung.getText().toString().trim();
        if (toEmail.length() > 0 && tieuDe.length() > 0 && noiDung.length() > 0){
            SendEmailUtils.guiEmail(toEmail, tieuDe, noiDung, new SendEmailUtils.OnEmailSentListener() {
                @Override
                public void sentSuccess() {
                    edtTieuDe.setText("");
                    edtNoiDung.setText("");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                            layoutScreen.setVisibility(View.VISIBLE);
                            Toast.makeText(GuiEmailNhaSanXuatActivity.this,
                                    "Gửi thành công !", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void sentError() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                            layoutScreen.setVisibility(View.VISIBLE);
                            Toast.makeText(GuiEmailNhaSanXuatActivity.this,
                                    "Gặp lỗi trong khi gửi !", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } else {
            Toast.makeText(this, "Vui lòng không để trống thông tin !", Toast.LENGTH_SHORT).show();
        }

    }
}