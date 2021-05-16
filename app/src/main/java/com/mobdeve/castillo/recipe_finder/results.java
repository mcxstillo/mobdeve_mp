package com.mobdeve.castillo.recipe_finder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class results extends AppCompatActivity {

    private ImageView logo;
    private RecyclerView resultsRv;
    private ResultsAdapter adapter;
    public ImageView sidebar;
    ArrayList<Recipe> recipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        init();

        LinearLayoutManager lm = new LinearLayoutManager(results.this);
        resultsRv.setLayoutManager(lm);
        adapter = new ResultsAdapter(recipes);
        resultsRv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        DatabaseReference DB = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        DB.child("Recipes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Recipe recipeItem = dataSnapshot.getValue(Recipe.class);
                    Log.d("recipeItemName",""+recipeItem.getName());
                    dataSnapshot.getValue(Recipe.class);
                    Log.d("dataSnapshot",""+dataSnapshot.getValue(Recipe.class).getName());

                    recipes.add(recipeItem);
                    Log.d("ArrayContent",""+recipes.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void init() {
        this.logo = findViewById(R.id.results_logo);
        logo.setImageResource(R.drawable.chef);
        this.sidebar = findViewById(R.id.sidebarIv);
        this.resultsRv = findViewById(R.id.resultsRv);
    }
}