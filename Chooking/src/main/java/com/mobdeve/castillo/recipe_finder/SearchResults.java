package com.mobdeve.castillo.recipe_finder;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class SearchResults extends AppCompatActivity {

    DrawerLayout navbar;
    private FirebaseUser user;
    private DatabaseReference reference;
    private DatabaseReference DBSearch;
    private TextView noresultTv;
    private String userID;
    private RecyclerView searchRv;
    private SearchAdapter searchResultsAdapter;
    private SearchAdapter.RecyclerViewClickListener searchListener;
    private ArrayList<User> usersList;
    private ArrayList<Recipe> recipesList;
    private ArrayList<Recipe> searchRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        init();

        this.searchListener = new SearchAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent viewRecipe = new Intent(SearchResults.this, RecipePage.class);

                String recipeID = searchRecipes.get(position).getRecipeID();
                Log.d("RecipeID",recipeID);
                viewRecipe.putExtra("recipeID",recipeID);
                //in going to recipebook, pass arraylist of recipes, loop that in the thing to see if nag match ba sa clinick nung user, then display the details
//                viewRecipe.putExtra("position",position);
                startActivity(viewRecipe);
            }
        };
    }

    private void init() {
        this.navbar = findViewById(R.id.navdrawer);
        this.noresultTv = findViewById(R.id.noresultTv);
        this.searchRv = (RecyclerView) findViewById(R.id.resultsRv);
        this.usersList = new ArrayList<User>();
        this.recipesList = new ArrayList<Recipe>();
        this.searchRecipes = new ArrayList<Recipe>();

        // if search array is empty, hide rv and display noresultTv

        LinearLayoutManager lm = new LinearLayoutManager(SearchResults.this);
        searchRv.setLayoutManager(lm);
        searchResultsAdapter = new SearchAdapter(searchRecipes, searchListener);
        searchRv.setAdapter(searchResultsAdapter);
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
        startActivity(new Intent(SearchResults.this, Profile.class));
    }

    public void ClickRecipebook (View view){
        Toast error = Toast.makeText(getApplicationContext(), "You are currently here.", Toast.LENGTH_SHORT);
        error.show();
    }

    public void ClickMyRecipes (View view){
        Intent type = new Intent(SearchResults.this, results.class);
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
                startActivity(new Intent(SearchResults.this,MainActivity.class));
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