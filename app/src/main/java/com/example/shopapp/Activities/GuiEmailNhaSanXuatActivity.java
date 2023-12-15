package com.example.shopapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shopapp.R;

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
        guiEmai();
    }
    private void guiEmai(){
        btnGuiEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String fromEmail = "shopapp.work@gmail.com";
                    String emailPassword = "gqatxjroykiz onwr";
                    String toEmail = edtEmailNSX.getText().toString().trim();
                    String tieuDe = edtTieuDe.getText().toString().trim();
                    String noiDung = edtNoiDung.getText().toString().trim();
                    String host = "smtp.gmail.com";
                    Properties properties = System.getProperties();
                    properties.put("mail.smtp.host", host);
                    properties.put("mail.smtp.port", "465");
                    properties.put("mail.smtp.ssl.enable", "true");
                    properties.put("mail.smtp.auth", "true");
                    Session session = Session.getInstance(properties, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(fromEmail, emailPassword);
                        }
                    });
                    MimeMessage mimeMessage = new MimeMessage(session);
                    mimeMessage.addRecipient(Message.RecipientType.TO,
                            new InternetAddress(toEmail));
                    mimeMessage.setSubject(tieuDe);
                    mimeMessage.setText(noiDung);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Transport.send(mimeMessage);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                    edtEmailNSX.setText(email);
                    edtTieuDe.setText("");
                    edtNoiDung.setText("");
                    Toast.makeText(GuiEmailNhaSanXuatActivity.this,
                            "Email đã được gửi tới " + toEmail, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(GuiEmailNhaSanXuatActivity.this,
                            "Có lỗi xảy ra ...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}