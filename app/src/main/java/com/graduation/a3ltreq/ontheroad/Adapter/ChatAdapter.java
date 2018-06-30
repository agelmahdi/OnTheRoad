package com.graduation.a3ltreq.ontheroad.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.graduation.a3ltreq.ontheroad.R;
import com.graduation.a3ltreq.ontheroad.model.ChatMessage;
import com.graduation.a3ltreq.ontheroad.model.Provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.graduation.a3ltreq.ontheroad.activity.LoginActivity.PREFERENCE_KEY;
import static com.graduation.a3ltreq.ontheroad.activity.LoginActivity.PREF_USER_ID;
import static com.graduation.a3ltreq.ontheroad.activity.LoginActivity.PREF_USER_NAME;

/**
 * Created by Ahmed El-Mahdi on 7/2/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private final ArrayList<ChatMessage> mChat;

     private Context mContext;

    SharedPreferences SharedPrefs;

    public ChatAdapter(ArrayList<ChatMessage> mChat, Context context) {
        this.mChat = mChat;
        this.mContext = context;


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;


        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.messege_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.messege_recived, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        final ChatMessage message = mChat.get(position);

        switch (viewHolder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) viewHolder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) viewHolder).bind(message);
        }

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

    @Override
    public int getItemViewType(int position) {
        SharedPrefs = mContext.getSharedPreferences(PREFERENCE_KEY, 0);
        ChatMessage message = mChat.get(position);
        if (message.getMessageUser().equals(SharedPrefs.getString(PREF_USER_NAME,""))) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }


    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        }

        void bind(ChatMessage message) {
            messageText.setText(message.getMessageText());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getMessageTime());
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;

        ImageView imageView;
        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
            imageView = itemView.findViewById(R.id.image_message_profile);
        }

        void bind(ChatMessage message) {
            messageText.setText(message.getMessageText());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getMessageTime());

            nameText.setText(message.getMessageUser());

            imageView.setImageResource(R.drawable.ic_mechanic_profile);

            // Insert the profile image from the URL into the ImageView.
        }
    }
}
