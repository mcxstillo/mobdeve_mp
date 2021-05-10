package com.mobdeve.castillo.recipe_finder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageView logo;
    public Button toLogin;
    public Button toRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        this.logo = findViewById(R.id.main_logo);
        logo.setImageResource(R.drawable.logo);
        this.toLogin = findViewById(R.id.toLoginBtn);
        this.toRegister = findViewById(R.id.toRegisterBtn);
    }
}