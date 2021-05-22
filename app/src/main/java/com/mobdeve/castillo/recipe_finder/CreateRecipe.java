package com.mobdeve.castillo.recipe_finder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.FileProvider;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class CreateRecipe extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    StorageReference storageReference;
    String currentPhotoPath;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 101;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    DrawerLayout navbar;
    String recipeKey, stepsKey;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private ImageView recipeimg;
    private TextView navUsernameTv;
    private EditText name, preptime, cooktime, desc;
    private Spinner cuisine, size, difficulty;
    ArrayAdapter<CharSequence> cuisine_adapter, size_adapter, difficulty_adapter;
    private String selected_cuisine, selected_size, selected_diff; // hi cams use this string to get values ng cuisine and size since dito ko inassign yung values for dropdown
    private Button nextBtn, updateBtn, img_cameraBtn, img_galleryBtn;
    public String type;
    Recipe recipe = new Recipe();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        Intent intent_type = getIntent();
        type = intent_type.getStringExtra("TYPE");


        init();

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        userID = user.getUid();
        DatabaseReference DB = FirebaseDatabase.getInstance("https://mobdeve-b369a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        this.recipeKey =Objects.requireNonNull(DB.push().getKey());
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

        //CAMERA BUTTON
        this.img_cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("clicked ","camera btn");
                dispatchTakePictureIntent();
            }
        });


        //do this
        this.img_galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("clicked ","gallery btn");
                Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery,GALLERY_REQUEST_CODE);
            }
        });

        this.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User userName = snapshot.getValue(User.class);
                        recipe.setName(name.getText().toString());
                        Log.d("user",userName.getName());
                        recipe.setCreator(userID);
                        recipe.setCuisine(selected_cuisine);
                        recipe.setServing_size(selected_size);
                        recipe.setDifficulty(selected_diff);
                        recipe.setPreptime(preptime.getText().toString());
                        recipe.setCookingtime(cooktime.getText().toString());
                        recipe.setDesc(desc.getText().toString());
                        recipe.setLikes(0);

                        Log.d("recipekey?",recipeKey);
                        recipe.setRecipeID(recipeKey);
                        User userProfile = snapshot.getValue(User.class);
                        DB.child("Recipes").child(recipeKey).setValue(recipe);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("Sad", "User Profile Cannot be Displayed");
                    }
                });
                Intent toIngr = new Intent(CreateRecipe.this, CreateIngredients.class);
                Log.d("recipekeycreaterecipe",recipeKey);
                toIngr.putExtra("RecipeKey",recipeKey);
                startActivity(toIngr);
            }
        });


        this.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // lagay mo nalang update stuff here yey
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if camera button is chosen
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                File f = new File(currentPhotoPath);
                //setting the IMAGE to the imageview
                recipeimg.setImageURI(Uri.fromFile(f));
                Log.d("URI","Absolute URI of image is "+Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);

                uploadImageToFirebase(f.getName(),contentUri);
                Log.d("filename",f.getName()+"");
            }

        }

        //not sure if adding to db
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("TAG","onActivityResult: Gallery Image Uri: "+imageFileName);
                recipeimg.setImageURI(contentUri);
                uploadImageToFirebase(imageFileName,contentUri);
            }

        }


    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    private void uploadImageToFirebase(String name, Uri contentUri) {
        Log.d("uploadtofb",name);
        final StorageReference imageRef = storageReference.child("pictures/"+name);
        Log.d("imageRef",imageRef.toString());
        Log.d("imageRefputFile",imageRef.putFile(contentUri).toString());
        imageRef.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Log.d("onSuccess","im in");
                Task<Uri> downloadUrl=taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Log.d("onComplete","im in");
                        Toast.makeText(CreateRecipe.this,"Image Uploaded to Firebase", Toast.LENGTH_LONG).show();
                        String t = task.getResult().toString();

                        recipe.setImgUri(t);
                        Log.d("add uri",t);
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("upload failed","very sad");
            }
        });


    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(CreateRecipe.this, "com.example.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
        else{
            Log.d("camera","does not exist here");
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selected = parent.getItemAtPosition(position).toString();
        switch (parent.getId()) {
            case R.id.create_cuisineEt: this.selected_cuisine = selected; break;
            case R.id.create_sizeEt: this.selected_size = selected; break;
            case R.id.create_difficultyEt:Et: this.selected_diff = selected; break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void init() {
        this.navbar = findViewById(R.id.navdrawer);
        this.name = findViewById(R.id.create_nameEt);
        this.cuisine = (Spinner) findViewById(R.id.create_cuisineEt);
        this.size = (Spinner) findViewById(R.id.create_sizeEt);
        this.difficulty = (Spinner) findViewById(R.id.create_difficultyEt);
        this.preptime = findViewById(R.id.create_prepEt);
        this.cooktime = findViewById(R.id.create_cookEt);
        this.desc = findViewById(R.id.create_descEt);
        this.nextBtn = findViewById(R.id.create_nextBtn);
        this.updateBtn = findViewById(R.id.create_updateBtn);
        this.img_cameraBtn = findViewById(R.id.img_cameraBtn);
        this.img_galleryBtn = findViewById(R.id.img_galleryBtn);
        this.storageReference =  FirebaseStorage.getInstance("gs://mobdeve-b369a.appspot.com/").getReference();
        this.navUsernameTv = findViewById(R.id.navUsernameTv);
        this.recipeimg = (ImageView) findViewById(R.id.recipeIv);
        recipeimg.setImageResource(R.drawable.default_img);

        switch(type) {
            case "CREATE": updateBtn.setVisibility(View.GONE); break;
            case "UPDATE": nextBtn.setVisibility(View.GONE);
                           updateBtn.setVisibility(View.VISIBLE); break;
        }

        cuisine_adapter = ArrayAdapter.createFromResource(this,R.array.cuisine_array,R.layout.support_simple_spinner_dropdown_item);
        size_adapter = ArrayAdapter.createFromResource(this, R.array.size_array,R.layout.support_simple_spinner_dropdown_item);
        difficulty_adapter = ArrayAdapter.createFromResource(this, R.array.difficulty,R.layout.support_simple_spinner_dropdown_item);
        cuisine_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        size_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        cuisine.setAdapter(cuisine_adapter);
        size.setAdapter(size_adapter);
        difficulty.setAdapter(difficulty_adapter);

        cuisine.setOnItemSelectedListener(this);
        size.setOnItemSelectedListener(this);
        difficulty.setOnItemSelectedListener(this);
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
        startActivity(new Intent(CreateRecipe.this, profile.class));
    }

    public void ClickRecipebook (View view){
        startActivity(new Intent(CreateRecipe.this, RecipeBook.class));
    }

    public void ClickMyRecipes (View view){
        Intent type = new Intent(CreateRecipe.this, results.class);
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
                startActivity(new Intent(CreateRecipe.this,MainActivity.class));
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