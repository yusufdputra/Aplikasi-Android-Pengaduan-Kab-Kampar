package com.ysf.pengaduankecelakaan.ui;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.ysf.pengaduankecelakaan.R;
import com.ysf.pengaduankecelakaan.helper.Methods;
import com.ysf.pengaduankecelakaan.models.PengaduansModel;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ysf.pengaduankecelakaan.helper.KEYS.KEY_ADRESS;
import static com.ysf.pengaduankecelakaan.helper.KEYS.KEY_LATITUDE;
import static com.ysf.pengaduankecelakaan.helper.KEYS.KEY_LONGITUDE;
import static com.ysf.pengaduankecelakaan.helper.KEYS.KEY_NAMA;

public class FormPengaduanActivity extends AppCompatActivity implements View.OnClickListener {

    Methods methods = new Methods();
    private TextInputLayout til_pengaju, til_jalan, til_tempat, til_kordinat, til_keluhan, til_keramaian, til_rambu;
    private TextInputEditText et_pengaju, et_jalan, et_tempat, et_kordinat, et_keluhan, et_keramaian, et_rambu;
    private AppCompatSpinner spinner_rambu;
    private MaterialButton btn_lokasi1, btn_lokasi2, btn_ktp, btn_kirim;
    private ImageView iv_back, iv_lokasi1, iv_lokasi2, iv_ktp, iv_get_lokasi;
    private Uri filePath_ktp, filePath_lokasi1, filePath_lokasi2;
    private String [] list_spin_rambu = {
            "Sekolah",
            "Pasar"
    };

    private int req_lokasi1 = 1, req_lokasi2 = 2, req_ktp = 3, maps_req_code = 4;
    // 1 untuk lokasi 1
    // 2 untuk lokasi 2
    // 3 untuk lokasi ktp

    public String getAlamat;
    public GeoPoint getKordinat;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    private CollectionReference ref_pengaduan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.from_pengaduan);

        init();
        setOnClickListener();

        String get_nama = getIntent().getStringExtra(KEY_NAMA);
        et_keramaian.setText(get_nama);



    }


    private void init() {
        til_pengaju = findViewById(R.id.til_pengaju);
        til_jalan = findViewById(R.id.til_jalan);
        til_tempat = findViewById(R.id.til_tempat);
        til_kordinat = findViewById(R.id.til_kordinat);
        til_keluhan = findViewById(R.id.til_keluhan);
        til_keramaian = findViewById(R.id.til_keramaian);
        til_rambu = findViewById(R.id.til_rambu2);

        et_pengaju = findViewById(R.id.et_pengaju);
        et_jalan = findViewById(R.id.et_jalan);
        et_tempat = findViewById(R.id.et_tempat);
        et_kordinat = findViewById(R.id.et_kordinat);
        et_kordinat.setEnabled(false);
        et_keluhan = findViewById(R.id.et_keluhan);
        et_keramaian = findViewById(R.id.et_keramaian);
        et_keramaian.setEnabled(false);
//        et_rambu = findViewById(R.id.et_rambu);
        spinner_rambu = findViewById(R.id.spin_rambu);


        btn_lokasi1 = findViewById(R.id.btn_lokasi1);
        btn_lokasi2 = findViewById(R.id.btn_lokasi2);
        btn_ktp = findViewById(R.id.btn_ktp);
        btn_kirim = findViewById(R.id.btn_kirim);

        iv_back = findViewById(R.id.iv_back);
        iv_lokasi1 = findViewById(R.id.iv_lokasi1);
        iv_lokasi2 = findViewById(R.id.iv_lokasi2);
        iv_ktp = findViewById(R.id.iv_ktp);
        iv_get_lokasi = findViewById(R.id.iv_get_lokasi);

        mStorageRef = FirebaseStorage.getInstance().getReference(getResources().getString(R.string.ref_pengaduan));
        ref_pengaduan = FirebaseFirestore.getInstance().collection(getResources().getString(R.string.ref_pengaduan));
    }

    private void setOnClickListener() {
        btn_lokasi1.setOnClickListener(this);
        btn_lokasi2.setOnClickListener(this);
        btn_ktp.setOnClickListener(this);
        btn_kirim.setOnClickListener(this);
        iv_get_lokasi.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_back:
                onBackPressed();
                finish();
                break;
            case R.id.btn_lokasi1:
                selectImage(req_lokasi1);
                break;
            case R.id.btn_lokasi2:
                selectImage(req_lokasi2);
                break;
            case R.id.btn_ktp:
                selectImage(req_ktp);
                break;
            case R.id.iv_get_lokasi:
                Intent intent = new Intent(this, MapsActivity.class);
                startActivityForResult(intent, maps_req_code);
//                startActivity(intent);
                break;
            case R.id.btn_kirim:
                if (!methods.validasiInput(til_pengaju) || !methods.validasiInput(til_jalan) || !methods.validasiInput(til_tempat)
                        || !methods.validasiInput(til_kordinat) || !methods.validasiInput(til_keluhan)
                        || !methods.validasiGambar(v, filePath_ktp) || !methods.validasiGambar(v, filePath_lokasi1) || !methods.validasiGambar(v, filePath_lokasi2)) {
                    return;
                } else {
                    KirimPengaduan(v);
                }

                break;
        }
    }


    private void selectImage(int request) {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Silahkan pilih gambar"), request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK && data != null) {
            if ( requestCode == maps_req_code){
                getAlamat = data.getStringExtra(KEY_ADRESS);
                String latitude = data.getStringExtra(KEY_LATITUDE);
                String longitude = data.getStringExtra(KEY_LONGITUDE);

                double lat = Double.parseDouble(latitude);
                double longi = Double.parseDouble(longitude);


                getKordinat = new GeoPoint(lat, longi) ;

                Log.i("TAG", "onActivityResult: "+ getKordinat);
//                getKordinat = new GeoPoint((latitude), (longitude));
                et_kordinat.setText(latitude+", "+longitude);
                et_jalan.setText(getAlamat);

            }else if (requestCode == req_lokasi1){
                filePath_lokasi1 = data.getData();
                iv_lokasi1.setImageURI(filePath_lokasi1);
            } else  if (requestCode == req_lokasi2){
                filePath_lokasi2 = data.getData();
                iv_lokasi2.setImageURI(filePath_lokasi2);
            }else  if (requestCode == req_ktp){
                filePath_ktp = data.getData();
                iv_ktp.setImageURI(filePath_ktp);
            }

        }
    }


    private void KirimPengaduan(View v) {
        final ProgressDialog progressDialog = new ProgressDialog(v.getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        UploadGambar(progressDialog);
    }

    private void UploadGambar(ProgressDialog progressDialog) {
        try {
            HashMap<Integer, String> hashMap = new HashMap<>();

            StorageReference file_ref_lokasi1 = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(filePath_lokasi1));
            StorageReference file_ref_lokasi2 = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(filePath_lokasi2));
            StorageReference file_ref_ktp = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(filePath_lokasi2));

            mUploadTask = file_ref_lokasi1.putFile(filePath_lokasi1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    hashMap.put(0, taskSnapshot.getStorage().getName());
                }
            });
            mUploadTask = file_ref_lokasi2.putFile(filePath_lokasi2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    hashMap.put(1, taskSnapshot.getStorage().getName());
                }
            });
            mUploadTask = file_ref_ktp.putFile(filePath_ktp).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    hashMap.put(2, taskSnapshot.getStorage().getName());

                    PengaduansModel values = new PengaduansModel(
                            et_pengaju.getText().toString(),
                            getAlamat,
                            et_keramaian.getText().toString(),
                            et_keluhan.getText().toString(),
                            hashMap.get(0),
                            hashMap.get(1),
                            hashMap.get(2),
                            getKordinat,
                            Timestamp.now(),
                            spinner_rambu.getSelectedItem().toString()
                    );

                    ref_pengaduan.add(values)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(getApplicationContext(), "Terimakasih. Pengaduan anda akan kami respon.", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                    onBackPressed();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Maaf. Pengaduan anda gagal terkirim.", Toast.LENGTH_LONG).show();
                            Log.i("TAG", "onFailure: "+e);
                            progressDialog.dismiss();
                        }
                    });

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressDialog.setProgress((int) progress);
                }
            });
        }catch (Exception e){
            progressDialog.cancel();
            Log.i("TAG", "onFailure: "+e);
            Toast.makeText(getApplicationContext(), "Maaf. Pengaduan anda gagal terkirim.", Toast.LENGTH_LONG).show();
        }
    }

    private String getFileExtension(Uri filepath) {
        ContentResolver cR = this.getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(filepath));
    }

}
