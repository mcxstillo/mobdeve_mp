package com.mobdeve.castillo.recipe_finder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class login extends AppCompatActivity {

    private ImageView logo;
    public EditText username;
    public EditText password;
    public Button loginBtn;
    public TextView link_signupTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        this.logo = findViewById(R.id.login_logo);
        this.logo.setImageResource(R.drawable.logo);
        this.username = findViewById(R.id.usernameEt);
        this.password = findViewById(R.id.passwordEt);
        this.loginBtn = findViewById(R.id.loginBtn);
        this.link_signupTv = findViewById(R.id.login_regTv);
    }
}