package com.example.shopapp.Utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmailUtils {
    public interface OnEmailSentListener {
        void sentSuccess();
        void sentError();
    }
    public static void guiEmail(String toEmail, String tieuDe, String noiDung, OnEmailSentListener listener) {
        try {
            String fromEmail = "shopapp.work@gmail.com";
            String emailPassword = "gqatxjroykiz onwr";
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
                        listener.sentSuccess();
                    } catch (Exception e) {
                        e.printStackTrace();
                        listener.sentError();
                    }
                }
            });
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

