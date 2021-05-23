package com.mobdeve.castillo.recipe_finder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentSection extends AppCompatActivity {

    DrawerLayout navbar;
    private RecyclerView commentsRv;
    private EditText commentEt;
    private ImageButton sendBtn;
    private ArrayList<Comment> comments;
    private CommentAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        init();
        Intent fromPage = getIntent();
        DatabaseReference DB = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
                .child(fromPage.getStringExtra("userIDComments"));
        DatabaseReference DBComment = DB.child("Recipes").child(fromPage.getStringExtra("recipeKey")).child("Comments");
        String commentKey = DBComment.push().getKey();

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Comment comment = new Comment();

//        if (comments == null || comments.isEmpty()) {
//            commentsRv.setVisibility(View.GONE);
//        }
//        else {
//            LinearLayoutManager llm = new LinearLayoutManager(CommentSection.this);
//            commentsRv.setLayoutManager(llm);
//            this.adapter = new CommentAdapter(comments);
//            commentsRv.setAdapter(adapter);
//            adapter.notifyDataSetChanged();
//        }

        Log.d("commentkey",commentKey);
        //loop the data in the db and put in array
        DBComment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("snapshotcount",snapshot.getChildrenCount()+"");
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Comment commentItem = dataSnapshot.getValue(Comment.class);
//                    String commentItem = dataSnapshot.getValue(String.class);
                    Log.d("commentItem",commentItem+"");
                    comments.add(commentItem);
                }
                //when user submits a comment
                sendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String commentKey = DBComment.push().getKey();
                        //comment overwrites, fix
                        comment.setUser(userID);
                        comment.setComment(commentEt.getText().toString());
                        DBComment.child(commentKey).setValue(comment);
                        comments.clear();

                    }
                });
                adapter.notifyDataSetChanged();
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        LinearLayoutManager lm = new LinearLayoutManager(CommentSection.this);
        this.commentsRv.setLayoutManager(lm);
        this.adapter = new CommentAdapter(comments);
        this.commentsRv.setAdapter(this.adapter);

    }

    private void init() {
        this.commentsRv = (RecyclerView) findViewById(R.id.commentsRv);
        this.commentEt = findViewById(R.id.commentEt);
        this.sendBtn = findViewById(R.id.sendBtn);
        this.comments = new ArrayList<Comment>();
//        this.adapter = new CommentAdapter(comments);
    }

    // NAVBAR FUNCTIONS
    public void ClickMenu(View view) {
        openDrawer(navbar);
    }

    public static void openDrawer (DrawerLayout drawer) {
        drawer.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawer) {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickProfile (View view){
        startActivity(new Intent(CommentSection.this, profile.class));
    }

    public void ClickRecipebook (View view){
        startActivity(new Intent(CommentSection.this, RecipeBook.class));
    }

    public void ClickMyRecipes (View view){
        Intent type = new Intent(CommentSection.this, results.class);
        type.putExtra("TYPE", "MY_RECIPES");
        startActivity(type);
    }

    public void ClickLogout(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Logout user form firebase in this function and redirect to MainActivity
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(CommentSection.this,MainActivity.class));
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }
}