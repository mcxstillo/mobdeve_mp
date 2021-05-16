package com.mobdeve.castillo.recipe_finder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private ImageView imgProfile;
    private TextView emailProfile, nameProfile, descProfile;
    private Button viewBtn, editBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.emailProfile = findViewById(R.id.emailProfile);
        this.nameProfile = findViewById(R.id.nameProfile);
        this.viewBtn = findViewById(R.id.viewBtn);

        init();

        //if account is theirs, logout

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        userID = user.getUid();

        Log.d("INPROFILE","HELLO");
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String name = userProfile.name;
                    String email = userProfile.email;
                    String desc = userProfile.desc;

                    emailProfile.setText(email);
                    nameProfile.setText(name);
                    descProfile.setText(desc);

                    Log.d("email",email +"");
                    Log.d("name",name +"");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Sad", "User Profile Cannot be Displayed");

            }
        });

        this.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(profile.this, editprofile.class));
            }
        });

        this.viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(profile.this, CreateRecipe.class));
            }
        });

    }

    private void init() {
        this.imgProfile = findViewById(R.id.imgProfile);
        this.emailProfile = findViewById(R.id.emailProfile);
        this.nameProfile = findViewById(R.id.nameProfile);
        this.descProfile = findViewById(R.id.descProfile);
        this.viewBtn = findViewById(R.id.viewBtn);
        this.editBtn = findViewById(R.id.editBtn);
    }

    private void initFirebase() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        userID = user.getUid();
    }
}