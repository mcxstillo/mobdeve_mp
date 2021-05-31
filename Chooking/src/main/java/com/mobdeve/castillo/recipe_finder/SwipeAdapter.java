package com.mobdeve.castillo.recipe_finder;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class SwipeAdapter extends RecyclerView.Adapter<SwipeAdapter.ViewHolder>{

    Context context;
    private ArrayList<Recipe> recipes;
    private RecyclerViewClickListener listener;
    DatabaseReference reference = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");

    public SwipeAdapter (ArrayList<Recipe> recipes, RecyclerViewClickListener listener) {
        this.recipes = recipes;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ConstraintLayout mainLayout;
        private ImageView recipeImg;
        private TextView name, rating, difficulty, time;
        private Button edit, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.mainLayout = (ConstraintLayout) itemView.findViewById(R.id.mainLayout);
            this.recipeImg = (ImageView) itemView.findViewById(R.id.swipe_thumbnailIv);
            this.name = itemView.findViewById(R.id.swipe_nameTv);
            this.rating = itemView.findViewById(R.id.swipe_ratingTv);
            this.difficulty = itemView.findViewById(R.id.swipe_difficultyTv);
            this.time = itemView.findViewById(R.id.swipe_timeTv);
            this.edit = itemView.findViewById(R.id.swipe_editBtn);
            this.delete = itemView.findViewById(R.id.swipe_delBtn);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }

        public void setName(String name) {
            this.name.setText(name);
        }

        public void setRating(String rating) {
            this.rating.setText(rating);
        }

        public void setDifficulty(String difficulty) {
            this.difficulty.setText(difficulty);
        }

        public void setTime(String time) {
            this.time.setText(time);
        }
    }

    @NonNull
    @Override
    public SwipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.swipe_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(!recipes.get(position).getPreptime().equals("") || !recipes.get(position).getCookingtime().equals("")) {
            int total_time;
            total_time = Integer.parseInt(recipes.get(position).getPreptime()) + Integer.parseInt(this.recipes.get(position).getCookingtime());
            holder.setTime(String.valueOf(total_time) + " minutes");
        } else
            holder.setTime("not specified");

        int likes = recipes.get(position).getLikes();
        int dislikes = recipes.get(position).getDislikes();

        float rating = ((float) likes / (likes + dislikes)) * 5;
        if(!(rating>=0)){
            rating = 0;
        }
        else{
            rating =((float) likes / (likes + dislikes)) * 5;
        }

        String imgUri=recipes.get(position).getImgUri();
        Picasso.get().load(imgUri).into(holder.recipeImg);

        DecimalFormat format = new DecimalFormat("0.##");
        holder.setRating(format.format(rating));
        holder.setName(recipes.get(position).getName());
        holder.setDifficulty(recipes.get(position).getDifficulty());

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewRecipe = new Intent(context, RecipePage.class);
                viewRecipe.putExtra("recipeID", recipes.get(position).getRecipeID());
                context.startActivity(viewRecipe);
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toEditRecipe = new Intent(context,CreateRecipe.class);
                toEditRecipe.putExtra("TYPE","UPDATE");
                toEditRecipe.putExtra("recipeID", recipes.get(position).getRecipeID());
                context.startActivity(toEditRecipe);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference.child(recipes.get(position).creator).child("Recipes").child(recipes.get(position).recipeID).removeValue();
                recipes.remove(position);
                notifyDataSetChanged();

            }
        });
    }


    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }
}