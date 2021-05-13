package com.mobdeve.castillo.recipe_finder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ImageView logo;
    public EditText email;
    public EditText password;
    public Button loginBtn;
    public TextView link_signupTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        mAuth = FirebaseAuth.getInstance();

        this.link_signupTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toR = new Intent(login.this, register.class);
                startActivity(toR);
            }
        });

        this.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signInWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Log.d("LOGIN","successful!!!!!!");
                                    startActivity(new Intent(login.this, profile.class));
                                }else{
                                    Toast.makeText(login.this, "Login failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void init() {
        this.logo = findViewById(R.id.login_logo);
        this.logo.setImageResource(R.drawable.logo);
        this.email = findViewById(R.id.emailLoginEt);
        this.password = findViewById(R.id.passwordEt);
        this.loginBtn = findViewById(R.id.loginBtn);
        this.link_signupTv = findViewById(R.id.login_regTv);
    }
}