package com.example.yupchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Toast;

import com.example.yupchat.databinding.ActivityForgetPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    ActivityForgetPasswordBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();

        binding.Goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgetPassword.this, SignInActivities.class);
                startActivity(intent);
            }


        });


        binding.Resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.Resetbtn.setEnabled(false);
                binding.Resetbtn.setTextColor(Color.argb(50,255,255,255));


                auth.sendPasswordResetEmail(binding.etEmailid.getText().toString())

                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgetPassword.this, "email send Successfully", Toast.LENGTH_LONG).show();

                                } else {
                                    String error = task.getException().getMessage();
                                    binding.Resetbtn.setEnabled(true);
                                    binding.Resetbtn.setTextColor(Color.rgb(255,255,255));

                                }

                            }
                        });
            }
        });
    }


    private void checkInputs() {


        if (TextUtils.isEmpty(binding.etEmailid.getText())) {
            binding.Resetbtn.setEnabled(false);

        } else {
            binding.Resetbtn.setEnabled(true);
        }
    }
}