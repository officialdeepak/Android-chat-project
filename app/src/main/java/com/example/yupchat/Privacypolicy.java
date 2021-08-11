package com.example.yupchat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.yupchat.databinding.ActivityPrivacypolicyBinding;

public class Privacypolicy extends AppCompatActivity {
    ActivityPrivacypolicyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivacypolicyBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
    }
}