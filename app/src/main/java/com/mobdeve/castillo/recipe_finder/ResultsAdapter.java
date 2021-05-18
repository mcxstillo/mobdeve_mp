package com.mobdeve.castillo.recipe_finder;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder>{

    private ArrayList<Recipe> recipes;

    public ResultsAdapter (ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, rating, difficulty, preptime;
        public ImageView recipeImg;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.recipeImg = (ImageView) itemView.findViewById(R.id.thumbnailIv);
            this.name = itemView.findViewById(R.id.recipe_nameTv);
            this.name = itemView.findViewById(R.id.nameTv);
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
        public void setRecipeImg(String img){
            this.recipeImg.setImageURI(Uri.parse(img));
        }


    }

    @NonNull
    @Override
    public ResultsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String imgUri=this.recipes.get(position).getImgUri();
        Picasso.get().load(imgUri).into(holder.recipeImg);

        holder.setName(this.recipes.get(position).getName());
        holder.setRating(this.recipes.get(position).getRating());
        holder.setDifficulty(this.recipes.get(position).getDifficulty());
        holder.setPreptime(this.recipes.get(position).getPreptime());
    }

    @Override
    public int getItemCount() {
        return this.recipes.size();
    }
}
