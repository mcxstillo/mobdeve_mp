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
    private RecyclerViewClickListener listener;

    public ResultsAdapter (ArrayList<Recipe> recipes, RecyclerViewClickListener listener) {
        this.recipes = recipes;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            listener.onClick(v, getAdapterPosition());
            listener.onClick(v, getBindingAdapterPosition());

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

        if(!recipes.get(position).getPreptime().equals("") || !recipes.get(position).getCookingtime().equals("")) {
            int total_time;
            total_time = Integer.parseInt(recipes.get(position).getPreptime()) + Integer.parseInt(this.recipes.get(position).getCookingtime());
            holder.setPreptime(String.valueOf(total_time) + " minutes");
        }


        String imgUri=recipes.get(position).getImgUri();
        Picasso.get().load(imgUri).into(holder.recipeImg);

        holder.setName(recipes.get(position).getName());
        holder.setRating(recipes.get(position).getRating());
        holder.setDifficulty(recipes.get(position).getDifficulty());
        holder.setPreptime("not specified");
    }

    @Override
    public int getItemCount() {
        return this.recipes.size();
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }
}
