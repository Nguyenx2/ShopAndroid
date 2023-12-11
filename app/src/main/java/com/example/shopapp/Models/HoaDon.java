package com.example.shopapp.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class HoaDon implements Serializable {
    private String id;
    private String userId;
    private ArrayList<GioHangItem> listGioHangItem;
    private float tongTien;
    private String createdAt;

    public HoaDon(String id, String userId, ArrayList<GioHangItem> listGioHangItem, float tongTien, String createdAt) {
        this.id = id;
        this.userId = userId;
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
