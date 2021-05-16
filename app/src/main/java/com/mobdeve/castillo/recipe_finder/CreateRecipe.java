package com.mobdeve.castillo.recipe_finder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class CreateRecipe extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private EditText name, preptime, cooktime, desc;
    private Spinner cuisine, size;
    ArrayAdapter<CharSequence> cuisine_adapter, size_adapter;
    private String selected_cuisine, selected_size; // hi cams use this string to get values ng cuisine and size since dito ko inassign yung values for dropdown
    private Button nextBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);
        
        init();

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        userID = user.getUid();
        Recipe recipe = new Recipe();


        this.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DatabaseReference DB = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        recipe.setName(name.getText().toString());
                        recipe.setCuisine(selected_cuisine);
                        recipe.setServing_size(selected_size);
                        recipe.setPreptime(preptime.getText().toString());
                        recipe.setCookingtime(cooktime.getText().toString());
                        recipe.setDesc(desc.getText().toString());
                        User userProfile = snapshot.getValue(User.class);
                        DB.child("Recipes").child(Objects.requireNonNull(DB.push().getKey())).setValue(recipe);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("Sad", "User Profile Cannot be Displayed");

                    }
                });
            }
        });


        //SET VALUE within user

//        FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .setValue(user)


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selected = parent.getItemAtPosition(position).toString();
        switch (parent.getId()) {
            case R.id.create_cuisineEt: this.selected_cuisine = selected; break;
            case R.id.create_sizeEt: this.selected_size = selected; break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void init() {
        this.name = findViewById(R.id.create_nameEt);
        this.cuisine = (Spinner) findViewById(R.id.create_cuisineEt);
        this.size = (Spinner) findViewById(R.id.create_sizeEt);
        this.preptime = findViewById(R.id.create_prepEt);
        this.cooktime = findViewById(R.id.create_cookEt);
        this.desc = findViewById(R.id.create_descEt);
        this.nextBtn = findViewById(R.id.create_nextBtn);

        cuisine_adapter = ArrayAdapter.createFromResource(this,R.array.cuisine_array,R.layout.support_simple_spinner_dropdown_item);
        size_adapter = ArrayAdapter.createFromResource(this, R.array.size_array,R.layout.support_simple_spinner_dropdown_item);
        cuisine_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        size_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        cuisine.setAdapter(cuisine_adapter);
        size.setAdapter(size_adapter);

        cuisine.setOnItemSelectedListener(this);
        size.setOnItemSelectedListener(this);
    }
}