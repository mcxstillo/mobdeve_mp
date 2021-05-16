package com.mobdeve.castillo.recipe_finder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;

public class RecipeBook extends AppCompatActivity {

    private ArrayList<Recipe> recipes;
    private RecyclerView recipesRv;
    private ResultsAdapter adapter;
    private Button createBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_book);

        init();

        LinearLayoutManager lm = new LinearLayoutManager(RecipeBook.this);
        this.recipesRv.setLayoutManager(lm);
        this.adapter = new ResultsAdapter(this.recipes);
        this.recipesRv.setAdapter(this.adapter);
    }

    private void init() {
        this.recipesRv = (RecyclerView) findViewById(R.id.recipesRv);
        this.createBtn = findViewById(R.id.createBtn);
    }
}