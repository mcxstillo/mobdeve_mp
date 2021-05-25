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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class results extends AppCompatActivity{

    //POSTED RECIPES/SEARCH RESULTS
    DrawerLayout navbar;
    private RecyclerView resultsRv;
    private ResultsAdapter adapter;
    private FloatingActionButton createBtn;
    private ResultsAdapter.RecyclerViewClickListener listener;
    private TextView noticeTv;
    ArrayList<Recipe> recipes;
    private TextView navUsernameTv;
    private ArrayList<User> usersList;
    private ArrayList<Recipe> recipesList;
    private ArrayList<Recipe> searchRecipes;
    private SearchView searchBtn;
    private RecyclerView searchRv;
    private SearchAdapter searchResultsAdapter;
    private SearchAdapter.RecyclerViewClickListener searchListener;

    DatabaseReference DBSearch = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
    DatabaseReference DB = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        init();
        DB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userID = snapshot.getValue(User.class);
                navUsernameTv.setText(userID.name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //gets all the recipes to display
        DB.child("Recipes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Recipe recipeItem = dataSnapshot.getValue(Recipe.class);
                    dataSnapshot.getValue(Recipe.class);
                    recipes.add(recipeItem);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        this.createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent create = new Intent(results.this, CreateRecipe.class);
                create.putExtra("TYPE", "CREATE");
                startActivity(create);
            }
        });

        if(recipes == null) {
            noticeTv.setVisibility(View.VISIBLE);
            resultsRv.setVisibility(View.GONE);
        } else {
            noticeTv.setVisibility(View.GONE);
            LinearLayoutManager lm = new LinearLayoutManager(results.this);
            resultsRv.setLayoutManager(lm);
            adapter = new ResultsAdapter(recipes, listener);
            resultsRv.setAdapter(adapter);
        }


        //SEARCH FUNCTION---
        //makes arraylist of users
        DBSearch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User userID = dataSnapshot.getValue(User.class);
                    usersList.add(userID);
                    Log.d("userid",userID.userID);

                    //this is causing the error itself
                    DBSearch.child(userID.userID).child("Recipes").addValueEventListener(new ValueEventListener() {
                        //                    DBSearch.child(dataSnapshot.getValue(User.class).userID).child("Recipes").addValueEventListener(new ValueEventListener() {
//                      reference.child("Recipes").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Recipe recipe = dataSnapshot.getValue(Recipe.class);
                                recipesList.add(recipe);
                                Log.d("array",recipesList+"");
                            }
                        }

                        @Override
                        public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });






        searchBtn.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Log.d("recipesListSize",recipesList.size()+"");
//                Log.d("searchRecipesSize",searchRecipes.size()+"");
                Log.d("onQueryTextSubmit", query);
                searchRecipes.clear();
                for(Recipe object : recipesList){
                    Log.d("objectname",object.name);
                    if(object.name.toLowerCase().contains(query.toLowerCase())){
                        Log.d("objectname",object.name);
                        searchRecipes.add(object);
                    }
                }

                //MAKE INTENT TO PASS ARRAY TO DISPLAY SEARCH RESULTS
//                searchResultsAdapter = new ResultsAdapter(searchRecipes,listener);
                if (searchRecipes == null || searchRecipes.isEmpty()) {
                    searchRv.setVisibility(View.GONE);
                    Log.d("secondIF","hi");
                }
                else {
                    LinearLayoutManager llm = new LinearLayoutManager(results.this);
                    searchRv.setLayoutManager(llm);
                    searchResultsAdapter = new SearchAdapter(searchRecipes, searchListener);
//                    recipesRv.setVisibility(View.GONE);
                    searchRv.setVisibility(View.VISIBLE);
//                    notice.setVisibility(View.GONE);
                    searchRv.setAdapter(searchResultsAdapter);
                    Log.d("secondELSE","hi");
                    Log.d("searchResultsAdapter",searchResultsAdapter.getItemCount()+"");
                    searchResultsAdapter.notifyDataSetChanged();


                }
                Intent toRecipeBook = new Intent(results.this, RecipeBook.class);
                startActivity(toRecipeBook);
                return false;


            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("onQueryTextChange", newText);
                return false;
            }



        });


        //SEARCH FUNCTION---


    }

    private void init() {
        this.navbar = findViewById(R.id.navdrawer);
        this.resultsRv = findViewById(R.id.resultsRv);
        this.recipes = new ArrayList<Recipe>();
        this.createBtn = findViewById(R.id.createBtn);
        this.noticeTv = findViewById(R.id.recipes_noticeTv);
        this.navUsernameTv = findViewById(R.id.navUsernameTv);
        this.searchBtn = findViewById(R.id.searchBtn);
        this.searchRecipes = new ArrayList<>();
        this.usersList = new ArrayList<>();
        this.recipesList = new ArrayList<>();
        this.searchRv = (RecyclerView) findViewById(R.id.searchRv);
        SearchsetOnClickListener();
        setOnClickListener();
    }

    private void SearchsetOnClickListener() {
        this.searchListener = new SearchAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent viewRecipe = new Intent(results.this, RecipePage.class);

                String recipeID = searchRecipes.get(position).getRecipeID();
                Log.d("RecipeID",recipeID);
                viewRecipe.putExtra("recipeID",recipeID);
                //in going to recipebook, pass arraylist of recipes, loop that in the thing to see if nag match ba sa clinick nung user, then display the details
//                viewRecipe.putExtra("position",position);
                startActivity(viewRecipe);
            }
        };
    }

    private void setOnClickListener() {
        this.listener = new ResultsAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent viewRecipe = new Intent(results.this, RecipePage.class);

                String recipeID = recipes.get(position).getRecipeID();
                Log.d("RecipeID",recipeID);
                viewRecipe.putExtra("recipeID",recipeID);
//                viewRecipe.putExtra("position",position);
                startActivity(viewRecipe);
            }
        };
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
        startActivity(new Intent(results.this, Profile.class));
    }

    public void ClickRecipebook (View view){
        startActivity(new Intent(results.this, RecipeBook.class));
    }

    public void ClickMyRecipes (View view){
        Toast error = Toast.makeText(getApplicationContext(), "You are currently here.", Toast.LENGTH_SHORT);
        error.show();
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
                startActivity(new Intent(results.this,MainActivity.class));
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