package com.sustainablefood.com;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddLocationActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView addImageView;
    private Spinner categoriesSpinner;
    private EditText nameOfFoodEdit;
    private EditText notesEdit;
    private Button addLocationButton;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference userReference = firebaseDatabase.getReference("User").child(firebaseAuth.getUid().toString());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        initializeViews();
    }

    private void initializeViews(){
        addImageView = findViewById(R.id.add_location_imageview);
        addImageView.setOnClickListener(this);
        categoriesSpinner = findViewById(R.id.add_location_category_spinner);
        nameOfFoodEdit = findViewById(R.id.add_location_name_of_food);
        notesEdit = findViewById(R.id.add_location_notes);
        addLocationButton = findViewById(R.id.add_location_button);
        addLocationButton.setOnClickListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.add_location_imageview){

        }
        else{

        }
    }
}
