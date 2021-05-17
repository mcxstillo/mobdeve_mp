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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RecipeBook extends AppCompatActivity {

    //LIKED RECIPES/LANDING PAGE?
    //SEARCH PAGE
    DrawerLayout navbar;
    private ArrayList<Recipe> recipes;
    private RecyclerView recipesRv;
    private ResultsAdapter adapter;
    private FloatingActionButton createBtn;
    private TextView notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_book);

        init();

        if (recipes == null) {
            recipesRv.setVisibility(View.GONE);
            notice.setVisibility(View.VISIBLE);
        } else {
            LinearLayoutManager lm = new LinearLayoutManager(RecipeBook.this);
            this.recipesRv.setLayoutManager(lm);
            this.adapter = new ResultsAdapter(this.recipes);
            this.recipesRv.setAdapter(this.adapter);
        }
    }

    private void init() {
        this.navbar = findViewById(R.id.navdrawer);
        this.recipesRv = (RecyclerView) findViewById(R.id.recipesRv);
        this.createBtn = findViewById(R.id.createBtn);
        this.notice = findViewById(R.id.noticeTv);
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