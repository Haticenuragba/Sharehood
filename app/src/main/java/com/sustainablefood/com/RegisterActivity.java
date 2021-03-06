package com.sustainablefood.com;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editName;
    private EditText editPhone;
    private EditText editPassword;
    private EditText editPasswordVerify;
    private Button btnRegister;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference userReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        editName = findViewById(R.id.signup_activity_username);
        editEmail = findViewById(R.id.signup_activity_email);
        editPassword = findViewById(R.id.signup_activity_password);
        editPasswordVerify = findViewById(R.id.signup_activity_verify_password);
        editPhone = findViewById(R.id.signup_activity_phone);
        btnRegister = findViewById(R.id.signup_activity_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        if (editEmail.getText().toString().equalsIgnoreCase("")
                || editPassword.getText().toString().equalsIgnoreCase("")
                || editName.getText().toString().equalsIgnoreCase("")
                || editPhone.getText().toString().equalsIgnoreCase("")
                || editPasswordVerify.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(RegisterActivity.this, "Please fill empty fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!editPassword.getText().toString().equalsIgnoreCase(editPasswordVerify.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "Passwords doesn't match", Toast.LENGTH_SHORT).show();
            return;
        }

        final android.app.AlertDialog progressDialog = new android.app.AlertDialog.Builder(RegisterActivity.this)
                .setTitle("In Progress...")
                .setMessage("Please wait...")
                .setCancelable(false)
                .show();

        Task<AuthResult> task =
                firebaseAuth.createUserWithEmailAndPassword(editEmail.getText().toString(), editPassword.getText().toString());
        task.addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()) {
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("Success")
                            .setMessage("You registered successfully")
                            .setNeutralButton("Log in", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .show();
                    userReference =  firebaseDatabase.getReference("User").child(task.getResult().getUser().getUid());
                    userReference.child("name").setValue(editName.getText().toString());
                    userReference.child("phone").setValue(editPhone.getText().toString());
                } else {
                    try {
                        throw task.getException();
                    } catch(FirebaseAuthWeakPasswordException e) {
                        Toast.makeText(getApplicationContext(), "Password is not strong enough", Toast.LENGTH_SHORT).show();
                    } catch(FirebaseAuthInvalidCredentialsException e) {
                        Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                    } catch(FirebaseAuthUserCollisionException e) {
                        Toast.makeText(getApplicationContext(), "User is already registered", Toast.LENGTH_SHORT).show();
                    } catch(Exception e) {
                        Log.v("firebase eror" , e.toString());
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}