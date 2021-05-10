package com.ysf.pengaduankecelakaan.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

public class PengaduansModel {
    String nama_pengaju, nama_jalan, nama_tempat, keluhan, path_lokasi1, path_lokasi2, path_ktp, kebutuhan_rambu;
    GeoPoint kordinat;
    Timestamp waktu_pengajuan;

    public PengaduansModel() {
    }

    public PengaduansModel(String nama_pengaju, String nama_jalan, String nama_tempat, String keluhan, String path_lokasi1, String path_lokasi2, String path_ktp, GeoPoint kordinat, Timestamp waktu_pengajuan, String kebutuhan_rambu) {
        this.nama_pengaju = nama_pengaju;
        this.nama_jalan = nama_jalan;
        this.nama_tempat = nama_tempat;
        this.keluhan = keluhan;
        this.path_lokasi1 = path_lokasi1;
        this.path_lokasi2 = path_lokasi2;
        this.path_ktp = path_ktp;
        this.kordinat = kordinat;
        this.waktu_pengajuan = waktu_pengajuan;
        this.kebutuhan_rambu = kebutuhan_rambu;
    }

    public String getKebutuhan_rambu() {
        return kebutuhan_rambu;
    }

    public void setKebutuhan_rambu(String kebutuhan_rambu) {
        this.kebutuhan_rambu = kebutuhan_rambu;
    }

    public Timestamp getWaktu_pengajuan() {
        return waktu_pengajuan;
    }

    public void setWaktu_pengajuan(Timestamp waktu_pengajuan) {
        this.waktu_pengajuan = waktu_pengajuan;
    }

    public String getNama_pengaju() {
        return nama_pengaju;
    }

    public void setNama_pengaju(String nama_pengaju) {
        this.nama_pengaju = nama_pengaju;
    }

    public String getNama_jalan() {
        return nama_jalan;
    }

    public void setNama_jalan(String nama_jalan) {
        this.nama_jalan = nama_jalan;
    }

    public String getNama_tempat() {
        return nama_tempat;
    }

    public void setNama_tempat(String nama_tempat) {
        this.nama_tempat = nama_tempat;
    }

    public String getKeluhan() {
        return keluhan;
    }

    public void setKeluhan(String keluhan) {
        this.keluhan = keluhan;
    }

    public String getPath_lokasi1() {
        return path_lokasi1;
    }

    public void setPath_lokasi1(String path_lokasi1) {
        this.path_lokasi1 = path_lokasi1;
    }

    public String getPath_lokasi2() {
        return path_lokasi2;
    }

    public void setPath_lokasi2(String path_lokasi2) {
        this.path_lokasi2 = path_lokasi2;
    }

    public String getPath_ktp() {
        return path_ktp;
    }

    public void setPath_ktp(String path_ktp) {
        this.path_ktp = path_ktp;
    }

    public GeoPoint getKordinat() {
        return kordinat;
    }

    public void setKordinat(GeoPoint kordinat) {
        this.kordinat = kordinat;
    }
}
