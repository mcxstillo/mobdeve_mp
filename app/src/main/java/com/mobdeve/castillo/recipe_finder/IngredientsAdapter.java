package com.mobdeve.castillo.recipe_finder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder>{

    ArrayList<String> ingredients;

    public IngredientsAdapter(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.item = itemView.findViewById(R.id.ingredientTv);
        }

        public void setIngredient(String item) {
            this.item.setText(item);
        }
    }

    @NonNull
    @Override
    public IngredientsAdapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_layout, parent, false);
        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull  IngredientsAdapter.ViewHolder holder, int position) {
        holder.setIngredient(this.ingredients.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}
