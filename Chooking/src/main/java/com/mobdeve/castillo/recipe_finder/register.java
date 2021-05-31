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
import com.google.firebase.database.FirebaseDatabase;

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

//                                  FirebaseUser user = mAuth.getCurrentUser();
                                User user = new User(reg_emailEt.getText().toString().trim(),reg_usernameEt.getText().toString().trim(),null, FirebaseAuth.getInstance().getCurrentUser().getUid());
//                                updateUI(user);

                                FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()){
                                            updateUI(user);
                                        }
                                        else{
                                            Log.d("aw","not successful");
                                        }
                                    }
                                });

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(register.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        });

        this.link_loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLogin = new Intent(register.this, login.class);
                startActivity(toLogin);
            }
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

    public void updateUI(User currentUser) {
        Intent Login = new Intent(getApplicationContext(), login.class);
        startActivity(Login);
    }
}