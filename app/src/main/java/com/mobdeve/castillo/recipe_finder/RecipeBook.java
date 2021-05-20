package com.mobdeve.castillo.recipe_finder;

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
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecipeBook extends AppCompatActivity {

    //LIKED RECIPES/LANDING PAGE?
    //SEARCH PAGE
    DrawerLayout navbar;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private ArrayList<Recipe> recipes;
    private RecyclerView recipesRv;
    private ResultsAdapter adapter;
    private FloatingActionButton createBtn;
    private ResultsAdapter.RecyclerViewClickListener listener;
    private TextView notice;
    private TextView navUsernameTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_book);
        reference = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        init();

        reference.child("Liked").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Recipe recipeItem = dataSnapshot.getValue(Recipe.class);
                    Log.d("recipeboookitem",recipeItem.recipeID);
                    dataSnapshot.getValue(Recipe.class);
                    recipes.add(recipeItem);

                }
                if (!(recipes.isEmpty())) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userID = snapshot.getValue(User.class);
                navUsernameTv.setText(userID.name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (recipes == null || recipes.isEmpty()) {
            recipesRv.setVisibility(View.GONE);
            notice.setVisibility(View.VISIBLE);
        } else {
            LinearLayoutManager lm = new LinearLayoutManager(RecipeBook.this);
            this.recipesRv.setLayoutManager(lm);
            this.adapter = new ResultsAdapter(this.recipes, listener);
            this.recipesRv.setAdapter(this.adapter);
        }
    }

    private void init() {
        this.navbar = findViewById(R.id.navdrawer);
        this.recipes = new ArrayList<Recipe>();
        this.recipesRv = (RecyclerView) findViewById(R.id.recipesRv);
        this.createBtn = findViewById(R.id.createBtn);
        this.notice = findViewById(R.id.noticeTv);
        this.navUsernameTv = findViewById(R.id.navUsernameTv);
        setOnClickListener();
    }

    private void setOnClickListener() {
        this.listener = new ResultsAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent viewRecipe = new Intent(RecipeBook.this, RecipePage.class);
                String recipeID = recipes.get(position).getRecipeID();
                Log.d("RecipeID",recipeID);
                viewRecipe.putExtra("recipeID",recipeID);
//                viewRecipe.putExtra("position",position);
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

    public void ClickLogo(View view) {
        closeDrawer(navbar);
    }

    public static void closeDrawer(DrawerLayout drawer) {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickProfile (View view){
        startActivity(new Intent(RecipeBook.this, profile.class));
    }

    public void ClickRecipebook (View view){
        Toast error = Toast.makeText(getApplicationContext(), "You are currently here.", Toast.LENGTH_SHORT);
        error.show();
    }

    public void ClickMyRecipes (View view){
        Intent type = new Intent(RecipeBook.this, results.class);
        type.putExtra("TYPE", "MY_RECIPES");
        startActivity(type);
    }

    public void ClickLogout (View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Logout user form firebase in this function and redirect to MainActivity
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(RecipeBook.this,MainActivity.class));
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