package com.mobdeve.castillo.recipe_finder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class editprofile extends AppCompatActivity {

    public EditText profile_nameEt, descEt, oldpass, newpass, confirmpass;
    public Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        init();
    }

    private void init() {
        this.profile_nameEt = findViewById(R.id.edit_nameEt);
        this.descEt = findViewById(R.id.edit_descEt);
        this.updateBtn = findViewById(R.id.profile_updateBtn);
        this.oldpass = findViewById(R.id.edit_oldpassEt);
        this.newpass = findViewById(R.id.edit_newpassEt);
        this.confirmpass = findViewById(R.id.edit_confpassEt);
    }
}