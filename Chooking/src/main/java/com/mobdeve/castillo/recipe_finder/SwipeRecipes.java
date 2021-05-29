package com.mobdeve.castillo.recipe_finder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SwipeRecipes extends AppCompatActivity {

    DrawerLayout navbar;
    private ArrayList<Recipe> recipes;
    private SwipeAdapter adapter;
    private RecyclerView recipeRv;
    private TextView navUsernameTv, noticeTv;
    private FloatingActionButton createBtn;
    private SwipeAdapter.RecyclerViewClickListener listener;

    DatabaseReference DBSearch = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
    DatabaseReference DB = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_recipes);

        init();

        DB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userID = snapshot.getValue(User.class);
                navUsernameTv.setText(userID.name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DB.child("Recipes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Recipe recipeItem = dataSnapshot.getValue(Recipe.class);
                    dataSnapshot.getValue(Recipe.class);
                    recipes.add(recipeItem);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        this.createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent create = new Intent(SwipeRecipes.this, CreateRecipe.class);
                create.putExtra("TYPE", "CREATE");
                startActivity(create);
            }
        });

        if(recipes == null) {
            noticeTv.setVisibility(View.VISIBLE);
            recipeRv.setVisibility(View.GONE);
        } else {
            noticeTv.setVisibility(View.GONE);
            LinearLayoutManager lm = new LinearLayoutManager(SwipeRecipes.this);
            recipeRv.setLayoutManager(lm);
            adapter = new SwipeAdapter(recipes, listener);
            recipeRv.setAdapter(adapter);
        }
    }

    private void init() {
        this.noticeTv = findViewById(R.id.swipe_noticeTv);
        this.navbar = findViewById(R.id.navdrawer);
        this.navUsernameTv = findViewById(R.id.navUsernameTv);
        this.recipes = new ArrayList<Recipe>();
        this.recipeRv = (RecyclerView) findViewById(R.id.swipeRecipesRv);
        this.createBtn = findViewById(R.id.swipe_createRecBtn);
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
        startActivity(new Intent(SwipeRecipes.this, Profile.class));
    }

    public void ClickRecipebook (View view){
        startActivity(new Intent(SwipeRecipes.this, RecipeBook.class));
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
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SwipeRecipes.this,MainActivity.class));
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