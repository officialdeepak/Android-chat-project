package com.example.yupchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.yupchat.Adapters.ChatsAdapter;
import com.example.yupchat.databinding.ActivityGroupChatBinding;
import com.example.yupchat.models.Messagemodel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class GroupChat extends AppCompatActivity {

    ActivityGroupChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();


        binding.Backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupChat.this,MainActivity.class);
                startActivity(intent);
            }
        });
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<Messagemodel>messagemodels = new ArrayList<>();

        final String senderId = FirebaseAuth.getInstance().getUid();
        binding.userName.setText("Friend Group");


        final ChatsAdapter adapter = new ChatsAdapter(messagemodels,this);
        binding.ChatRecyView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.ChatRecyView.setLayoutManager(layoutManager);

        database.getReference().child("Group chat")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messagemodels.clear();
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                            Messagemodel model = dataSnapshot.getValue(Messagemodel.class);
                            messagemodels.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        binding.sendimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.messageUser.getText().toString().isEmpty()){
                    binding.messageUser.setError("Enter your Message");
                    return;
                }

                final String message = binding.messageUser.getText().toString();
                final Messagemodel model = new Messagemodel(senderId,message);
                model.setTimestamp(new Date().getTime());
                binding.messageUser.setText("");

                database.getReference().child("Group chat").push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
            }
        });
    }
}