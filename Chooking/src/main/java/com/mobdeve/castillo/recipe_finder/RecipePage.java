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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class RecipePage extends AppCompatActivity {

    DrawerLayout navbar;
    private ImageView photo, like, dislike;
    private TextView nameTv, creatorTv, cuisineTv, servingsTv, preptimeTv, cooktimeTv, descTv, comments;
    private TextView navUsernameTv;
    private RecyclerView stepsRv, ingredientsRv;
    private StepsAdapter adapter;
    private IngredientsAdapter ingrAdapter;
    private FloatingActionButton faveBtn;
    private ArrayList<Steps> steps;
    private ArrayList<User> usersList;
    private ArrayList<String> ingredients;
    private ResultsAdapter.RecyclerViewClickListener listener;
    DatabaseReference DB = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    DatabaseReference DBOthers = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
    private static String currentUserID;
    private ArrayList<Recipe> recipesList;
    private ArrayList<Recipe> searchRecipes;
    private RecyclerView searchRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_page);

        init();
        Intent fromOthers = getIntent();
        String recipeKey = fromOthers.getStringExtra("recipeID");
        Intent toComments = new Intent(RecipePage.this, CommentSection.class);
        Intent toProfile = new Intent(RecipePage.this, OtherProfile.class);

        //create arraylist of all recipes
        DBOthers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                //all the users in the DB get added to usersList
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User userID = dataSnapshot.getValue(User.class);
                    usersList.add(userID);
                }

                //display's current users name
                DB.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User currentUser = snapshot.getValue(User.class);
                        navUsernameTv.setText(currentUser.name);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                //loops all the users to get their UserID
                for(int i =0;i<usersList.size();i++){
                    int finalI = i;

                    DBOthers.child(usersList.get(finalI).userID).child("Recipes").child(recipeKey).child("Steps").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            final long childrenCount = snapshot.getChildrenCount();
                            steps.clear();
                            for(int i =0;i<snapshot.getChildrenCount();i++){
                                int finalI1 = i;
                                DBOthers.child(usersList.get(finalI).userID).child("Recipes").child(recipeKey).child("Steps").child(String.valueOf(i)).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Steps stepItem = snapshot.getValue(Steps.class);

                                        if(steps.size()<childrenCount)
                                            steps.add(stepItem);

                                        adapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }

                                });

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });

                    //for ingredients
                    DBOthers.child(usersList.get(finalI).userID).child("Recipes").child(recipeKey).child("Ingredients").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            final long childrenCount = snapshot.getChildrenCount();
                            ingredients.clear();

                            for(int i =0;i<snapshot.getChildrenCount();i++){

                                DBOthers.child(usersList.get(finalI).userID).child("Recipes").child(recipeKey).child("Ingredients").child(String.valueOf(i)).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String item = snapshot.getValue(String.class);
                                        if(ingredients.size()<childrenCount)
                                            ingredients.add(item);

                                        ingrAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });


                    //gets the user and checks their recipes
                    DBOthers.child(usersList.get(finalI).userID).child("Recipes").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //loops through user's recipes
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String username = usersList.get(finalI).getName();

                                Recipe recipeItem = dataSnapshot.getValue(Recipe.class);

                                //if the user's recipe matches
                                if(recipeItem.recipeID.equals(recipeKey)){

                                    String imgUri=recipeItem.getImgUri();
                                    Picasso.get().load(imgUri).into(photo);

                                    toComments.putExtra("userIDComments",usersList.get(finalI).userID);
                                    toProfile.putExtra("userID",usersList.get(finalI).userID);

                                    nameTv.setText(recipeItem.getName());

                                    creatorTv.setText("by "+ username);
                                    cuisineTv.setText(recipeItem.getCuisine().toUpperCase());
                                    servingsTv.setText(recipeItem.getServing_size() + " SERVINGS");
                                    preptimeTv.setText(recipeItem.getPreptime() + " MINUTES");
                                    cooktimeTv.setText(recipeItem.getCookingtime() + " MINUTES");
                                    descTv.setText(recipeItem.getDesc());

                                    like.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            int likes = recipeItem.getLikes();
                                            int dislikes = recipeItem.getDislikes();
                                            float rating = ((float)likes / (likes + dislikes)) * 5;

                                            Toast.makeText(RecipePage.this, "Left a like for this recipe", Toast.LENGTH_SHORT).show();

                                            likes++;
                                            recipeItem.setLikes(likes);
                                            recipeItem.setDislikes(dislikes);

                                            DBOthers.child(usersList.get(finalI).userID).child("Recipes").child(recipeKey).child("likes").setValue(recipeItem.getLikes());
                                            DBOthers.child(usersList.get(finalI).userID).child("Recipes").child(recipeKey).child("dislikes").setValue(recipeItem.getDislikes());
                                        }
                                    });

                                    dislike.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            int likes = recipeItem.getLikes();
                                            int dislikes = recipeItem.getDislikes();

                                            Toast.makeText(RecipePage.this, "Left a dislike for this recipe", Toast.LENGTH_SHORT).show();

                                            dislikes++;
                                            recipeItem.setLikes(likes);
                                            recipeItem.setDislikes(dislikes);
                                            DBOthers.child(usersList.get(finalI).userID).child("Recipes").child(recipeKey).child("likes").setValue(recipeItem.getLikes());
                                            DBOthers.child(usersList.get(finalI).userID).child("Recipes").child(recipeKey).child("dislikes").setValue(recipeItem.getDislikes());
                                        }
                                    });
                                    //IMPORTANT!
                                    break;
                                }

                                //for steps -- does not show if you put here
                                //if the user's recipe matches
                                if(recipeItem.recipeID.equals(recipeKey)){
                                    String imgUri=recipeItem.getImgUri();
                                    Picasso.get().load(imgUri).into(photo);

                                    toComments.putExtra("userIDComments",usersList.get(finalI).userID);
                                    toProfile.putExtra("userID",usersList.get(finalI).userID);

                                    nameTv.setText(recipeItem.getName());
                                    creatorTv.setText("by "+ username);
                                    cuisineTv.setText(recipeItem.getCuisine().toUpperCase());
                                    servingsTv.setText(recipeItem.getServing_size() + " SERVINGS");
                                    preptimeTv.setText(recipeItem.getPreptime() + " MINUTES");
                                    cooktimeTv.setText(recipeItem.getCookingtime() + " MINUTES");
                                    descTv.setText(recipeItem.getDesc());

                                    like.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            int likes = recipeItem.getLikes();
                                            int dislikes = recipeItem.getDislikes();
                                            float rating = ((float)likes / (likes + dislikes)) * 5;

                                            likes++;
                                            recipeItem.setLikes(likes);
                                            recipeItem.setDislikes(dislikes);

                                            DBOthers.child(usersList.get(finalI).userID).child("Recipes").child(recipeKey).child("likes").setValue(recipeItem.getLikes());
                                            DBOthers.child(usersList.get(finalI).userID).child("Recipes").child(recipeKey).child("dislikes").setValue(recipeItem.getDislikes());

                                        }
                                    });

                                    dislike.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            int likes = recipeItem.getLikes();
                                            int dislikes = recipeItem.getDislikes();
                                            float rating = ((float) likes / (likes + dislikes)) * 5;

                                            dislikes++;
                                            recipeItem.setLikes(likes);
                                            recipeItem.setDislikes(dislikes);

                                            DBOthers.child(usersList.get(finalI).userID).child("Recipes").child(recipeKey).child("likes").setValue(recipeItem.getLikes());
                                            DBOthers.child(usersList.get(finalI).userID).child("Recipes").child(recipeKey).child("dislikes").setValue(recipeItem.getDislikes());
                                        }
                                    });
                                    //IMPORTANT!
                                    break;
                                }



                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }

        });



        //ONLY WORKS FOR CURRENT USER AND THEIR RECIPES
        DB.child("Recipes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Recipe recipeItem = dataSnapshot.getValue(Recipe.class);

                    if(recipeItem.recipeID.equals(recipeKey)){

                        FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child(recipeItem.creator).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                User userID = snapshot.getValue(User.class);
                                //since own recipe, show edit btn
                                creatorTv.setText("by "+ userID.name);

                                toProfile.putExtra("userID",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                navUsernameTv.setText(userID.name);

                                //gets UserID
                                String imgUri=recipeItem.getImgUri();
                                Picasso.get().load(imgUri).into(photo);
                                nameTv.setText(recipeItem.getName());
                                cuisineTv.setText(recipeItem.getCuisine().toUpperCase());
                                servingsTv.setText(recipeItem.getServing_size() + " SERVINGS");
                                preptimeTv.setText(recipeItem.getPreptime() + " MINUTES");
                                cooktimeTv.setText(recipeItem.getCookingtime() + " MINUTES");
                                descTv.setText(recipeItem.getDesc());

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }




                DB.child("Recipes").child(recipeKey).child("Steps").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final long childrenCount = snapshot.getChildrenCount();
                        steps.clear();
                        for(int i =0;i<snapshot.getChildrenCount();i++){

                            DB.child("Recipes").child(recipeKey).child("Steps").child(String.valueOf(i)).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    Steps stepItem = snapshot.getValue(Steps.class);
                                    //steps get added in the array here

                                    if(steps.size()<childrenCount)
                                        steps.add(stepItem);

                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }

                            });

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

                //for ingredients
                DB.child("Recipes").child(recipeKey).child("Ingredients").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        final long childrenCount = snapshot.getChildrenCount();
                        ingredients.clear();

                        for(int i =0;i<snapshot.getChildrenCount();i++) {

                            DB.child("Recipes").child(recipeKey).child("Ingredients").child(String.valueOf(i)).addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    String item = snapshot.getValue(String.class);

                                    if(ingredients.size()<childrenCount)
                                        ingredients.add(item);

                                    ingrAdapter.notifyDataSetChanged();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LinearLayoutManager lm = new LinearLayoutManager(RecipePage.this);
        this.stepsRv.setLayoutManager(lm);
        this.adapter = new StepsAdapter(steps);
        this.stepsRv.setAdapter(this.adapter);

        LinearLayoutManager ingrlm = new LinearLayoutManager(RecipePage.this);
        this.ingredientsRv.setLayoutManager(ingrlm);
        this.ingrAdapter = new IngredientsAdapter(ingredients);
        this.ingredientsRv.setAdapter(this.ingrAdapter);

        //when FAVE button is clicked
        faveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecipePage.this, "Saved to Recipe Book", Toast.LENGTH_SHORT).show();
                steps.clear();
                ingredients.clear();
                for(int i =0;i<usersList.size();i++){
                    String userIDofRecipe = usersList.get(i).userID;
                    steps.clear();
                    ingredients.clear();
                    DBOthers.child(userIDofRecipe).child("Recipes").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange( DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                steps.clear();
                                ingredients.clear();
                                Recipe currentRecipe = dataSnapshot.getValue(Recipe.class);

                                if (currentRecipe.recipeID.equals(recipeKey))
                                    DB.child("Liked").child(recipeKey).setValue(currentRecipe);
                                else
                                    Log.d("currentRecipe", "null");
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                        }
                    });
                }
            }
        });

        //redirect to user profile
        this.creatorTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(toProfile);
            }
        });

        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass recipe key
                toComments.putExtra("recipeKey",recipeKey);
                startActivity(toComments);
            }
        });
    }


    private void init() {
        this.navbar = findViewById(R.id.navdrawer);
        this.photo = (ImageView) findViewById(R.id.recipe_img);
        this.like = (ImageView) findViewById(R.id.likeBtn);
        this.dislike = (ImageView) findViewById(R.id.dislikeBtn);
        this.nameTv = findViewById(R.id.recipe_nameTv);
        this.creatorTv = findViewById(R.id.recipe_opTv);
        this.cuisineTv = findViewById(R.id.recipe_cuisineTv);
        this.servingsTv = findViewById(R.id.recipe_servingTv);
        this.preptimeTv = findViewById(R.id.recipe_prepTv);
        this.cooktimeTv = findViewById(R.id.recipe_cookTv);
        this.comments = findViewById(R.id.commentsTv);
        this.descTv = findViewById(R.id.recipe_descTv);
        this.stepsRv = (RecyclerView) findViewById(R.id.stepsRv);
        this.ingredientsRv = (RecyclerView) findViewById(R.id.ingredientsRv);
        this.faveBtn = findViewById(R.id.faveBtn);
        this.navUsernameTv = findViewById(R.id.navUsernameTv);
        this.steps = new ArrayList<Steps>();
        this.usersList = new ArrayList<User>();
        this.ingredients = new ArrayList<String>();
        this.searchRecipes = new ArrayList<>();
        this.usersList = new ArrayList<>();
        this.recipesList = new ArrayList<>();
        this.searchRv = (RecyclerView) findViewById(R.id.searchRv);
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
        startActivity(new Intent(RecipePage.this, Profile.class));
        finish();
    }

    public void ClickRecipebook (View view){
        startActivity(new Intent(RecipePage.this, RecipeBook.class));
        finish();
    }

    public void ClickMyRecipes (View view){
        Intent type = new Intent(RecipePage.this, SwipeRecipes.class);
        type.putExtra("TYPE", "MY_RECIPES");
        startActivity(type);
        finish();
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
                startActivity(new Intent(RecipePage.this,MainActivity.class));
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