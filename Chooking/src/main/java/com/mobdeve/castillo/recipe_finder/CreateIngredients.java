package com.mobdeve.castillo.recipe_finder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.Dataset;
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
    private EditText firstIngr;
    private Button addBtn, nextBtn;
    private int ingrcount = 1;
 //   private TextView navUsernameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ingredients);

        init();

        Intent fromCreate = getIntent();
        //GETS USERID
        DatabaseReference DB = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        //GETS RECIPEID
        String recipeKey = fromCreate.getStringExtra("RecipeKey");
        //gets the reference of recipes posted
        DatabaseReference DBRecipe = DB.child("Recipes").child(recipeKey);
        String ingrKey = DBRecipe.push().getKey();
        Intent toSteps = new Intent(CreateIngredients.this, CreateSteps.class);


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

        if(fromCreate.getStringExtra("TYPE").equals("UPDATE")){
            Log.d("from edit","lesgo");
            toSteps.putExtra("TYPE","UPDATE");
            for(int i=0;i<textFields.size();i++){
                int finalI = i;

                DB.child("Recipes").child(recipeKey).child("Ingredients").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("there are ", snapshot.getChildrenCount() +" ingredients");
                        int ingr = 0;
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String ingredient = dataSnapshot.getValue(String.class);
                            Log.d("ingredient is ",ingredient);
                            Log.d("ingr ",ingr+"");
                            textFields.get(ingr).setText(ingredient);

                            if(ingr== snapshot.getChildrenCount()-1){
                                break;
                            }else{

                                ingr++;
                                LinearLayout ll = new LinearLayout(CreateIngredients.this);
                                ll.setLayoutParams(params);
                                ll.setGravity(17);

                                EditText step = new EditText(CreateIngredients.this);
                                step.setWidth(760);
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

                        DBRecipe.child("Ingredients").setValue(ingredients);

                        LinearLayout ll = new LinearLayout(CreateIngredients.this);
                        ll.setLayoutParams(params);
                        ll.setGravity(17);

                        EditText step = new EditText(CreateIngredients.this);
                        step.setWidth(760);
                        step.setHint("Add ingredient");
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

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBRecipe.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DBRecipe.child("Ingredients").setValue(ingredients);
                        toSteps.putExtra("RecipeKey", fromCreate.getStringExtra("RecipeKey"));
                        startActivity(toSteps);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                for (int i = 0; i < textFields.size(); i++)
                    ingredients.add(textFields.get(i).getText().toString());
            }
        });
    }

    private void init() {
        this.ingredients = new ArrayList<String>();
        this.textFields = new ArrayList<EditText>();
        this.firstIngr = findViewById(R.id.firstIngrEt);
        this.mainLayout = findViewById(R.id.ingrMainLayout);
        this.addBtn = findViewById(R.id.addIngredientsBtn);
        this.nextBtn = findViewById(R.id.ingrNextBtn);

        textFields.add(firstIngr);
    }
}