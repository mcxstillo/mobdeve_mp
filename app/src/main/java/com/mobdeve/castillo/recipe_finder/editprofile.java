package com.mobdeve.castillo.recipe_finder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class editprofile extends AppCompatActivity {

    DrawerLayout navbar;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    public EditText profile_nameEt, descEt, oldpass, newpass, confirmpass;
    public Button updateBtn;
    User userEdited = new User();
    private TextView navUsernameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        init();

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        userID = user.getUid();

        //get current data and display on fields.
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                profile_nameEt.setText(userProfile.name);
                descEt.setText(userProfile.desc);
                navUsernameTv.setText(userProfile.name);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Sad", "User Profile Cannot be Displayed");

            }
        });

        //when user presses update button
        this.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //error here, updates entire user model, not just specific fields
//                userEdited = new User(user.getEmail(),profile_nameEt.getText().toString(),descEt.getText().toString());

                reference.child(userID).child("name").setValue(profile_nameEt.getText().toString());
                reference.child(userID).child("desc").setValue(descEt.getText().toString());

//                        setName(profile_nameEt.getText().toString());
//                userEdited.setDesc(descEt.getText().toString());
//                reference.child(userID).setValue(userEdited);
                startActivity(new Intent(editprofile.this,profile.class));
            }
        });
    }

    private void init() {
        this.navbar = findViewById(R.id.navdrawer);
        this.profile_nameEt = findViewById(R.id.edit_nameEt);
        this.descEt = findViewById(R.id.edit_descEt);
        this.updateBtn = findViewById(R.id.profile_updateBtn);
        this.oldpass = findViewById(R.id.edit_oldpassEt);
        this.newpass = findViewById(R.id.edit_newpassEt);
        this.confirmpass = findViewById(R.id.edit_confpassEt);
        this.navUsernameTv = findViewById(R.id.navUsernameTv);
    }

    // NAVBAR FUNCTIONS
    public void ClickMenu(View view) {
        openDrawer(navbar);
    }

    public static void openDrawer (DrawerLayout drawer) {
        drawer.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawer) {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickProfile (View view){
        startActivity(new Intent(editprofile.this, profile.class));
    }

    public void ClickRecipebook (View view){
        startActivity(new Intent(editprofile.this, RecipeBook.class));
    }

    public void ClickMyRecipes (View view){
        Intent type = new Intent(editprofile.this, results.class);
        type.putExtra("TYPE", "MY_RECIPES");
        startActivity(type);
    }

    public void ClickLogout(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Logout user form firebase in this function and redirect to MainActivity
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(editprofile.this,MainActivity.class));
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }
}