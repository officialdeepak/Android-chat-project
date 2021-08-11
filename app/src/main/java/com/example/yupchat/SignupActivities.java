package com.example.yupchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Toast;

import com.example.yupchat.databinding.ActivitySignupActivitiesBinding;
import com.example.yupchat.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivities extends AppCompatActivity {
    ActivitySignupActivitiesBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupActivitiesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();




        auth = FirebaseAuth.getInstance();
        database =FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(SignupActivities.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We're Creating Account");

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.etUsername.getText().toString().isEmpty()){
                    binding.etUsername.setError("Enter your UserName");
                    return;
                }
                if(binding.etEmailid.getText().toString().isEmpty()){
                    binding.etEmailid.setError("Enter your email");
                    return;
                }
                if(binding.etPassword.getText().toString().isEmpty()){
                    binding.etPassword.setError("Enter your password");
                    return;
                }
                progressDialog.show();
                auth.createUserWithEmailAndPassword
                        (binding.etEmailid.getText().toString(),binding.etPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();

                                if (task.isSuccessful()){
                                    Users user = new Users (binding.etUsername.getText().toString(),binding.etEmailid.getText().toString(),binding.etPassword.getText().toString());
                                    String id = task.getResult().getUser().getUid();
                                    database.getReference().child("users").child(id).setValue(user);


                                    Toast.makeText(SignupActivities.this,"User Create Successfully",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(SignupActivities.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        binding.TVaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivities.this, SignInActivities.class);
                startActivity(intent);
                finish();

            }
        });


    }

}