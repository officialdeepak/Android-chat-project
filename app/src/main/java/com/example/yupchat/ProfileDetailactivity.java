package com.example.yupchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.yupchat.Adapters.ChatsAdapter;
import com.example.yupchat.databinding.ProfileDetailBinding;
import com.example.yupchat.models.Messagemodel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import okhttp3.internal.cache.DiskLruCache;

import static com.example.yupchat.databinding.ProfileDetailBinding.inflate;

public class ProfileDetailactivity extends AppCompatActivity {


    ProfileDetailBinding binding;

    FirebaseDatabase database;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ProfileDetailBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        getSupportActionBar().hide();


           final String senderId = auth.getUid();
            String recieverId = getIntent().getStringExtra("userId");
            String userName = getIntent().getStringExtra("userName");
            String profilepic = getIntent().getStringExtra("profilepic");

            binding.userName.setText(userName);
            Picasso.get().load(profilepic).placeholder(R.drawable.avtar).into(binding.profileImage);



            binding.Backarrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProfileDetailactivity.this, MainActivity.class);
                    startActivity(intent);
                }

            });

            final ArrayList<Messagemodel> messagemodels = new ArrayList<>();


            final ChatsAdapter chatsAdapter = new ChatsAdapter(messagemodels, this, recieverId);
            binding.ChatRecyView.setAdapter(chatsAdapter);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            binding.ChatRecyView.setLayoutManager(layoutManager);



            //database node
            final String senderRoom = (senderId + recieverId);
            final String receiverRoom = (recieverId + senderId);


            database.getReference().child("chats")
                 .child(senderRoom).addValueEventListener(new ValueEventListener() {
             @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                 messagemodels.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
             Messagemodel models = snapshot1.getValue(Messagemodel.class);
             models.setMessageid(snapshot1.getKey());

              messagemodels.add(models);
             }
             chatsAdapter.notifyDataSetChanged();
            }


            @Override
              public void onCancelled(@NonNull DatabaseError error) {

             }
            });

           binding.sendimg.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(binding.messageUser.getText().toString().isEmpty()) {
                        binding.messageUser.setError("Enter your Message");
                        return;
                    }
                    String messsage = binding.messageUser.getText().toString();
                    final Messagemodel models = new Messagemodel(senderId, messsage);
                    models.setTimestamp(new Date().getTime());
                    binding.messageUser.setText("");
                    database.getReference().child("chats").child(senderRoom).push().setValue(models)
                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    database.getReference().child("chats").child(receiverRoom).push().setValue(models)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                }
                                            });
                                }
                            });

                }
            });




        }

    }




