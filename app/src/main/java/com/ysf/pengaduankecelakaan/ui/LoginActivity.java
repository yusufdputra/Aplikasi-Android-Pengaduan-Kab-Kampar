package com.ysf.pengaduankecelakaan.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.ysf.pengaduankecelakaan.R;
import com.ysf.pengaduankecelakaan.helper.KEYS;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    // "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 4 characters
                    "$");
    private TextInputEditText etEmail, etPassword;
    private ImageView iv_back;
    private Button btnLogin;
    private TextView tvSignup, tv_msg, tvforgot_password;
    private TextInputLayout error_email, error_password;
    //private View tv_msg;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener listener;
    private CollectionReference ref_users;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        setOnClickListener();


    }

    private void setOnClickListener() {
        iv_back.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    private void init() {
        iv_back = findViewById(R.id.iv_back);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tv_msg = findViewById(R.id.tv_msg);
        progressBar = findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

        error_email = findViewById(R.id.error_email);
        error_password = findViewById(R.id.error_password);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;
        switch (id) {
            case R.id.btn_login:
                doLogin();
                break;

            case R.id.iv_back:
                this.onBackPressed();
                this.finish();
                break;
        }
    }

    private void doLogin() {
        progressBar.setVisibility(View.VISIBLE);

        if (!validasiEmail() || !validasiPassword()) {
            progressBar.setVisibility(View.GONE);
            return;
        } else {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();


            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                progressBar.setVisibility(View.GONE);
                                finish();

                            } else {
                                progressBar.setVisibility(View.GONE);
                                tv_msg.setVisibility(View.VISIBLE);
                                auth.signOut();
                                tv_msg.setText(R.string.msg_salah);
                            }
                        }
                    })
                    .addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {
                            progressBar.setVisibility(View.GONE);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });


        }
    }


    private boolean validasiEmail() {
        String email = error_email.getEditText().getText().toString().trim();
        if (email.isEmpty()) {
            error_email.setError("Field tidak boleh kosong");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            error_email.setError("Masukkan alamat email yang benar");
            return false;
        } else {
            error_email.setError(null);
            return true;
        }
    }

    private boolean validasiPassword() {
        String pw = error_password.getEditText().getText().toString().trim();
        if (pw.isEmpty()) {
            error_password.setError("Field tidak boleh kosong");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(pw).matches()) {
            error_password.setError("Password lemah. Harus terdiri dari 1 [0-9] dan [a-zA-Z]. Minimal 4 karakter.");
            return false;
        } else if (pw.length() < 6) {
            error_password.setError("Minimal 4 karakter.");
            return false;
        } else {
            error_password.setError(null);
            return true;
        }
    }

}
