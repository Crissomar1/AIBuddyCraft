package com.crissomar.aibuddycraft.utils;

public class ChatMessage {
    //Create a class to store the messages for the conversation with openAI
    private String sender;
    private String message;

    public ChatMessage(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }


    public String toOpenAIString() {
        return "{\"role\":\""+getSender()+"\",\"content\":\"" + getMessage() + "\"}";
    }

}
