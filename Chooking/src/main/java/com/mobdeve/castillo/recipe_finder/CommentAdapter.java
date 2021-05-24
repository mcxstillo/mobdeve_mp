package com.mobdeve.castillo.recipe_finder;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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

        //paadd na lang setter
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
        DatabaseReference DBName = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
                .child(comments.get(position).user).child("name");
        DatabaseReference DB = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");

        DB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    //of type string daw to????
                    User user = dataSnapshot.getValue(User.class);
                    //if a user from the db matches this user, display
                        if(user.userID != null&& !(user.userID.equals("")) && user.userID.equals(comments.get(position).getUser())){
                            Log.d("UserinRV",user.name);

                            //set profile pic of commenter
                            String imgUri=user.profPicID;
                            Picasso.get().load(imgUri).into(holder.profilepic);

                            holder.setName(user.name);
                            holder.setComment(comments.get(position).getComment());
                        }

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        //palagay just in case
//        holder.setComment(comments.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
