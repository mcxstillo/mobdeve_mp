package com.mobdeve.castillo.recipe_finder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class CreateSteps extends AppCompatActivity {

    DrawerLayout navbar;
    private ArrayList<Steps> steps;
    private ArrayList<EditText> textFields;
    private LinearLayout mainLayout;
    private EditText firstStep;
    private Button addBtn, postBtn;
    private int stepnum = 1;
    private TextView navUsernameTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_steps);

        init();
        Intent fromCreate = getIntent();
        //this activity reads the database to get the key of the recipe
        //GETS USERID
        DatabaseReference DB = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        //GETS RECIPEID
        String recipeKey = fromCreate.getStringExtra("RecipeKey");
        //gets the reference of recipes posted
        DatabaseReference DBRecipe = DB.child("Recipes").child(recipeKey);
        String stepsKey = DBRecipe.push().getKey();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 50, 0, 50);

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

        if(fromCreate.getStringExtra("TYPE").equals("UPDATE")){

            for(int i=0;i<textFields.size();i++){
                DB.child("Recipes").child(recipeKey).child("Steps").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int stepCtr=0;
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Steps stepItem = dataSnapshot.getValue(Steps.class);

                            textFields.get(stepCtr).setText(stepItem.step_desc);

                            if(stepCtr== snapshot.getChildrenCount()-1){
                                break;
                            }else{

                                stepCtr++;
                                LinearLayout ll = new LinearLayout(CreateSteps.this);
                                ll.setLayoutParams(params);
                                ll.setGravity(17);

                                EditText step = new EditText(CreateSteps.this);
                                step.setWidth(770);
                                step.setHint("Add ingredient");
                                textFields.add(step);

                                ll.addView(step);
                                mainLayout.addView(ll);

                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        }




        addBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DBRecipe.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        DBRecipe.child("Steps").setValue(steps);

                        LinearLayout ll = new LinearLayout(CreateSteps.this);
                        ll.setLayoutParams(params);
                        ll.setGravity(17);

                        EditText step = new EditText(CreateSteps.this);
                        step.setWidth(760);
                        step.setHint("Enter instructions");
                        textFields.add(step);

                        ll.addView(step);
                        mainLayout.addView(ll);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    DBRecipe.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DBRecipe.child("Steps").setValue(steps);
                        Intent toResults = new Intent(CreateSteps.this,SwipeRecipes.class);

                        startActivity(toResults);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                for (int i = 0; i < textFields.size(); i++)
                    steps.add(new Steps(i+1, textFields.get(i).getText().toString()));
            }
        });
    }

    private void init() {
        this.navbar = findViewById(R.id.navdrawer);
        this.steps = new ArrayList<Steps>();
        this.textFields = new ArrayList<EditText>();
        this.firstStep = findViewById(R.id.firstStepEt);
        this.postBtn = findViewById(R.id.testBtn);
        this.mainLayout = findViewById(R.id.stepsMainLayout);
        this.addBtn = findViewById(R.id.addStepBtn);
        this.navUsernameTv = findViewById(R.id.navUsernameTv);

        textFields.add(firstStep);
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
        startActivity(new Intent(CreateSteps.this, Profile.class));
    }

    public void ClickRecipebook (View view){
        startActivity(new Intent(CreateSteps.this, RecipeBook.class));
    }

    public void ClickMyRecipes (View view){
        Intent type = new Intent(CreateSteps.this, SwipeRecipes.class);
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
                startActivity(new Intent(CreateSteps.this,MainActivity.class));
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

