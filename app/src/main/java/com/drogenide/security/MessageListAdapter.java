package com.drogenide.security;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MyViewHolder> {

    private ArrayList<MessageModel> messagelist;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView msg;

        public MyViewHolder(View view) {
            super(view);
            msg= (TextView) view.findViewById(R.id.txt_msg);
        }
    }


    public MessageListAdapter(ArrayList<MessageModel> moviesList) {
        this.messagelist = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MessageModel msg = messagelist.get(position);
        holder.msg.setText(msg.getMsg());
        
    }

    @Override
    public int getItemCount() {
        return messagelist.size();
    }
}
