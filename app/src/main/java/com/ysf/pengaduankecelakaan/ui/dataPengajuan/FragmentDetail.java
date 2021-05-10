package com.ysf.pengaduankecelakaan.ui.dataPengajuan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ysf.pengaduankecelakaan.R;
import com.ysf.pengaduankecelakaan.helper.KEYS;
import com.ysf.pengaduankecelakaan.helper.Methods;
import com.ysf.pengaduankecelakaan.ui.DataPengaduanActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FragmentDetail extends Fragment implements View.OnClickListener {

    private ExtendedFloatingActionButton fab_back;
    private CollectionReference ref_pengaduan;
    private StorageReference mStorageRef;

    private TextView tv_nama_pengaju, tv_waktu_pengajuan, tv_kebutuhan, tv_tempat, tv_keluhan, tv_nama_jalan;
    private ImageView iv_lokasi1, iv_lokasi2, iv_ktp;
    private MaterialButton btn_tuju, btn_tolak;
    Methods methods = new Methods();
    private GeoPoint getGeopoint;
    private String id_pengajuan, path_lokasi1, path_lokasi2, path_ktp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_pengajuan, container, false);

        init(view);
        setOnClickListener();
        if (getArguments() != null) {
            id_pengajuan = getArguments().getString(KEYS.KEY_ID);
            getDetail(id_pengajuan);
        }


        return view;
    }

    private void getDetail(String id) {
        ref_pengaduan.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    DocumentSnapshot doc = task.getResult();

                    path_lokasi1 = doc.get("path_lokasi1").toString();
                    path_lokasi2 = doc.get("path_lokasi2").toString();
                    path_ktp = doc.get("path_ktp").toString();

                    tv_nama_pengaju.setText(doc.get("nama_pengaju").toString());
                    tv_kebutuhan.setText(doc.get("kebutuhan_rambu").toString());
                    tv_tempat.setText(doc.get("nama_tempat").toString());
                    tv_keluhan.setText(doc.get("keluhan").toString());
                    tv_nama_jalan.setText(doc.get("nama_jalan").toString());

                    Date timestamp = doc.getTimestamp("waktu_pengajuan").toDate();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("h:m a / d-MM-y");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(timestamp);
                    tv_waktu_pengajuan.setText(dateFormat.format(calendar.getTime()));

                    methods.setImg(mStorageRef, iv_lokasi1, path_lokasi1);
                    methods.setImg(mStorageRef, iv_lokasi2, path_lokasi2);
                    methods.setImg(mStorageRef, iv_ktp, path_ktp);

                    getGeopoint = doc.getGeoPoint("kordinat");

                }
            }
        });
    }

    private void init(View view) {
        fab_back = view.findViewById(R.id.fab_back);
        tv_nama_pengaju = view.findViewById(R.id.tv_nama_pengaju);
        tv_waktu_pengajuan = view.findViewById(R.id.tv_waktu);
        tv_kebutuhan = view.findViewById(R.id.tv_rambu);
        tv_tempat = view.findViewById(R.id.tv_nama_tempat);
        tv_keluhan = view.findViewById(R.id.tv_keluhan);
        tv_nama_jalan = view.findViewById(R.id.tv_nama_jalan);

        iv_lokasi1 = view.findViewById(R.id.iv_lokasi1);
        iv_lokasi2 = view.findViewById(R.id.iv_lokasi2);
        iv_ktp = view.findViewById(R.id.iv_ktp);

        btn_tuju = view.findViewById(R.id.btn_tuju_lokasi);
        btn_tolak = view.findViewById(R.id.btn_tuju_tolak);

        mStorageRef = FirebaseStorage.getInstance().getReference(getResources().getString(R.string.ref_pengaduan));
        ref_pengaduan = FirebaseFirestore.getInstance().collection(getResources().getString(R.string.ref_pengaduan));

    }

    private void setOnClickListener() {
        fab_back.setOnClickListener(this);
        btn_tuju.setOnClickListener(this);
        btn_tolak.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_tuju_lokasi:
//                String uri = String.format(Locale.ENGLISH, String.format("geo:%f,%f", getGeopoint.getLatitude(), getGeopoint.getLongitude()));
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                getContext().startActivity(intent);
                String uri = "http://maps.google.com/maps?q=loc:" + getGeopoint.getLatitude() + "," + getGeopoint.getLongitude();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
                break;
            case R.id.btn_tuju_tolak:
                AlertDialog.Builder alerBuilder = new AlertDialog.Builder(getContext());
                alerBuilder.setMessage("Apakah anda ingin menghapus pengajuan ini?");
                alerBuilder.setPositiveButton("Hapus", (dialogInterface, i) -> {
                    ref_pengaduan.document(id_pengajuan)
                            .delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mStorageRef.child(path_lokasi1).delete();
                                    mStorageRef.child(path_lokasi2).delete();
                                    mStorageRef.child(path_ktp).delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getContext(), "Berhasil menghapus pengajuan",Toast.LENGTH_LONG).show();
                                            getActivity().onBackPressed();
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Terjadi Kesalahan",Toast.LENGTH_LONG).show();
                        }
                    });
                });

                alerBuilder.setNegativeButton("Batalkan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alerBuilder.show();
                break;

        }
    }
}
