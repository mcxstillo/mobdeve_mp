package com.mobdeve.castillo.recipe_finder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class register extends AppCompatActivity {

    private ImageView logo;
    public EditText reg_usernameEt;
    public EditText reg_emailEt;
    public EditText reg_passwordEt;
    public EditText reg_confpassEt;
    public Button registerBtn;
    public TextView link_loginTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
    }

    private void init(){
        this.logo = findViewById(R.id.reg_logo);
        logo.setImageResource(R.drawable.chef);
        this.reg_usernameEt = findViewById(R.id.nameEt);
        this.reg_emailEt = findViewById(R.id.emailEt);
        this.reg_passwordEt = findViewById(R.id.reg_passwordEt);
        this.reg_confpassEt = findViewById(R.id.confirm_passEt);
        this.registerBtn = findViewById(R.id.registerBtn);
        this.link_loginTv = findViewById(R.id.reg_loginTv);
    }
}