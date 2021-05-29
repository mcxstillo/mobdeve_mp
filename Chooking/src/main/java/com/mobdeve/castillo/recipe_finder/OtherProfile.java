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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OtherProfile extends AppCompatActivity {

    DrawerLayout navbar;
    private FirebaseUser user;
    private DatabaseReference reference, DB;
    private String userID;
    private ImageView imgProfile;
    private TextView emailProfile, nameProfile, descProfile,navUsernameTv;
    private RecyclerView recipesRv;
    private ResultsAdapter adapter;
    private ResultsAdapter.RecyclerViewClickListener listener;
    private ArrayList<Recipe> recipes;
    private String currentUserID;
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
        setContentView(R.layout.activity_other_profile);

        currentUserID =FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.emailProfile = findViewById(R.id.emailProfile);
        this.nameProfile = findViewById(R.id.nameProfile);
        // this.viewBtn = findViewById(R.id.viewBtn);

        init();
        initFirebase();

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

        LinearLayoutManager lm = new LinearLayoutManager(OtherProfile.this);
        recipesRv.setLayoutManager(lm);
        adapter = new ResultsAdapter(recipes, listener);
        recipesRv.setAdapter(adapter);

        //if account is theirs, logout

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");

        //check if user's profile or others


        //if other user checking other people's profile
        Intent fromRecipePage = getIntent();


        //if user doesnt own the recipe
        if(fromRecipePage.getStringExtra("userID").equals(currentUserID)){
            //pass user's ID

            userID = user.getUid();
        }else{
            userID =fromRecipePage.getStringExtra("userID");
        }

        Log.d("userID",userID);



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
                    Picasso.get().load(userProfile.profPicID).into(imgProfile);

                    Picasso.get().load(userProfile.profPicID).into(imgProfile);

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

        DB = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        Intent fromRecipePage = getIntent();


        //if user doesnt own the recipe
        if(fromRecipePage.getStringExtra("userID").equals(currentUserID)){
            //pass user's ID

            userID = user.getUid();
        }else{
            userID =fromRecipePage.getStringExtra("userID");
        }




        Log.d("userIDinpProfile",userID);
        FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child(userID).child("Recipes").addValueEventListener(new ValueEventListener() {
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
        this.imgProfile = findViewById(R.id.userImgProfile);
        imgProfile.setImageResource(R.drawable.ic_profilephoto);
        this.emailProfile = findViewById(R.id.userEmailProfile);
        this.nameProfile = findViewById(R.id.userNameProfile);
        this.descProfile = findViewById(R.id.userDescProfile);
        this.navUsernameTv = findViewById(R.id.navUsernameTv);
        this.recipes = new ArrayList<Recipe>();
        setOnClickListener();
        this.recipesRv = (RecyclerView) findViewById(R.id.user_recipesRv);
        this.searchBtn = findViewById(R.id.searchBtn);
        this.searchRecipes = new ArrayList<>();
        this.usersList = new ArrayList<>();
        this.recipesList = new ArrayList<>();
        this.searchRv = (RecyclerView) findViewById(R.id.searchRv);

    }




    private void setOnClickListener() {
        this.listener = new ResultsAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent viewRecipe = new Intent(OtherProfile.this, RecipePage.class);

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
        startActivity(new Intent(OtherProfile.this, Profile.class));
    }

    public void ClickMyRecipes (View view){
        Intent type = new Intent(OtherProfile.this, SwipeRecipes.class);
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
                startActivity(new Intent(OtherProfile.this,login.class));
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