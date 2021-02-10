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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView tvSignup;
    EditText etEmail,etPassword;
    Button btnLogin;

    DatabaseReference user,user1;
    int checkUserExist = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        goToSignUpActivity();

        checkUserExist();
    }

    private void checkUserExist() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if(email.isEmpty()){
                    etEmail.setError("Please Enter Email");
                }
                else if(password.isEmpty()){
                    etPassword.setError("Please Enter Password");
                }
                else{
                    signin(etEmail.getText().toString().trim(),etPassword.getText().toString().trim());

                }
            }
        });
    }

    private void goToSignUpActivity() {
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Signup.class));
                MainActivity.this.finish();
            }
        });
    }

    private void signin(String email, String password) {
         user.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        if( dataSnapshot.child("Email").getValue().toString().equals(email) && dataSnapshot.child("Password").getValue().toString().equals(password)) {
                            Intent intent = new Intent(MainActivity.this,Index.class);
                            intent.putExtra("Type",dataSnapshot.child("Type").getValue().toString());
                            intent.putExtra("Name",dataSnapshot.child("Name").getValue().toString());
                            intent.putExtra("Email",dataSnapshot.child("Email").getValue().toString());
                            intent.putExtra("Phone",dataSnapshot.child("Phone").getValue().toString());
                            intent.putExtra("Password",dataSnapshot.child("Password").getValue().toString());

                            startActivity(intent);
                            MainActivity.this.finish();
                            clearAllFields();
                            checkUserExist = 1;

                            statusUpdateForUser(dataSnapshot.child("Type").getValue().toString());

                            break;
                        }
                        else {
                            checkUserExist = 0;
                        }
                 }
                 if(checkUserExist == 0){
                     Toast.makeText(MainActivity.this, "Please Enter Correct Information", Toast.LENGTH_SHORT).show();
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });

    }

    public void statusUpdateForUser(String string){
       user1 = FirebaseDatabase.getInstance().getReference().child("Type");
        user1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Map<String , Object> map = new HashMap<>();
                    map.put("Type",string);

                    dataSnapshot.getRef()
                            .updateChildren(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            });
                }
                    }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void clearAllFields() {
        etEmail.setText("");
        etPassword.setText("");
    }


    public void init(){
        tvSignup = findViewById(R.id.tvSignup);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        user = FirebaseDatabase.getInstance().getReference().child("Users");
    }
}