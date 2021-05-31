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
import android.view.Menu;
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
// import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    DrawerLayout navbar;
    private FirebaseUser user;
    private DatabaseReference reference;
    private DatabaseReference DB;
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

                    if (userProfile.getProfPicID() == null)
                        profilepic.setImageResource(R.drawable.ic_profilephoto);
                    else
                        Picasso.get().load(userProfile.profPicID).into(profilepic);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Sad", "User Profile Cannot be Displayed");

            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, editprofile.class));
            }
        });
    }



    private void initFirebase() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        userID = user.getUid();

        DB = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
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
        Toast.makeText(getApplicationContext(), "You are currently here.", Toast.LENGTH_SHORT).show();
    }

    public void ClickRecipebook (View view){
        startActivity(new Intent(Profile.this, RecipeBook.class));
    }

    public void ClickMyRecipes (View view){
        Intent type = new Intent(Profile.this, SwipeRecipes.class);
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