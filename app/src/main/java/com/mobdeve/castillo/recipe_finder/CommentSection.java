package com.mobdeve.castillo.recipe_finder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

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

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // put database activity for adding comment :D
            }
        });
    }

    private void init() {
        this.commentsRv = (RecyclerView) findViewById(R.id.commentsRv);
        this.commentEt = findViewById(R.id.commentEt);
        this.sendBtn = findViewById(R.id.sendBtn);
        this.comments = new ArrayList<Comment>();
        this.adapter = new CommentAdapter(comments);
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