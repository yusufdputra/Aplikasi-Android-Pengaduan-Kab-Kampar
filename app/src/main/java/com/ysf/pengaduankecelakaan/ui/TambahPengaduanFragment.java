package com.ysf.pengaduankecelakaan.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ysf.pengaduankecelakaan.R;

public class TambahPengaduanFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tambah_pengaduan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        setOnClickListener();
    }

    private void init() {

    }

    private void setOnClickListener() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){

        }
    }
}
