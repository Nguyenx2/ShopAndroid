package com.example.shopapp.Models;

import java.io.Serializable;

public class NhaSanXuat implements Serializable {
    private String id;
    private String tenNhaSX;
    private String email;
    private String soDienThoai;
    private String diaChi;

    private String anhDaiDien;

    public NhaSanXuat() {};

    public NhaSanXuat(String id, String tenNhaSX, String email, String soDienThoai, String diaChi, String anhDaiDien) {
        this.id = id;
        this.tenNhaSX = tenNhaSX;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.anhDaiDien = anhDaiDien;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenNhaSX() {
        return tenNhaSX;
    }

    public void setTenNhaSX(String tenNhaSX) {
        this.tenNhaSX = tenNhaSX;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getAnhDaiDien() {
        return anhDaiDien;
    }

    public void setAnhDaiDien(String anhDaiDien) {
        this.anhDaiDien = anhDaiDien;
    }
}
