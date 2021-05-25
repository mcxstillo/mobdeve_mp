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
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    DrawerLayout navbar;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID, currentUserID;
    private ImageView profilepic;
    private TextView navUsernameTv, nameProfile, emailProfile, descProfile;
    private Button editBtn;
    private ArrayList<User> usersList;
    private ArrayList<Recipe> recipesList;
    private ArrayList<Recipe> searchRecipes;
    private SearchView searchBtn;
    private RecyclerView searchRv;
    private SearchAdapter searchResultsAdapter;
    private SearchAdapter.RecyclerViewClickListener searchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        initFirebase();


        //SEARCH FUNCTION---
        //makes arraylist of users
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User userID = dataSnapshot.getValue(User.class);
                    usersList.add(userID);
                    Log.d("userid",userID.userID);

                    //this is causing the error itself
                    reference.child(userID.userID).child("Recipes").addValueEventListener(new ValueEventListener() {
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
//                searchRecipes.clear();
//                for(Recipe object : recipesList){
//                    Log.d("objectname",object.name);
//                    if(object.name.toLowerCase().contains(query.toLowerCase())){
//                        Log.d("objectname",object.name);
//                        searchRecipes.add(object);
//                    }
//                }
                Intent toRecipeBook = new Intent(Profile.this, RecipeBook.class);


                toRecipeBook.putExtra("query",query);
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


        currentUserID =FirebaseAuth.getInstance().getCurrentUser().getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String name = userProfile.name;
                    String email = userProfile.email;
                    String desc = userProfile.desc;

                    emailProfile.setText(email);
                    nameProfile.setText(name);
                    descProfile.setText(desc);

                    Picasso.get().load(userProfile.profPicID).into(profilepic);

                    Picasso.get().load(userProfile.profPicID).into(profilepic);

                    Log.d("email",email +"");
                    Log.d("name",name +"");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Sad", "User Profile Cannot be Displayed");

            }
        });
    }

    private void initFirebase() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        userID = user.getUid();

        DatabaseReference DB = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    private void init() {
        this.navbar = findViewById(R.id.navdrawer);
        this.profilepic = (ImageView) findViewById(R.id.imgProfile);
        profilepic.setImageResource(R.drawable.ic_profilephoto);
        this.editBtn = findViewById(R.id.editBtn);
        this.navUsernameTv = findViewById(R.id.navUsernameTv);
        this.nameProfile = findViewById(R.id.nameProfile);
        this.emailProfile = findViewById(R.id.emailProfile);
        this.descProfile = findViewById(R.id.descProfile);

        this.searchBtn = findViewById(R.id.searchBtn);
        this.searchRecipes = new ArrayList<>();
        this.usersList = new ArrayList<>();
        this.recipesList = new ArrayList<>();
        this.searchRv = (RecyclerView) findViewById(R.id.searchRv);
        SearchsetOnClickListener();
    }

    private void SearchsetOnClickListener() {
        this.searchListener = new SearchAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent viewRecipe = new Intent(Profile.this, RecipePage.class);

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

    public static void closeDrawer(DrawerLayout drawer) {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickProfile (View view){
        Toast error = Toast.makeText(getApplicationContext(), "You are currently here.", Toast.LENGTH_SHORT);
        error.show();
    }

    public void ClickRecipebook (View view){
        startActivity(new Intent(Profile.this, RecipeBook.class));
    }

    public void ClickMyRecipes (View view){
        Intent type = new Intent(Profile.this, results.class);
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
                startActivity(new Intent(Profile.this,login.class));
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