package com.poly.DtoEntity;

import java.util.List;

public class SanPhamKhuyenMaiRequest {
    private int idKhuyenMai;
    private List<Integer> sanPhams;

    // Getters và setters
    public int getIdKhuyenMai() {
        return idKhuyenMai;
    }

    public void setIdKhuyenMai(int idKhuyenMai) {
        this.idKhuyenMai = idKhuyenMai;
    }

    public List<Integer> getSanPhams() {
        return sanPhams;
    }

    public void setSanPhams(List<Integer> sanPhams) {
        this.sanPhams = sanPhams;
    }
}
