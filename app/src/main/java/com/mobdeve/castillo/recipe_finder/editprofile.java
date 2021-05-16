package com.mobdeve.castillo.recipe_finder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class editprofile extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    public EditText profile_nameEt, descEt, oldpass, newpass, confirmpass;
    public Button updateBtn;
    User userEdited = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        init();

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        userID = user.getUid();

        //get current data and display
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                profile_nameEt.setText(userProfile.name);
                descEt.setText(userProfile.desc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Sad", "User Profile Cannot be Displayed");

            }
        });

        this.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEdited = new User(user.getEmail(),profile_nameEt.getText().toString(),descEt.getText().toString());
                reference.child(userID).setValue(userEdited);
                startActivity(new Intent(editprofile.this,profile.class));
            }
        });
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