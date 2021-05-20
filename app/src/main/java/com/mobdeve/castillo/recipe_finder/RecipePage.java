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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipePage extends AppCompatActivity {

    DrawerLayout navbar;
    private ImageView photo;
    private TextView nameTv, creatorTv, cuisineTv, servingsTv, preptimeTv, cooktimeTv, descTv;
    private TextView navUsernameTv;
    private RecyclerView stepsRv;
    private StepsAdapter adapter;
    private FloatingActionButton faveBtn;
    private ArrayList<Steps> steps;
    private ResultsAdapter.RecyclerViewClickListener listener;
//    private Steps steps;
    DatabaseReference DB = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    //database reference for the recipe's ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_page);

        init();
        // insert db read here yey
        //get recipe ID first
        Intent fromResults = getIntent();
        String recipeKey = fromResults.getStringExtra("recipeID");

        Log.d("fromResultsKey",recipeKey);
        DatabaseReference DBName = DB.child("name");
        DatabaseReference DBRecipe = DB.child(recipeKey);


        DB.child("Recipes").child(recipeKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Recipe recipeItem = snapshot.getValue(Recipe.class);
                    FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child(recipeItem.getCreator()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User userID = snapshot.getValue(User.class);
                            creatorTv.setText(userID.name);
                            navUsernameTv.setText(userID.name);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                //gets UserID
                String imgUri=recipeItem.getImgUri();
                Picasso.get().load(imgUri).into(photo);

                nameTv.setText(recipeItem.getName());
                creatorTv.setText("by " + recipeItem.getCreator());

                cuisineTv.setText(recipeItem.getCuisine());
                servingsTv.setText(recipeItem.getServing_size() + " SERVINGS");
                preptimeTv.setText(recipeItem.getPreptime() + " MINUTES");
                cooktimeTv.setText(recipeItem.getCookingtime() + " MINUTES");
                descTv.setText(recipeItem.getDesc());


                DB.child("Recipes").child(recipeKey).child("Steps").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("sscountBFR",snapshot.getChildrenCount()+"");
                            for(int i =0;i<snapshot.getChildrenCount();i++){

                                DB.child("Recipes").child(recipeKey).child("Steps").child(String.valueOf(i)).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Steps stepItem = snapshot.getValue(Steps.class);
                                        Log.d("stepItem",stepItem.step_desc);
                                        steps.add(stepItem);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                        }
//                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LinearLayoutManager lm = new LinearLayoutManager(RecipePage.this);
        this.stepsRv.setLayoutManager(lm);
        this.adapter = new StepsAdapter(this.steps);
        this.stepsRv.setAdapter(this.adapter);

        faveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecipePage.this, "Saved to Recipe Book", Toast.LENGTH_SHORT).show();

                DB.child("Recipes").child(recipeKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //set value of the recipe key?????
                        Recipe likedRecipe = snapshot.getValue(Recipe.class);
                        //add create steps here
                        DB.child("Liked").child(likedRecipe.recipeID).setValue(likedRecipe);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void init() {
        this.navbar = findViewById(R.id.navdrawer);
        this.photo = (ImageView) findViewById(R.id.recipe_img);
        this.nameTv = findViewById(R.id.recipe_nameTv);
        this.creatorTv = findViewById(R.id.recipe_opTv);
        this.cuisineTv = findViewById(R.id.recipe_cuisineTv);
        this.servingsTv = findViewById(R.id.recipe_servingTv);
        this.preptimeTv = findViewById(R.id.recipe_prepTv);
        this.cooktimeTv = findViewById(R.id.recipe_cookTv);
        this.descTv = findViewById(R.id.recipe_descTv);
        this.stepsRv = (RecyclerView) findViewById(R.id.stepsRv);
        this.faveBtn = findViewById(R.id.faveBtn);
        this.navUsernameTv = findViewById(R.id.navUsernameTv);
        this.steps = new ArrayList<Steps>();
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
        startActivity(new Intent(RecipePage.this, profile.class));
    }

    public void ClickRecipebook (View view){
        startActivity(new Intent(RecipePage.this, RecipeBook.class));
    }

    public void ClickMyRecipes (View view){
        Intent type = new Intent(RecipePage.this, results.class);
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