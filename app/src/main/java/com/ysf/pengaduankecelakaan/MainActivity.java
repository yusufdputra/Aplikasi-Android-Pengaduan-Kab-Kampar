package com.ysf.pengaduankecelakaan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ysf.pengaduankecelakaan.ui.DataPengaduanActivity;
import com.ysf.pengaduankecelakaan.ui.ItemPengaduan;
import com.ysf.pengaduankecelakaan.ui.LoginActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.AttributeSet;
import android.view.View;

import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String [] pusat_keramaian = {
            "Sekolah",
            "Pasar"
    };

    private ExtendedFloatingActionButton fab_add;
    private ImageView id_logo_dinas;
    FirebaseUser currentUser;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setOnClickListener();

        if (currentUser != null){
            fab_add.setVisibility(View.GONE);
        }else{
            fab_add.setVisibility(View.VISIBLE);
        }
    }



    private void init() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        fab_add = findViewById(R.id.fab);
        id_logo_dinas = findViewById(R.id.id_logo_dinas);
    }

    private void setOnClickListener() {
        fab_add.setOnClickListener(this);
        id_logo_dinas.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.fab:
                ItemPengaduan.newInstance(pusat_keramaian.length, pusat_keramaian).show(getSupportFragmentManager(), "dialog");
                break;
            case R.id.id_logo_dinas:
                if (currentUser != null) {
                    Intent intent = new Intent(this, DataPengaduanActivity.class);
                    startActivity(intent);
                    break;
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    break;
                }
        }
    }


}