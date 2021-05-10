package com.ysf.pengaduankecelakaan.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ysf.pengaduankecelakaan.MainActivity;
import com.ysf.pengaduankecelakaan.R;
import com.ysf.pengaduankecelakaan.models.PengaduansModel;
import com.ysf.pengaduankecelakaan.ui.dataPengajuan.FragmentList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataPengaduanActivity extends AppCompatActivity {

    private ImageView iv_logo;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener listener;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_pengaduan);

        auth =  FirebaseAuth.getInstance();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_data, new FragmentList());
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();

        iv_logo = findViewById(R.id.id_logo_dinas);
        iv_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alerBuilder = new AlertDialog.Builder(DataPengaduanActivity.this);
                alerBuilder.setMessage("Ingin keluar?");
                alerBuilder.setPositiveButton("Yes", (dialogInterface, i) -> {
                    auth.signOut();
                    onBackPressed();
                    finish();
                });

                alerBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alerBuilder.show();
            }
        });


    }








}
