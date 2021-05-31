package com.mobdeve.castillo.recipe_finder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class RecipeBook extends AppCompatActivity implements Serializable {

    //LIKED RECIPES/LANDING PAGE?
    //SEARCH PAGE
    DrawerLayout navbar;
    private FirebaseUser user;
    private DatabaseReference reference;
    private DatabaseReference DBSearch;
    private String userID;
    private ImageView supportBtn;
    private ArrayList<Recipe> recipes;
    private RecyclerView recipesRv;
    private RecyclerView searchRv;
    private ResultsAdapter adapter;
    private SearchAdapter searchResultsAdapter;
    private ResultsAdapter.RecyclerViewClickListener listener;
    private SearchAdapter.RecyclerViewClickListener searchListener;
    private TextView notice;
    private TextView navUsernameTv;
    private SearchView searchBtn;
    private ArrayList<User> usersList;
    private ArrayList<Recipe> recipesList;
    private ArrayList<Recipe> searchRecipes;
    private TextView myRecipeBookTv;
    private TextView showingTv;


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
                    recipesList.clear();
                    DBSearch.child(userID.userID).child("Recipes").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                            recipes.clear();
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Recipe recipe = dataSnapshot.getValue(Recipe.class);
                                recipesList.add(recipe);
                            }


                            reference.child("Liked").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    recipes.clear();

                                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                                        Recipe recipeItem = dataSnapshot.getValue(Recipe.class);
                                        dataSnapshot.getValue(Recipe.class);

                                        //check if the recipe exists before adding to liked
                                            for(int i =0;i<recipesList.size();i++){
                                                 if(recipesList.get(i).recipeID.equals(recipeItem.recipeID)){
                                                    recipes.add(recipeItem);
                                                 }
                                            }
                                    }

                                    if (recipes == null || recipes.isEmpty()) {
                                        recipesRv.setVisibility(View.GONE);
                                        notice.setVisibility(View.VISIBLE);
                                        showingTv.setVisibility(View.GONE);
                                    }
                                    else {
                                        LinearLayoutManager lm = new LinearLayoutManager(RecipeBook.this);
                                        recipesRv.setLayoutManager(lm);
                                        recipesRv.setVisibility(View.VISIBLE);
                                        adapter = new ResultsAdapter(recipes, listener);
                                        recipesRv.setAdapter(adapter);
                                        searchRv.setVisibility(View.GONE);
                                        notice.setVisibility(View.GONE);

                                        adapter.notifyDataSetChanged();
                                    }

                                    new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                                        @Override
                                        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                                            return false;
                                        }

                                        @Override
                                        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                                            reference.child("Liked").child(recipes.get(viewHolder.getAdapterPosition()).recipeID).removeValue();
                                            recipes.remove(viewHolder.getAdapterPosition());
                                            adapter.notifyDataSetChanged();
//                                            recipesRv.setAdapter(adapter);
                                        }

                                    }).attachToRecyclerView(recipesRv);

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

            searchRecipes.clear();
            for(Recipe object : recipesList){
                if(object.name.toLowerCase().contains(fromOthers.getStringExtra("query").toLowerCase())){
                    searchRecipes.add(object);
                }
            }

            //MAKE INTENT TO PASS ARRAY TO DISPLAY SEARCH RESULTS
            if (searchRecipes == null || searchRecipes.isEmpty()) {
                searchRv.setVisibility(View.GONE);
                recipesRv.setVisibility(View.VISIBLE);
                notice.setVisibility(View.VISIBLE);
            }
            else {
                LinearLayoutManager llm = new LinearLayoutManager(RecipeBook.this);
                searchRv.setLayoutManager(llm);
                searchResultsAdapter = new SearchAdapter(searchRecipes, searchListener);
                recipesRv.setVisibility(View.GONE);
                searchRv.setVisibility(View.VISIBLE);
                notice.setVisibility(View.GONE);
                searchRv.setAdapter(searchResultsAdapter);
                searchResultsAdapter.notifyDataSetChanged();
            }
        }

        supportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RecipeBook.this);
                builder.setTitle("Recipe Book");
                builder.setMessage("This is your recipe book where you can see recipes you have liked for easy access \n " +
                        "\n" +
                        "Swipe to remove any recipe from the recipe book");
                builder.setNeutralButton("Got it", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });

        searchBtn.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.d("query",query);
                //if query from get intent is null, yung nasa baba
                //put query here
                searchRecipes.clear();
                for(Recipe object : recipesList){
                    if(object.name.toLowerCase().contains(query.toLowerCase())){
                        searchRecipes.add(object);
                    }
                }

                //MAKE INTENT TO PASS ARRAY TO DISPLAY SEARCH RESULTS
                if (searchRecipes == null || searchRecipes.isEmpty()) {
                    searchRv.setVisibility(View.GONE);
                    recipesRv.setVisibility(View.VISIBLE);
                    notice.setVisibility(View.VISIBLE);
                    showingTv.setVisibility(View.GONE);
                }
                else {
                    LinearLayoutManager llm = new LinearLayoutManager(RecipeBook.this);
                    searchRv.setLayoutManager(llm);
                    searchResultsAdapter = new SearchAdapter(searchRecipes, searchListener);
                    recipesRv.setVisibility(View.GONE);
                    searchRv.setVisibility(View.VISIBLE);
                    myRecipeBookTv.setVisibility(View.GONE);
                    showingTv.setVisibility(View.VISIBLE);
                    String str= "Showing search results for" +" '"+query+"'";
                    showingTv.setText(str);
                    notice.setVisibility(View.GONE);
                    searchRv.setAdapter(searchResultsAdapter);
                    searchResultsAdapter.notifyDataSetChanged();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

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
        this.supportBtn = findViewById(R.id.book_supportBtn);
        this.recipesRv = (RecyclerView) findViewById(R.id.recipesRv);
        this.notice = findViewById(R.id.noticeTv);
        this.navUsernameTv = findViewById(R.id.navUsernameTv);
        this.showingTv = findViewById(R.id.showingTv);
        this.searchBtn = findViewById(R.id.searchBtn);
        this.myRecipeBookTv = findViewById(R.id.myRecipeBookTv);
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
        Intent type = new Intent(RecipeBook.this, SwipeRecipes.class);
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