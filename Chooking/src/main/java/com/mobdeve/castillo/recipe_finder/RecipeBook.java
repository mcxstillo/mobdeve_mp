package com.mobdeve.castillo.recipe_finder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
/*import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;*/

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Type;

public class RecipeBook extends AppCompatActivity implements Serializable {

    //LIKED RECIPES/LANDING PAGE?
    //SEARCH PAGE
    DrawerLayout navbar;
    private FirebaseUser user;
    private DatabaseReference reference;
    private DatabaseReference DBSearch;
    private String userID;
    private ArrayList<Recipe> recipes;
    private RecyclerView recipesRv;
    private RecyclerView searchRv;
    private ResultsAdapter adapter;
    private SearchAdapter searchResultsAdapter;
    private FloatingActionButton createBtn;
    private ResultsAdapter.RecyclerViewClickListener listener;
    private SearchAdapter.RecyclerViewClickListener searchListener;
    private TextView notice;
    private TextView navUsernameTv;
    private SearchView searchBtn;
    private ArrayList<User> usersList;
    private ArrayList<Recipe> recipesList;
    private ArrayList<Recipe> searchRecipes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_book);

        init();
        Log.d("recipebeforecondi",""+recipes.size());

        reference = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        DBSearch = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");




        //SEARCH FUNCTION---
        //makes arraylist of users
        DBSearch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User userID = dataSnapshot.getValue(User.class);
                    usersList.add(userID);
                    Log.d("userid",userID.userID);


                    DBSearch.child(userID.userID).child("Recipes").addValueEventListener(new ValueEventListener() {
//                    DBSearch.child(dataSnapshot.getValue(User.class).userID).child("Recipes").addValueEventListener(new ValueEventListener() {
//                      reference.child("Recipes").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                            recipes.clear();
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Recipe recipe = dataSnapshot.getValue(Recipe.class);
                                recipesList.add(recipe);
                                Log.d("array",recipesList+"");
                            }


                            reference.child("Liked").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Log.d("snapshotcount",snapshot.getChildrenCount()+"");
                                    recipes.clear();
                                    Log.d("beforeadding",""+recipes.size());

                                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                                        Recipe recipeItem = dataSnapshot.getValue(Recipe.class);
                                        Log.d("recipeboookitem",recipeItem.recipeID);
                                        dataSnapshot.getValue(Recipe.class);

                                        //check if the recipe exists before adding to liked
                                        Log.d("recipesListcontains",recipesList.contains(recipeItem)+"");

                                            for(int i =0;i<recipesList.size();i++){
                                                Log.d("nameofrecipe",recipesList.get(i).recipeID);
                                                 if(recipesList.get(i).recipeID.equals(recipeItem.recipeID)){
                                                    recipes.add(recipeItem);
                                                 }
                                            }

                                    }

                                    if (recipes == null || recipes.isEmpty()) {
                                        recipesRv.setVisibility(View.GONE);
                                        notice.setVisibility(View.VISIBLE);
                                        Log.d("secondIF","hi");
                                    }
                                    else {
                                        LinearLayoutManager lm = new LinearLayoutManager(RecipeBook.this);
                                        recipesRv.setLayoutManager(lm);
                                        adapter = new ResultsAdapter(recipes, listener);
                                        searchRv.setVisibility(View.GONE);
                                        recipesRv.setVisibility(View.VISIBLE);
                                        notice.setVisibility(View.GONE);
                                        recipesRv.setAdapter(adapter);
                                        Log.d("secondELSE","hi");
                                        Log.d("adaptercount",adapter.getItemCount()+"");
                                        adapter.notifyDataSetChanged();
                                    }


                                }

                                @Override
                                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                                }
                            });














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

//        Log.d("onQueryTextSubmit", query);
        Intent fromOthers = getIntent();

        if(fromOthers.getStringExtra("query")!=null){

            Log.d("objectnamefromOthers","INSIDE THE IF STATEMETN "+fromOthers.getStringExtra("query"));
            searchRecipes.clear();
            for(Recipe object : recipesList){
                Log.d("objectnamefromOthers","inside for loop "+object.name);
                if(object.name.toLowerCase().contains(fromOthers.getStringExtra("query").toLowerCase())){
                    Log.d("objectnamefromOthers","inside in statemnet "+object.name);
                    searchRecipes.add(object);
                    Log.d("searchRecipes.size",searchRecipes.size()+"");
                }
            }

            //MAKE INTENT TO PASS ARRAY TO DISPLAY SEARCH RESULTS
//                searchResultsAdapter = new ResultsAdapter(searchRecipes,listener);
            if (searchRecipes == null || searchRecipes.isEmpty()) {
                searchRv.setVisibility(View.GONE);
                recipesRv.setVisibility(View.VISIBLE);
                notice.setVisibility(View.VISIBLE);
                Log.d("secondIF","hi");
            }
            else {
                LinearLayoutManager llm = new LinearLayoutManager(RecipeBook.this);
                searchRv.setLayoutManager(llm);
                searchResultsAdapter = new SearchAdapter(searchRecipes, searchListener);
                recipesRv.setVisibility(View.GONE);
                searchRv.setVisibility(View.VISIBLE);
                notice.setVisibility(View.GONE);
                searchRv.setAdapter(searchResultsAdapter);
                Log.d("secondELSE","hi");
                Log.d("searchResultsAdapter",searchResultsAdapter.getItemCount()+"");
                searchResultsAdapter.notifyDataSetChanged();


            }
        }



        //di nattrigger if galing sa iba
        searchBtn.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Log.d("recipesListSize",recipesList.size()+"");
//                Log.d("searchRecipesSize",searchRecipes.size()+"");

                Log.d("query",query);
                //if query from get intent is null, yung nasa baba
                //put query here


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
                    recipesRv.setVisibility(View.VISIBLE);
                    notice.setVisibility(View.VISIBLE);
                    Log.d("secondIF","hi");
                }
                else {
                    LinearLayoutManager llm = new LinearLayoutManager(RecipeBook.this);
                    searchRv.setLayoutManager(llm);
                    searchResultsAdapter = new SearchAdapter(searchRecipes, searchListener);
                    recipesRv.setVisibility(View.GONE);
                    searchRv.setVisibility(View.VISIBLE);
                    notice.setVisibility(View.GONE);
                    searchRv.setAdapter(searchResultsAdapter);
                    Log.d("secondELSE","hi");
                    Log.d("searchResultsAdapter",searchResultsAdapter.getItemCount()+"");
                    searchResultsAdapter.notifyDataSetChanged();


                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("onQueryTextChange", newText);
                return false;
            }
        });


        //SEARCH FUNCTION---


        //loading recyclerview and array
//        reference.child("Liked").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.d("snapshotcount",snapshot.getChildrenCount()+"");
//                recipes.clear();
//
//                //check if the recipe exists before adding to liked
//
//                Log.d("beforeadding",""+recipes.size());
//                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                        Recipe recipeItem = dataSnapshot.getValue(Recipe.class);
//                        Log.d("recipeboookitem",recipeItem.recipeID);
//                        dataSnapshot.getValue(Recipe.class);
//
//
//
//                        recipes.add(recipeItem);
//                        Log.d("afteradding",""+recipes.size());
//
//                    }
//
//                        if (recipes == null || recipes.isEmpty()) {
//                            recipesRv.setVisibility(View.GONE);
//                            notice.setVisibility(View.VISIBLE);
//                            Log.d("secondIF","hi");
//                        }
//                        else {
//                            LinearLayoutManager lm = new LinearLayoutManager(RecipeBook.this);
//                            recipesRv.setLayoutManager(lm);
//                            adapter = new ResultsAdapter(recipes, listener);
//                            searchRv.setVisibility(View.GONE);
//                            recipesRv.setVisibility(View.VISIBLE);
//                            notice.setVisibility(View.GONE);
//                            recipesRv.setAdapter(adapter);
//                            Log.d("secondELSE","hi");
//                            Log.d("adaptercount",adapter.getItemCount()+"");
//                            adapter.notifyDataSetChanged();
//                        }
//
//                }
//
//            @Override
//            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
//
//            }
//        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userID = snapshot.getValue(User.class);
                navUsernameTv.setText(userID.name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void init() {
        this.navbar = findViewById(R.id.navdrawer);
        this.recipes = new ArrayList<>();
        this.recipesRv = (RecyclerView) findViewById(R.id.recipesRv);
        this.createBtn = findViewById(R.id.createBtn);
        this.notice = findViewById(R.id.noticeTv);
        this.navUsernameTv = findViewById(R.id.navUsernameTv);
        this.searchBtn = findViewById(R.id.searchBtn);
        this.searchRecipes = new ArrayList<>();
        this.usersList = new ArrayList<>();
        this.recipesList = new ArrayList<>();
        this.searchRv = (RecyclerView) findViewById(R.id.searchRv);
        SearchsetOnClickListener();
        setOnClickListener();
    }

    private void setOnClickListener() {
        this.listener = new ResultsAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent viewRecipe = new Intent(RecipeBook.this, RecipePage.class);
                String recipeID = recipes.get(position).getRecipeID();
                Log.d("RecipeID",recipeID);
                viewRecipe.putExtra("recipeID",recipeID);
//                viewRecipe.putExtra("position",position);
                startActivity(viewRecipe);
            }
        };
    }

        private void SearchsetOnClickListener() {
        this.searchListener = new SearchAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent viewRecipe = new Intent(RecipeBook.this, RecipePage.class);

                String recipeID = searchRecipes.get(position).getRecipeID();
                Log.d("RecipeID",recipeID);
                viewRecipe.putExtra("recipeID",recipeID);
                //in going to recipebook, pass arraylist of recipes, loop that in the thing to see if nag match ba sa clinick nung user, then display the details
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

    public void ClickLogo(View view) {
        closeDrawer(navbar);
    }

    public static void closeDrawer(DrawerLayout drawer) {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickProfile (View view){
        startActivity(new Intent(RecipeBook.this, Profile.class));
    }

    public void ClickRecipebook (View view){
        Toast error = Toast.makeText(getApplicationContext(), "You are currently here.", Toast.LENGTH_SHORT);
        error.show();
    }

    public void ClickMyRecipes (View view){
        Intent type = new Intent(RecipeBook.this, results.class);
        type.putExtra("TYPE", "MY_RECIPES");
        startActivity(type);
    }

    public void ClickLogout (View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Logout user form firebase in this function and redirect to MainActivity
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(RecipeBook.this,MainActivity.class));
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