package com.mobdeve.castillo.recipe_finder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CreateIngredients extends AppCompatActivity {

    private ArrayList<String> ingredients;
    private ArrayList<EditText> textFields;
    private LinearLayout mainLayout;
    private Button addBtn, nextBtn;
    private int ingrcount = 1;
 //   private TextView navUsernameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ingredients);

        init();

        Intent fromCreate = getIntent();
        //this activity reads the database to get the key of the recipe
        //GETS USERID
        DatabaseReference DB = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        //GETS RECIPEID
        String recipeKey = fromCreate.getStringExtra("RecipeKey");
        //gets the reference of recipes posted
        DatabaseReference DBRecipe = DB.child("Recipes").child(recipeKey);
        String ingrKey = DBRecipe.push().getKey();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 50, 0, 50);

        DB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userID = snapshot.getValue(User.class);
//                navUsernameTv.setText(userID.name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DBRecipe.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Log.d("ingrArraySize",ingredients.size()+"");

                        DBRecipe.child("Ingredients").setValue(ingredients);

                        LinearLayout ll = new LinearLayout(CreateIngredients.this);
                        ll.setLayoutParams(params);
                        ll.setGravity(17);

                        EditText step = new EditText(CreateIngredients.this);
                        step.setWidth(760);
                        step.setHint("Enter ingredient");
                        textFields.add(step);

                        Log.d("STEP", "CREATED STEP NUMBER " + step.getId());

                        ll.addView(step);
                        mainLayout.addView(ll);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBRecipe.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DBRecipe.child("Ingredients").setValue(ingredients);
                        Intent toSteps = new Intent(CreateIngredients.this, CreateSteps.class);
                        toSteps.putExtra("RecipeKey", fromCreate.getStringExtra("RecipeKey"));
                        startActivity(toSteps);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                for (int i = 0; i < textFields.size(); i++) {
                    ingredients.add(textFields.get(i).getText().toString());

                    Log.d("ARRAY CONTENT", "" + ingredients.get(i).toString());
                }
            }
        });
    }

    private void init() {
        this.ingredients = new ArrayList<String>();
        this.textFields = new ArrayList<EditText>();
        this.mainLayout = findViewById(R.id.ingrMainLayout);
        this.addBtn = findViewById(R.id.addIngredientsBtn);
        this.nextBtn = findViewById(R.id.ingrNextBtn);
    }
}