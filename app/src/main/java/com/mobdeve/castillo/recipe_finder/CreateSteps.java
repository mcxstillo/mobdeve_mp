package com.mobdeve.castillo.recipe_finder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class CreateSteps extends AppCompatActivity {

    private ArrayList<Steps> steps;
    int counter;
    private LinearLayout mainLayout;
    private Button addBtn, postBtn;
    private TextView stepsdesc;
    private int stepnum = 1;
    String recipeKey,stepsKey;

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
        //gets the reference of recipe
        DatabaseReference DBRecipe = DB.child("Recipes").child(recipeKey);
        String stepsKey = DBRecipe.push().getKey();
        ArrayList<Steps> steps = new ArrayList<>();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 50, 0, 50);
        Steps step = new Steps();

        addBtn.setOnClickListener(new View.OnClickListener() {
            String descnum;
            @Override
            public void onClick(View v) {
                DBRecipe.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        step.setStepnum(stepnum);
                        step.setStep_desc(stepsdesc.getText().toString());
                        steps.add(step);
                        stepnum++;

                        Log.d("stepsArraySize",steps.size()+"");
                        DBRecipe.child("Steps").setValue(steps);

                        LinearLayout ll = new LinearLayout(CreateSteps.this);
                        ll.setLayoutParams(params);
                        ll.setGravity(17);

                        EditText step = new EditText(CreateSteps.this);
                        step.setWidth(760);
                        step.setHint("Enter instructions");
                        step.setId(stepnum);

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

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBRecipe.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        startActivity(new Intent(CreateSteps.this,results.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void init() {
        this.steps = new ArrayList<Steps>();
        this.mainLayout = findViewById(R.id.stepsMainLayout);
        this.addBtn = findViewById(R.id.addStepBtn);
        this.postBtn = findViewById(R.id.postBtn);
        this.stepsdesc = findViewById(R.id.stepsEt);

        stepsdesc.setId(stepnum);
/*        this.stepdesc = findViewById(R.id.stepsEt);
        this.delBtn = findViewById(R.id.delStepBtn);*/
    }
}

