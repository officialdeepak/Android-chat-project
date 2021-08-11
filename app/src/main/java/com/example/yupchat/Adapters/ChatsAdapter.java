package com.example.yupchat.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yupchat.R;
import com.example.yupchat.models.Messagemodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatsAdapter extends RecyclerView.Adapter {

    ArrayList<Messagemodel> messagemodels;
    Context context;
    String recId;


    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    public ChatsAdapter(ArrayList<Messagemodel> messagemodels, Context context) {
        this.messagemodels = messagemodels;
        this.context = context;
    }

    public ChatsAdapter(ArrayList<Messagemodel> messagemodels, Context context, String recId) {
        this.messagemodels = messagemodels;
        this.context = context;
        this.recId = recId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENDER_VIEW_TYPE)

        {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender ,parent,false);

        return new SenderViewVolder(view);

        }
        else {

                View view = LayoutInflater.from(context).inflate(R.layout.sample_receiver, parent, false);

                return new ReceiverViewVolder(view);


        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messagemodels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid()))
        {

        return SENDER_VIEW_TYPE;

        }
        else {
            return RECEIVER_VIEW_TYPE;
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Messagemodel messagemodel = messagemodels.get(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this message")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String senderRoom = FirebaseAuth.getInstance().getUid() + recId;
                                database.getReference().child("chats").child(senderRoom)
                                        .child(messagemodel.getMessageid())
                                        .setValue(null);

                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                return false;
            }
        });
        if(holder.getClass() == SenderViewVolder.class){
            ((SenderViewVolder)holder).SenderMsg.setText(messagemodel.getMessage());
        }
        else {
            ((ReceiverViewVolder)holder).receiverMsg.setText(messagemodel.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return messagemodels.size();
    }

    public  class ReceiverViewVolder extends RecyclerView.ViewHolder {

        TextView receiverMsg , receiverTime;

        public ReceiverViewVolder(@NonNull View itemView) {


            super(itemView);
            receiverMsg = itemView.findViewById(R.id.ReceiverText);
            receiverTime = itemView.findViewById(R.id.ReceiverTime);


        }
    }

    public class SenderViewVolder extends RecyclerView.ViewHolder{
        TextView SenderMsg , SenderTime;

        public SenderViewVolder(@NonNull View itemView) {
            super(itemView);

            SenderMsg = itemView.findViewById(R.id.SenderMsg);
            SenderTime = itemView.findViewById(R.id.SenderTime);
        }
    }

}
