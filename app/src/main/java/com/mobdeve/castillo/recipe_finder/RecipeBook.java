package com.mobdeve.castillo.recipe_finder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecipeBook extends AppCompatActivity {

    //LIKED RECIPES/LANDING PAGE?
    //SEARCH PAGE
    DrawerLayout navbar;
    private FirebaseUser user;
    private DatabaseReference reference;
    private DatabaseReference DBSearch;
    private String userID;
    private ArrayList<Recipe> recipes;
    private RecyclerView recipesRv;
    private ResultsAdapter adapter;
    private FloatingActionButton createBtn;
    private ResultsAdapter.RecyclerViewClickListener listener;
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


        //makes arraylist of users
        DBSearch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.d("snapshotusercount",snapshot.getChildrenCount()+"");
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User userID = dataSnapshot.getValue(User.class);
                    Log.d("userIDdapat",userID.getUserID()+"");
                    usersList.add(userID);
                }
                for(int i=0;i<usersList.size();i++){
                    Log.d("userSize",""+usersList.size());
                    DBSearch.child(usersList.get(i).userID).child("Recipes").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Recipe recipeID = dataSnapshot.getValue(Recipe.class);

                                Log.d("recipeIDdapat",recipeID.recipeID+"");
                                recipesList.add(recipeID);
                                Log.d("recipesListSize",recipesList.size()+"");
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //makes arraylist of all recipes created (recipeID)


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
                ResultsAdapter searchResultsAdapter = new ResultsAdapter(searchRecipes,listener);
                recipesRv.setAdapter(searchResultsAdapter);

                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("onQueryTextChange", newText);
                return false;
            }
        });



        reference.child("Liked").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("snapshotcount",snapshot.getChildrenCount()+"");
//                if(snapshot.getChildrenCount()!=0){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Recipe recipeItem = dataSnapshot.getValue(Recipe.class);
                        Log.d("recipeboookitem",recipeItem.recipeID);
                        dataSnapshot.getValue(Recipe.class);
                        recipes.add(recipeItem);





                        Log.d("afteradding",""+recipes.size());


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

    //search DONT DELETE
//    private void search(String str){
//        ArrayList<Recipe> searchRecipes = new ArrayList<>();
//        for(Recipe object : recipesList){
//            if(object.getDesc().toLowerCase().contains(str.toLowerCase())){
//                searchRecipes.add(object);
//            }
//        }
//
//        ResultsAdapter searchResultsAdapter = new ResultsAdapter(searchRecipes);
//        recipesRv.setAdapter(searchResultsAdapter);
//
//    }

    private void init() {
        this.navbar = findViewById(R.id.navdrawer);
        this.recipes = new ArrayList<>();
        this.usersList = new ArrayList<>();
        this.recipesList = new ArrayList<>();

        this.recipesRv = (RecyclerView) findViewById(R.id.recipesRv);
        this.createBtn = findViewById(R.id.createBtn);
        this.notice = findViewById(R.id.noticeTv);
        this.navUsernameTv = findViewById(R.id.navUsernameTv);
        this.searchBtn = findViewById(R.id.searchBtn);
        this.searchRecipes = new ArrayList<>();
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
        startActivity(new Intent(RecipeBook.this, profile.class));
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