package com.ayub.projectpab;


import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("id")
    private int id;
    @SerializedName("nama_komponen")
    private String nama_komponen;
    @SerializedName("jenis")
    private String jenis;

    @SerializedName("merk")
    private String merk;
    @SerializedName("detail")
    private String detail;

    public Data(int id, String nama_komponen, String jenis, String merk, String detail) {
        this.id = id;
        this.nama_komponen = nama_komponen;
        this.jenis = jenis;
        this.merk = merk;
        this.detail = detail;
    }

    public Data(String nama_komponen, String jenis, String merk, String detail) {
        this.nama_komponen = nama_komponen;
        this.jenis = jenis;
        this.merk = merk;
        this.detail = detail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getNama_komponen() {
        return nama_komponen;
    }
    public void setNama_komponen(String nama_komponen) {
        this.nama_komponen = nama_komponen;
    }
    public String getJenis() {
        return jenis;
    }
    public void setEmail(String jenis) {
        this.jenis = jenis;
    }
    public String getMerk() {
        return merk;
    }
    public void setMerk(String merk) {
        this.merk = merk;
    }
    public String getDetail() {
        return detail;
    }
    public void setDetail(String Detail) {
        this.detail = Detail;
    }
}