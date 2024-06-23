package com.ayub.projectpab;


import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;


    @SerializedName("NIK")
    private String NIK;
    @SerializedName("NIM")
    private String NIM;
    // Konstruktor untuk membuat objek User baru
    public User(int id, String name, String email, String NIK, String NIM) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.NIK = NIK;
        this.NIM = NIM;
    }
    // Konstruktor untuk membuat objek User tanpa id (misalnya, untuk menambahkan user baru)
    public User(String name, String email, String NIK, String NIM) {
        this.name = name;
        this.email = email;
        this.NIK = NIK;
        this.NIM = NIM;
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
    public String getName() {
        return name;
    }
    // Setter untuk mengatur nama user
    public void setName(String name) {
        this.name = name;
    }
    // Getter untuk mendapatkan email user
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getNik() {
        return NIK;
    }
    public void setNIK(String Nik) {
        this.NIK = Nik;
    }
    public String getNim() {
        return NIM;
    }
    public void setNIM(String Nim) {
        this.NIM = Nim;
    }
}