package com.ayub.projectpab;


import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("id")
    private int id;
    @SerializedName("nama_komponen")
    private String nama_komponen;
    @SerializedName("jenis")
    private String jenis;

    @SerializedName("merek")
    private String merek;
    @SerializedName("detail")
    private String detail;
    // Konstruktor untuk membuat objek User baru
    public Data(int id, String nama_komponen, String jenis, String merek, String detail) {
        this.id = id;
        this.nama_komponen = nama_komponen;
        this.jenis = jenis;
        this.merek = merek;
        this.detail = detail;
    }
    // Konstruktor untuk membuat objek User tanpa id (misalnya, untuk menambahkan user baru)
    public Data(String nama_komponen, String jenis, String merek, String detail) {
        this.nama_komponen = nama_komponen;
        this.jenis = jenis;
        this.merek = merek;
        this.detail = detail;
    }
    // Getter untuk mendapatkan id user
    public int getId() {
        return id;
    }
    // Setter untuk mengatur id user
    public void setId(int id) {
        this.id = id;
    }



    // Getter untuk mendapatkan nama user
    public String getNama_komponen() {
        return nama_komponen;
    }
    // Setter untuk mengatur nama user
    public void setName(String Komponen) {
        this.nama_komponen = nama_komponen;
    }
    // Getter untuk mendapatkan email user
    public String getJenis() {
        return jenis;
    }
    public void setEmail(String email) {
        this.jenis = jenis;
    }

    public String getMerek() {
        return merek;
    }
    public void setMerek(String Nik) {
        this.merek = merek;
    }
    public String getDetail() {
        return detail;
    }
    public void setDetail(String Detail) {
        this.detail = Detail;
    }
}