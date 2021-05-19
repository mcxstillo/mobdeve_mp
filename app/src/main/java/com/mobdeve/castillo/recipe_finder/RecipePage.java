package com.mobdeve.castillo.recipe_finder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RecipePage extends AppCompatActivity {

    DrawerLayout navbar;
    private ImageView photo;
    private TextView nameTv, creatorTv, cuisineTv, servingsTv, preptimeTv, cooktimeTv, descTv;
    private RecyclerView stepsRv;
    private StepsAdapter adapter;
    private FloatingActionButton faveBtn;
    private ArrayList<Steps> steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_page);

        init();

        // insert db read here yey

        faveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecipePage.this, "Saved to Recipe Book", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        this.navbar = findViewById(R.id.navdrawer);
        this.photo = (ImageView) findViewById(R.id.recipe_img);
        this.nameTv = findViewById(R.id.recipe_nameTv);
        this.creatorTv = findViewById(R.id.recipe_opTv);
        this.cuisineTv = findViewById(R.id.recipe_cuisineTv);
        this.servingsTv = findViewById(R.id.recipe_servingTv);
        this.preptimeTv = findViewById(R.id.recipe_prepTv);
        this.cooktimeTv = findViewById(R.id.recipe_cookTv);
        this.descTv = findViewById(R.id.recipe_descTv);
        this.stepsRv = (RecyclerView) findViewById(R.id.stepsRv);
        this.faveBtn = findViewById(R.id.faveBtn);
        this.steps = new ArrayList<Steps>();
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
        startActivity(new Intent(RecipePage.this, profile.class));
    }

    public void ClickRecipebook (View view){
        startActivity(new Intent(RecipePage.this, RecipeBook.class));
    }

    public void ClickMyRecipes (View view){
        Intent type = new Intent(RecipePage.this, results.class);
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