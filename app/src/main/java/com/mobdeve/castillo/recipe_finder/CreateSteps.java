package com.mobdeve.castillo.recipe_finder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CreateSteps extends AppCompatActivity {

    private ArrayList<Steps> steps;
    private LinearLayout mainLayout;
    private Button addBtn;
    private TextView stepsdesc;
    private int stepnum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_steps);

        init();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 50, 0, 50);

        addBtn.setOnClickListener(new View.OnClickListener() {
            String descnum;
            Steps step;

            @Override
            public void onClick(View v) {
                step = new Steps(stepnum, stepsdesc.getText().toString());
                steps.add(step);

                stepnum++;

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
        });
    }

    private void init() {
        this.steps = new ArrayList<Steps>();
        this.mainLayout = findViewById(R.id.stepsMainLayout);
        this.addBtn = findViewById(R.id.addStepBtn);
        this.stepsdesc = findViewById(R.id.stepsEt);
        stepsdesc.setId(stepnum);
/*        this.stepdesc = findViewById(R.id.stepsEt);
        this.delBtn = findViewById(R.id.delStepBtn);*/
    }
}

