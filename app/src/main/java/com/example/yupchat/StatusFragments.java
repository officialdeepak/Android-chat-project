package com.example.yupchat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yupchat.databinding.FragmentStatusFragmentsBinding;


public class StatusFragments extends Fragment {
    FragmentStatusFragmentsBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStatusFragmentsBinding.inflate(inflater, container, false);


        return binding.getRoot();


    }
}
