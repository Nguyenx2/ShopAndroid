package com.example.shopapp.Models;

import java.io.Serializable;

public class GioHangItem implements Serializable {
    private String id;
    private String tenSP;
    private float giaSP;
    private String anhDaiDien;
    private int soLuong;
    private String tenNhaSX;
    public GioHangItem(){};

    public GioHangItem(String id, String tenSP, float giaSP, String anhDaiDien, int soLuong, String tenNhaSX) {
        this.id = id;
        this.tenSP = tenSP;
        this.giaSP = giaSP;
        this.anhDaiDien = anhDaiDien;
        this.soLuong = soLuong;
        this.tenNhaSX = tenNhaSX;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public float getGiaSP() {
        return giaSP;
    }

    public void setGiaSP(float giaSP) {
        this.giaSP = giaSP;
    }

    public String getAnhDaiDien() {
        return anhDaiDien;
    }

    public void setAnhDaiDien(String anhDaiDien) {
        this.anhDaiDien = anhDaiDien;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getTenNhaSX() {
        return tenNhaSX;
    }

    public void setTenNhaSX(String tenNhaSX) {
        this.tenNhaSX = tenNhaSX;
    }
}
