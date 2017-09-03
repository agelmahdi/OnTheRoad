package com.graduation.a3ltreq.ontheroad.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ahmed El-Mahdi on 7/2/2017.
 */
public class ChatMessage implements Parcelable {

    private ChatMessage(Parcel in) {
        id = in.readInt();
        messageText = in.readString();
        messageUser = in.readString();
        messageTime = in.readString();
    }

    public static final Creator<ChatMessage> CREATOR = new Creator<ChatMessage>() {
        @Override
        public ChatMessage createFromParcel(Parcel in) {
            return new ChatMessage(in);
        }

        @Override
        public ChatMessage[] newArray(int size) {
            return new ChatMessage[size];
        }
    };

    public int getId() {
        return id;
    }

    private int id;
    private String messageText;
    private String messageUser;
    private String messageTime;

    public ChatMessage(String messageText, String messageUser) {
        this.messageText = messageText;
        this.messageUser = messageUser;

        // Initialize to current time

    }

    public ChatMessage(JSONObject ChatMessage) throws JSONException {
        this.id = ChatMessage.getInt("id");
        this.messageUser = ChatMessage.getString("name");
        this.messageText = ChatMessage.getString("msg");
        this.messageTime = ChatMessage.getString("created_at");

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(messageText);
        dest.writeString(messageUser);
        dest.writeString(messageTime);
    }


}

