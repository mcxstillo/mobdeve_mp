package com.mobdeve.castillo.recipe_finder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{

    private ArrayList<Comment> comments;

    public CommentAdapter(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView profilepic;
        private TextView name, comment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.profilepic = itemView.findViewById(R.id.comment_img);
            this.name = itemView.findViewById( R.id.comment_nameTv);
            this.comment = itemView.findViewById(R.id.commentTv);
        }

        public void setName(String name) {
            this.name.setText(name);
        }

        public void setComment(String comment) {
            this.comment.setText(comment);
        }
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_layout, parent, false);
        CommentAdapter.ViewHolder vh = new CommentAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        // Picasso for profile photo
        holder.setName(comments.get(position).getUser().getName());
        holder.setComment(comments.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
