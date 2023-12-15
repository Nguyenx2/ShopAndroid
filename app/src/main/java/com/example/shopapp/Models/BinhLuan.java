package com.example.shopapp.Models;

import java.io.Serializable;

public class BinhLuan implements Serializable {
    private User user;
    private String thoiGian;
    private String noiDung;
    private Long danhGia;
    public BinhLuan(){};

    public BinhLuan(User user, String thoiGian, String noiDung, Long danhGia) {
        this.user = user;
        this.thoiGian = thoiGian;
        this.noiDung = noiDung;
        this.danhGia = danhGia;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public Long getDanhGia() {
        return danhGia;
    }

    public void setDanhGia(Long danhGia) {
        this.danhGia = danhGia;
    }
}
