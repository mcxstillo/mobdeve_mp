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
import com.google.firebase.auth.FirebaseUser;

public class register extends AppCompatActivity {

    private FirebaseAuth mAuth;

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

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(v -> {
            Log.d("onClick", reg_emailEt.getText().toString().trim());


            mAuth.createUserWithEmailAndPassword(reg_emailEt.getText().toString().trim(), reg_passwordEt.getText().toString().trim())
                    .addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "createUserWithEmail:success");

                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(register.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        });
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

    public void updateUI(FirebaseUser currentUser) {
        Intent Login = new Intent(getApplicationContext(), login.class);
        Login.putExtra("email", currentUser.getEmail());
        Log.v("DATA", currentUser.getUid());
        startActivity(Login);
    }
}