package com.mobdeve.castillo.recipe_finder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;

public class results extends AppCompatActivity {

    private ImageView logo;
    private RecyclerView resultsRv;
    private ResultsAdapter adapter;
    private ArrayList<Recipe> recipes;
    public ImageView sidebar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        init();

        LinearLayoutManager lm = new LinearLayoutManager(results.this);
        this.resultsRv.setLayoutManager(lm);
        this.adapter = new ResultsAdapter(this.recipes);
        this.resultsRv.setAdapter(this.adapter);
    }

    private void init() {
        this.logo = findViewById(R.id.results_logo);
        logo.setImageResource(R.drawable.chef);
        this.sidebar = findViewById(R.id.sidebarIv);
        this.resultsRv = findViewById(R.id.resultsRv);
    }
}