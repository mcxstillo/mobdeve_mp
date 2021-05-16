package com.mobdeve.castillo.recipe_finder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder>{

    ArrayList<Recipe> recipes;

    public RecipesAdapter (ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, rating, difficulty, preptime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.name = itemView.findViewById(R.id.recipe_nameTv);
            this.rating = itemView.findViewById(R.id.ratingTv);
            this.difficulty = itemView.findViewById(R.id.difficultyTv);
            this.preptime = itemView.findViewById(R.id.timeTv);
        }

        public void setName(String name) {
            this.name.setText(name);
        }
        public void setRating(Float rating) {
            this.rating.setText(String.valueOf(rating));
        }
        public void setDifficulty(String difficulty) {
            this.difficulty.setText(difficulty);
        }
        public void setPreptime(String preptime) {
            this.preptime.setText(preptime);
        }
    }

    @NonNull
    @Override
    public RecipesAdapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        RecipesAdapter.ViewHolder vh = new RecipesAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull  RecipesAdapter.ViewHolder holder, int position) {
        holder.setName(this.recipes.get(position).getName());
        holder.setRating(this.recipes.get(position).getRating());
        holder.setDifficulty(this.recipes.get(position).getDifficulty());
        holder.setPreptime(this.recipes.get(position).getPreptime());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
