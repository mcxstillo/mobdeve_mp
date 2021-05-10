package com.mobdeve.castillo.recipe_finder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

public class results extends AppCompatActivity {

    private ImageView logo;
    public ImageView sidebar;
    public RecyclerView results_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
    }

    private void init() {
        this.logo = findViewById(R.id.results_logo);
        logo.setImageResource(R.drawable.chef);
        this.sidebar = findViewById(R.id.sidebarIv);
        this.results_list = findViewById(R.id.resultsRv);
    }
}