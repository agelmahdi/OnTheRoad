package com.graduation.a3ltreq.ontheroad.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.graduation.a3ltreq.ontheroad.R;
import com.graduation.a3ltreq.ontheroad.model.ChatMessage;
import com.graduation.a3ltreq.ontheroad.model.Provider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed El-Mahdi on 7/2/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private final ArrayList<ChatMessage> mChat;

    private final Context mContext;

    public ChatAdapter(ArrayList<ChatMessage> mChat, Context mContext) {
        this.mChat = mChat;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.message_list_item, parent, false);
        return new ChatAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final ChatMessage message = mChat.get(position);



        holder.itemView.setTag(message.getId());
        holder.name.setText(message.getMessageUser());
        holder.description.setText(message.getMessageText());
        holder.duration.setText(message.getMessageTime());

    }

    public void add(List<ChatMessage> chatMessages) {
        mChat.clear();
        mChat.addAll(chatMessages);
        notifyDataSetChanged();
    }
    public ArrayList<ChatMessage> getMessages() {
        return mChat;
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final TextView name;
        public final TextView description;
        public final TextView duration;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.name =  itemView.findViewById(R.id.message_user);
            this.description =  itemView.findViewById(R.id.message_text);
            this.duration =  itemView.findViewById(R.id.message_time);
        }
    }
}
