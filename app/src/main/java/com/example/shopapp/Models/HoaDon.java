package com.example.shopapp.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class HoaDon implements Serializable {
    private String id;
    private String userId;
    private String hoTen;
    private String email;
    private String sdt;
    private String diaChi;
    private ArrayList<GioHangItem> listGioHangItem;
    private float tongTien;
    private String createdAt;
    public HoaDon(){};

    public HoaDon(String id, String userId, String hoTen, String email, String sdt, String diaChi, ArrayList<GioHangItem> listGioHangItem, float tongTien, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.hoTen = hoTen;
        this.email = email;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.listGioHangItem = listGioHangItem;
        this.tongTien = tongTien;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public ArrayList<GioHangItem> getListGioHangItem() {
        return listGioHangItem;
    }

    public void setListGioHangItem(ArrayList<GioHangItem> listGioHangItem) {
        this.listGioHangItem = listGioHangItem;
    }

    public float getTongTien() {
        return tongTien;
    }

    public void setTongTien(float tongTien) {
        this.tongTien = tongTien;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
