package com.mobdeve.castillo.recipe_finder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RecipeBook extends AppCompatActivity {

    //LIKED RECIPES/LANDING PAGE?
    //SEARCH PAGE
    private ArrayList<Recipe> recipes;
    private RecyclerView recipesRv;
    private ResultsAdapter adapter;
    private FloatingActionButton createBtn;
    private Button profile;
    private TextView notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_book);

        init();

        this.createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent create = new Intent(RecipeBook.this, CreateRecipe.class);
                create.putExtra("TYPE", "CREATE");
                startActivity(create);
            }
        });

        this.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecipeBook.this, profile.class));
            }
        });

        if (recipes == null) {
            recipesRv.setVisibility(View.GONE);
            notice.setVisibility(View.VISIBLE);
            profile.setVisibility(View.VISIBLE);
        } else {
            LinearLayoutManager lm = new LinearLayoutManager(RecipeBook.this);
            this.recipesRv.setLayoutManager(lm);
            this.adapter = new ResultsAdapter(this.recipes);
            this.recipesRv.setAdapter(this.adapter);
        }
    }

    private void init() {
        this.recipesRv = (RecyclerView) findViewById(R.id.recipesRv);
        this.createBtn = findViewById(R.id.createBtn);
        this.notice = findViewById(R.id.noticeTv);
        this.profile = findViewById(R.id.profileBtn);
    }
}