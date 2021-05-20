package com.mobdeve.castillo.recipe_finder;

import androidx.annotation.NonNull;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class profile extends AppCompatActivity {

    DrawerLayout navbar;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private ImageView imgProfile;
    private TextView emailProfile, nameProfile, descProfile,navUsernameTv;
    private Button editBtn;
    private RecyclerView recipesRv;
    private ResultsAdapter adapter;
    private ResultsAdapter.RecyclerViewClickListener listener;
    private ArrayList<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.emailProfile = findViewById(R.id.emailProfile);
        this.nameProfile = findViewById(R.id.nameProfile);
        // this.viewBtn = findViewById(R.id.viewBtn);

        init();
        initFirebase();



        LinearLayoutManager lm = new LinearLayoutManager(profile.this);
        recipesRv.setLayoutManager(lm);
        adapter = new ResultsAdapter(recipes, listener);
        recipesRv.setAdapter(adapter);

        //if account is theirs, logout

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        userID = user.getUid();

        reference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userID = snapshot.getValue(User.class);
                navUsernameTv.setText(userID.name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Log.d("INPROFILE","HELLO");
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

                    Log.d("email",email +"");
                    Log.d("name",name +"");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Sad", "User Profile Cannot be Displayed");

            }
        });

        this.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(profile.this, editprofile.class));
            }
        });
    }


    private void initFirebase() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        userID = user.getUid();

        DatabaseReference DB = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

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
    }

    private void init() {
        this.navbar = findViewById(R.id.navdrawer);
        this.imgProfile = findViewById(R.id.imgProfile);
        imgProfile.setImageResource(R.drawable.ic_profilephoto);
        this.emailProfile = findViewById(R.id.emailProfile);
        this.nameProfile = findViewById(R.id.nameProfile);
        this.descProfile = findViewById(R.id.descProfile);
        this.editBtn = findViewById(R.id.editBtn);
        this.navUsernameTv = findViewById(R.id.navUsernameTv);
        this.recipes = new ArrayList<Recipe>();
        setOnClickListener();
        this.recipesRv = (RecyclerView) findViewById(R.id.user_recipesRv);
    }

    private void setOnClickListener() {
        this.listener = new ResultsAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent viewRecipe = new Intent(profile.this, RecipePage.class);

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
        Toast error = Toast.makeText(getApplicationContext(), "You are currently here.", Toast.LENGTH_SHORT);
        error.show();
    }

    public void ClickRecipebook (View view){
        startActivity(new Intent(profile.this, RecipeBook.class));
    }

    public void ClickMyRecipes (View view){
        Intent type = new Intent(profile.this, results.class);
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
                startActivity(new Intent(profile.this,login.class));
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