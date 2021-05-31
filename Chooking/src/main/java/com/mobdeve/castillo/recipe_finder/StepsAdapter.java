package com.mobdeve.castillo.recipe_finder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

    ArrayList<Steps> steps;

    public StepsAdapter(ArrayList<Steps> steps) {
        this.steps = steps;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView stepnum, stepdesc;

        public ViewHolder(@NonNull  View itemView) {
            super(itemView);

            stepnum = itemView.findViewById(R.id.step_numTv);
            stepdesc = itemView.findViewById(R.id.step_descTv);
        }

        public void setStepnum(int stepnum) {
            this.stepnum.setText(String.valueOf(stepnum));
        }

        public void setStepdesc(String stepdesc) {
            this.stepdesc.setText(stepdesc);
        }
    }

    @NonNull
    @Override
    public StepsAdapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.steps_layout, parent, false);
        StepsAdapter.ViewHolder vh = new StepsAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull  StepsAdapter.ViewHolder holder, int position) {
        holder.setStepnum(this.steps.get(position).getStepnum());
        holder.setStepdesc(this.steps.get(position).getStep_desc());
    }

    @Override
    public int getItemCount() {
        return this.steps.size();
    }
}
