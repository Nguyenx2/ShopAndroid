package com.example.shopapp.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class SanPham implements Serializable {
    private String id;
    private String tenSP;
    private float giaSP;
    private String moTaSP;
    private ArrayList<String> thumbnails;
    private int soLuong;
    private int soLuongDaBan;
    private String nhaSanXuatId;
    private String tenNhaSX;
    public SanPham (){}

    public SanPham(String id, String tenSP, float giaSP, String moTaSP, ArrayList<String> thumbnails, int soLuong, int soLuongDaBan, String nhaSanXuatId, String tenNhaSX) {
        this.id = id;
        this.tenSP = tenSP;
        this.giaSP = giaSP;
        this.moTaSP = moTaSP;
        this.thumbnails = thumbnails;
        this.soLuong = soLuong;
        this.soLuongDaBan = soLuongDaBan;
        this.nhaSanXuatId = nhaSanXuatId;
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

    public String getMoTaSP() {
        return moTaSP;
    }

    public void setMoTaSP(String moTaSP) {
        this.moTaSP = moTaSP;
    }

    public ArrayList<String> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(ArrayList<String> thumbnails) {
        this.thumbnails = thumbnails;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getSoLuongDaBan() {
        return soLuongDaBan;
    }

    public void setSoLuongDaBan(int soLuongDaBan) {
        this.soLuongDaBan = soLuongDaBan;
    }

    public String getNhaSanXuatId() {
        return nhaSanXuatId;
    }

    public void setNhaSanXuatId(String nhaSanXuatId) {
        this.nhaSanXuatId = nhaSanXuatId;
    }

    public String getTenNhaSX() {
        return tenNhaSX;
    }

    public void setTenNhaSX(String tenNhaSX) {
        this.tenNhaSX = tenNhaSX;
    }
}
