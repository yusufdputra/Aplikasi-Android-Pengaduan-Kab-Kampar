package com.ysf.pengaduankecelakaan.helper;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ysf.pengaduankecelakaan.R;

public class Methods {

    public Methods() {
    }

    public boolean validasiInput(TextInputLayout error) {
        String email = error.getEditText().getText().toString().trim();
        if (email.isEmpty()) {
            error.setError("Field tidak boleh kosong");
            return false;
        }else {
            error.setError(null);
            return true;
        }
    }

    public boolean validasiGambar(View v, Uri filepath_profile) {
        if (filepath_profile == null) {
            Snackbar snackbar = Snackbar
                    .make(v, "Silahkan pilih foto terlebih dahulu!", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        } else {
            return true;
        }
    }

    public void setImg(StorageReference stRef, ImageView iv_foto, String foto_path){
        //set image to layout
        stRef.child(String.valueOf(foto_path)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fetch(new Callback() {
                    @Override
                    public void onSuccess() {

                        iv_foto.setAlpha(0f);
                        Picasso.get()
                                .load(uri)
                                .fit().centerInside()
                                .into(iv_foto);
                        iv_foto.animate().setDuration(10).alpha(1f).start();
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        });
    }
}
