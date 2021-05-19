package com.mobdeve.castillo.recipe_finder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class results extends AppCompatActivity {

    //POSTED RECIPES/SEARCH RESULTS
    DrawerLayout navbar;
    private RecyclerView resultsRv;
    private ResultsAdapter adapter;
    private FloatingActionButton createBtn;
    private ResultsAdapter.RecyclerViewClickListener listener;
    private TextView noticeTv;
    ArrayList<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        init();

        this.createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent create = new Intent(results.this, CreateRecipe.class);
                create.putExtra("TYPE", "CREATE");
                startActivity(create);
            }
        });

        if(recipes == null) {
            noticeTv.setVisibility(View.VISIBLE);
            resultsRv.setVisibility(View.GONE);
        } else {
            noticeTv.setVisibility(View.GONE);
            LinearLayoutManager lm = new LinearLayoutManager(results.this);
            resultsRv.setLayoutManager(lm);
            adapter = new ResultsAdapter(recipes, listener);
            resultsRv.setAdapter(adapter);
        }

        DatabaseReference DB = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        DB.child("Recipes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recipes.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    //a recipe within a snapshot
                    Recipe recipeItem = dataSnapshot.getValue(Recipe.class);
                    recipes.add(recipeItem);

                    Log.d("recipeItem",recipeItem.getName()+"");
                    Log.d("recipesize",recipes.size()+"");

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        }

    private void init() {
        this.navbar = findViewById(R.id.navdrawer);
        this.resultsRv = findViewById(R.id.resultsRv);
        this.recipes = new ArrayList<Recipe>();
        this.createBtn = findViewById(R.id.createBtn);
        this.noticeTv = findViewById(R.id.recipes_noticeTv);
        setOnClickListener();
    }

    private void setOnClickListener() {
        this.listener = new ResultsAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent viewRecipe = new Intent(results.this, RecipePage.class);
                /*
                    HELLO CAMS AM RIGHT HERE PUT DB CODE HERE HEHE
                */
                startActivity(viewRecipe);
            }
        };
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
        startActivity(new Intent(results.this, profile.class));
    }

    public void ClickRecipebook (View view){
        startActivity(new Intent(results.this, RecipeBook.class));
    }

    public void ClickMyRecipes (View view){
        Toast error = Toast.makeText(getApplicationContext(), "You are currently here.", Toast.LENGTH_SHORT);
        error.show();
    }

    public void ClickLogout(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Logout user form firebase in this function and redirect to MainActivity
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