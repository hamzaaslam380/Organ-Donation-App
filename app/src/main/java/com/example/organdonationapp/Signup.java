package com.example.organdonationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Signup extends AppCompatActivity {
    EditText etSignupEmail,etSignupPhone,etSignupPassword,etSignupConfirmPassword,etSignupName;
    Button btnSubmit;
    TextView tvGoBack;

    DatabaseReference firebase;
    DatabaseReference user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        init();

        addUser();

        tvGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this,MainActivity.class));
                Signup.this.finish();
            }
        });
    }

    private void addUser() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etSignupEmail.getText().toString().trim().isEmpty()){
                    etSignupEmail.setError("Please Enter Your Email");
                }
                else if(etSignupPhone.getText().toString().trim().isEmpty()){
                    etSignupPhone.setError("Please Enter Your Phone Number");
                }
                else if(etSignupPassword.getText().toString().trim().isEmpty()){
                    etSignupPassword.setError("Please Enter Your Password");
                }
                else if(etSignupConfirmPassword.getText().toString().trim().isEmpty()){
                    etSignupConfirmPassword.setError("Please Re Enter Your Password");
                }
                else if(etSignupName.getText().toString().trim().isEmpty()){
                    etSignupName.setError("Please Enter Your Name");
                }
                else if(etSignupPassword.getText().toString().trim().equals(etSignupConfirmPassword.getText().toString().trim())){
                    final int[] yes = {0};
                    user = FirebaseDatabase.getInstance().getReference().child("Users");
                    user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (dataSnapshot.child("Email").getValue().toString().equals(etSignupEmail.getText().toString())) {
                                    yes[0] = 1;
                                    break;
                                }
                            }
                            if(yes[0] == 1){
                                etSignupEmail.setError("Email Exist");
                            }
                            else{
                                saveDataInFireBase();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else {
                    etSignupConfirmPassword.setError("Password Must Be Same");
                }
            }
        });
    }

    private void saveDataInFireBase() {
        HashMap<String,String> post = new HashMap<>();
        post.put("Name",etSignupName.getText().toString().trim());
        post.put("Email",etSignupEmail.getText().toString().trim());
        post.put("Phone",etSignupPhone.getText().toString().trim());
        post.put("Password",etSignupConfirmPassword.getText().toString().trim());
        post.put("Type","user");


        // to store data on new node we use push
        firebase.push().setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Signup.this, "Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Signup.this,MainActivity.class));
                Signup.this.finish();
                resetAllFields();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Signup.this, "Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetAllFields() {
        etSignupName.setText("");
        etSignupPhone.setText("");
        etSignupEmail.setText("");
        etSignupPassword.setText("");
        etSignupConfirmPassword.setText("");
    }

    public void init(){
        etSignupEmail = findViewById(R.id.etSignupEmail);
        etSignupPhone = findViewById(R.id.etSignupPhone);
        etSignupConfirmPassword = findViewById(R.id.etSignupConfirmPassword);
        etSignupPassword = findViewById(R.id.etSignupPassword);
        etSignupName = findViewById(R.id.etSignupName);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvGoBack = findViewById(R.id.tvGoBack);
        firebase = FirebaseDatabase.getInstance().getReference().child("Users");
    }
}